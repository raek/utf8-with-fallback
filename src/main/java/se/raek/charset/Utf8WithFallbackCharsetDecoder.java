package se.raek.charset;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

/**
 * General charset decoder for ASCII-compatible single-byte charsets.
 */
public final class Utf8WithFallbackCharsetDecoder extends CharsetDecoder {
	/**
	 * The decoder to fall back to when a byte sequence is not valid UTF-8.
	 */
	private final NonAsciiByteDecoder byteDecoder;

	/**
	 * Has the next byte already been read? If so, it is in {@link #lookahead}.
	 */
	private boolean lookaheadRead;

	/**
	 * Storage for lookahead byte.
	 *
	 * {@link #peek(ByteBuffer)} and {@link #take(ByteBuffer)} will use this
	 * byte instead of reading one if {@link #lookaheadRead} is true.
	 */
	private byte lookahead;

	/**
	 * Current state of the decoding transducer.
	 */
	private State state;

	/**
	 * Returns the next byte from the input without consuming it.
	 * 
	 * May only be called when it is known that there is at least one byte available.
	 * 
	 * @param in The ByteBuffer to read from
	 * @return The next byte from the input
	 */
	private byte peek(final ByteBuffer in) {
		if (!lookaheadRead) {
			lookahead = in.get();
			lookaheadRead = true;
		}
		return lookahead;
	}

	/**
	 * Returns the next byte from the input and removes it. May only be called
	 * when it is know that there is at least one byte available.
	 * 
	 * @param in The ByteBuffer to read from
	 * @return The next byte from the input
	 */
	private byte take(final ByteBuffer in) {
		if (lookaheadRead) {
			lookaheadRead = false;
			return lookahead;
		}
		return in.get();
	}

	public Utf8WithFallbackCharsetDecoder(final Charset cs,
										  final float averageCharsPerByte,
										  final float maxCharsPerByte,
										  final NonAsciiByteDecoder byteDecoder) {
		super(cs, averageCharsPerByte, maxCharsPerByte);

		this.byteDecoder = byteDecoder;
		this.implReset();
	}

	@Override
	protected void implReset() {
		lookaheadRead = false;
		state = new Start();
	}

	@Override
	protected CoderResult decodeLoop(final ByteBuffer in, final CharBuffer out) {
		while (true) {
			if (!lookaheadRead && !in.hasRemaining()) {
				return CoderResult.UNDERFLOW;
			}
			if (!out.hasRemaining()) {
				return CoderResult.OVERFLOW;
			}
			state = state.next(in, out);
		}
	}

	@Override
	protected CoderResult implFlush(final CharBuffer out) {
		state = state.flush();
		while (true) {
			if (state.isFinal()) {
				return CoderResult.UNDERFLOW;
			}
			if (!out.hasRemaining()) {
				return CoderResult.OVERFLOW;
			}
			state = state.next(null, out);
		}
	}

	/**
	 * State class for the decoding transducer.
	 */
	private interface State {
		/**
		 * Tells whether the state does not have any unfinished read or
		 * write operations. If it does not, it is safe to stop decoding.
		 * 
		 * @return true if, and only if, it is safe to stop decoding in this state
		 */
		boolean isFinal();

		/**
		 * Performs a state transition and returns the next state.
		 * 
		 * @param in the ByteBuffer to read from, if needed
		 * @param out the CharBuffer to write to, if needed
		 * @return the next state
		 */
		State next(ByteBuffer in, CharBuffer out);

		/**
		 * Returns a state that will flush any characters held in this state, if
		 * any, or a final state. The returned state should never attempt to read.
		 * 
		 * @return a final state or state that will flush the remaining chars
		 */
		State flush();
	}

	private class Start implements State {
		@Override
		public boolean isFinal() {
			return true;
		}

		@Override
		public State next(final ByteBuffer in, final CharBuffer out) {
			final byte b = take(in);
			if (isAscii(b)) {
				out.put(decodeAscii(b));
				return this;
			}
			if (isTwoByteStart(b)) {
				return new Partial(b);
			}
			if (isThreeByteStart(b)) {
				return new Partial(b);
			}
			if (isFourByteStart(b)) {
				return new Partial(b);
			}
			out.put(decodeFallback(b));
			return this;
		}

