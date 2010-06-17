package se.raek.charset;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

public class Utf8WithFallbackCharsetDecoder extends CharsetDecoder {
	
	private NonAsciiByteDecoder byteDecoder;
	private int currentValue;
	private int bytesLeft;
	private boolean byteAlreadyRead;
	private byte currentByte;
	private boolean incompleteSupplementary;
	private char supplementaryChar;
	private boolean incompleteFlush;
	private ByteBuffer buffer;

	public Utf8WithFallbackCharsetDecoder(Charset cs,
			float averageCharsPerByte, float maxCharsPerByte,
			NonAsciiByteDecoder byteDecoder) {
		super(cs, averageCharsPerByte, maxCharsPerByte);
		
		this.byteDecoder = byteDecoder;
		this.buffer = ByteBuffer.allocateDirect(3);
		this.implReset();
	}

	@Override
	protected CoderResult decodeLoop(ByteBuffer in, CharBuffer out) {
		while (true) {
			if (incompleteFlush) {
				// The flushing of the buffer could not complete the last time
				if (!flushBuffer(out)) {
					// ...and neither this time
					return CoderResult.OVERFLOW;
				}
			}

			// We need to be able to write
			if (!out.hasRemaining()) {
				return CoderResult.OVERFLOW;
			}
			
			if (incompleteSupplementary) {
				out.put(supplementaryChar);
				incompleteSupplementary = false;
				buffer.clear();
				if (!out.hasRemaining()) {
					return CoderResult.OVERFLOW;
				}
			}

			// We need to be able to read, if we haven't already
			if (!byteAlreadyRead) {
				if (!in.hasRemaining()) {
					return CoderResult.UNDERFLOW;
				}
				currentByte = in.get();
				// Flag that the byte has been read in case we need to exit
				// the loop before we are done
				byteAlreadyRead = true;
			}
			
			if (isContinuation(currentByte)) {
				currentValue = addContinuationData(currentValue, currentByte);
				// Is this the last continuation byte?
				if (--bytesLeft > 0) {
					buffer.put(currentByte);
				} else {
					// The buffer together with currentByte contains one valid
					// UTF-8 encoded character, whose code point is in currentValue
					char[] chars = Character.toChars(currentValue);
					out.put(chars[0]);
					if (chars.length == 2) {
						if (!out.hasRemaining()) {
							incompleteSupplementary = true;
							supplementaryChar = chars[1];
							byteAlreadyRead = false;
							return CoderResult.OVERFLOW;
						}
						out.put(chars[1]);
					}
					buffer.clear();
				}
				byteAlreadyRead = false;
				continue;
			}
			
			if (bytesLeft > 0) {
				// Make sure any bytes of an incomplete UTF-8 encoded character is
				// flushed out before we continue
				if (!flushBuffer(out)) {
					// Overflow occured, so we'll have to continue this later.
					// When the buffer is flushed, bytesLeft will be cleared to 0.
					return CoderResult.OVERFLOW;
				}
			}
			
			// Here, the buffer is always empty and bytesLeft = 0
			
			if (isAscii(currentByte)) {
				// Ordinary US-ASCII, write it
				out.put((char) currentByte);
				byteAlreadyRead = false;
				continue;
			}

			if (isTwoByteStart(currentByte)) {
				buffer.put(currentByte);
				currentValue = twoByteStartData(currentByte);
				bytesLeft = 1;
			} else if (isThreeByteStart(currentByte)) {
				buffer.put(currentByte);
				currentValue = threeByteStartData(currentByte);
				bytesLeft = 2;
			} else if (isFourByteStart(currentByte)) {
				buffer.put(currentByte);
				currentValue = fourByteStartData(currentByte);
				bytesLeft = 3;
			} else {
				// The byte is not a legal UTF-8 byte
				out.put(byteDecoder.decodeByte(currentByte));
			}
			
			byteAlreadyRead = false;
		}
	}

	@Override
	protected CoderResult implFlush(CharBuffer out) {
		if (flushBuffer(out)) {
			return CoderResult.UNDERFLOW;
		} else {
			return CoderResult.OVERFLOW;
		}
	}

	@Override
	protected void implReset() {
		byteAlreadyRead = false;
		incompleteFlush = false;
		incompleteSupplementary = false;
		bytesLeft = 0;
		buffer.clear();
	}
	
	/**
	 * Decodes the bytes in the buffer and writes them to out. Returns true if
	 * the whole buffer was written and false if overflow occurred. Also sets
	 * incompleteFlush to true and resets it to false if this method returned true.
	 * @param out  the CharBuffer to write the decoded characters to
	 * @return  true if the whole buffer was decoded and written, false if
	 * 			overflow occurred
	 */
	private boolean flushBuffer(CharBuffer out) {
		incompleteFlush = true;
		buffer.flip();
		while (true) {
			if (!buffer.hasRemaining()) {
				incompleteFlush = false;
				bytesLeft = 0;
				buffer.clear();
				return true;
			}
			char c = byteDecoder.decodeByte(buffer.get());
			out.put(c);
			if (!out.hasRemaining()) {
				buffer.compact();
				return false;
			}
		}
	}
	
	private static boolean isAscii(byte b) {
		return ((int) b & 0x80) == 0x00;
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

}