		@Override
		public State flush() {
			return this;
		}
	}

	private final class Partial implements State {
		private final ByteBuffer buffer;
		private int value;

		public Partial(final byte start) {
			int length;

			if (isTwoByteStart(start)) {
				length = 2;
				value = twoByteStartData(start);
			} else if (isThreeByteStart(start)) {
				length = 3;
				value = threeByteStartData(start);
			} else if (isFourByteStart(start)) {
				length = 4;
				value = fourByteStartData(start);
			} else {
				throw new IllegalArgumentException(String.format("Invalid start (%d)", start));
			}

			buffer = ByteBuffer.allocateDirect(length);
			buffer.put(start);
		}

		@Override
		public boolean isFinal() {
			return false;
		}

		@Override
		public State next(final ByteBuffer in, final CharBuffer out) {
			if (!isContinuation(peek(in))) {
				buffer.flip();
				return new FlushBytes(buffer);
			}
			final byte b = take(in);
			buffer.put(b);
			value = addContinuationData(value, b);
			if (buffer.hasRemaining()) {
				return this;
			}
			final int requiredLength = bytesRequired(value);
			if (requiredLength == -1 || buffer.capacity() != requiredLength) {
				buffer.flip();
				return new FlushBytes(buffer);
			}
			final char[] chars = Character.toChars(value);
			out.put(chars[0]);
			return chars.length == 2
					? new FlushSurrogate(chars[1])
					: new Start();
		}

		@Override
		public State flush() {
			buffer.flip();
			return new FlushBytes(buffer);
		}
	}

	private final class FlushBytes implements State {
		private final ByteBuffer buffer;

		public FlushBytes(final ByteBuffer buffer) {
			this.buffer = buffer;
		}

		@Override
		public boolean isFinal() {
			return false;
		}

		@Override
		public State next(final ByteBuffer in, final CharBuffer out) {
			out.put(decodeFallback(buffer.get()));
			if (buffer.hasRemaining()) {
				return this;
			}
			return new Start();
		}

		@Override
		public State flush() {
			return this;
		}
	}

	private final class FlushSurrogate implements State {
		private final char c;

		public FlushSurrogate(final char c) {
			this.c = c;
		}

		@Override
		public boolean isFinal() {
			return false;
		}

		@Override
		public State next(final ByteBuffer in, final CharBuffer out) {
			out.put(c);
			return new Start();
		}

		@Override
		public State flush() {
			return this;
		}
	}

	private char decodeFallback(byte b) {
		return byteDecoder.decodeByte(b);
	}

	private static boolean isAscii(byte b) {
		return ((int) b & 0x80) == 0x00;
	}

	private static char decodeAscii(byte b) {
		return (char) b;
	}

	private static boolean isContinuation(byte b) {
		return ((int) b & 0xC0) == 0x80;
	}

	private static int continuationData(byte b) {
		return (int) b & 0x3F;
	}

	private static int addContinuationData(int value, byte b) {
		return (value << 6) + continuationData(b);
	}

	private static boolean isTwoByteStart(byte b) {
		// 0xC0 and 0xC1 only occurs in overlong encodings
		return ((int) b & 0xE0) == 0xC0;
	}

	private static int twoByteStartData(byte b) {
		return (int) b & 0x1F;
	}

	private static boolean isThreeByteStart(byte b) {
		return ((int) b & 0xF0) == 0xE0;
	}

	private static int threeByteStartData(byte b) {
		return (int) b & 0x0F;
	}

	private static boolean isFourByteStart(byte b) {
		return ((int) b & 0xF8) == 0xF0;
	}

	private static int fourByteStartData(byte b) {
		return (int) b & 0x07;
	}
	
	private static int bytesRequired(int codePoint) {
		if (codePoint < 0x80 && codePoint >= 0) {
			return 1;
		}
		if (codePoint < 0x800) {
			return 2;
		}
		if (codePoint < 0x10000) {
			return 3;
		}
		if (codePoint < 0x110000) {
			return 4;
		}
		return -1;
	}
}
