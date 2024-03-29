package se.raek.charset;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class Utf8WithIso885915FallbackCharsetDecoderTest {
	private static final Charset FALLBACK_CHARSET = Charset.forName("ISO-8859-15");
	
	private CharsetDecoder decoder;

	@Before
	public void setUp() {
		decoder = new Utf8WithIso885915FallbackCharset().newDecoder();
	}

	@Test
	public void testDecodingAscii() throws CharacterCodingException {
		final ByteBuffer buffer = ByteBuffer.wrap("abc".getBytes(StandardCharsets.US_ASCII));
		assertEquals("abc", decoder.decode(buffer).toString());
	}

	@Test
	public void testDecodingUtf8() throws CharacterCodingException {
		final ByteBuffer buffer = ByteBuffer.wrap("köttfärssås".getBytes(StandardCharsets.UTF_8));
		assertEquals("köttfärssås", decoder.decode(buffer).toString());
	}

	@Test
	public void testDecodingIso885915() throws CharacterCodingException {
		final ByteBuffer buffer = ByteBuffer.wrap("köttfärssås".getBytes(FALLBACK_CHARSET));
		assertEquals("köttfärssås", decoder.decode(buffer).toString());
	}

	@Test
	public void testDecodingTwoByteChars() throws CharacterCodingException {
		final String s = "абвг"; // Cyrillic
		final ByteBuffer buffer = ByteBuffer.wrap(s.getBytes(StandardCharsets.UTF_8));
		assertEquals(s, decoder.decode(buffer).toString());
	}

	@Test
	public void testDecodingThreeByteChars() throws CharacterCodingException {
		final String s = "あいうえお"; // Hiragana
		final ByteBuffer buffer = ByteBuffer.wrap(s.getBytes(StandardCharsets.UTF_8));
		assertEquals(s, decoder.decode(buffer).toString());
	}

	@Test
	public void testDecodingFourByteChars() throws CharacterCodingException {
		final String s = "𐌰𐌱𐌲𐌳"; // Gothic
		final ByteBuffer buffer = ByteBuffer.wrap(s.getBytes(StandardCharsets.UTF_8));
		assertEquals(s, decoder.decode(buffer).toString());
	}

	@Test
	public void testDecodingUtf8FollowedByIso885915() throws CharacterCodingException {
		// utf-8("åäö") + iso-8859-15("åäö")
		final byte[] bytes = {
				(byte) 0xC3, (byte) 0xA5, (byte) 0xC3, (byte) 0xA4, (byte) 0xC3, (byte) 0xB6,
				(byte) 0xE5, (byte) 0xE4, (byte) 0xF6
		};
		final ByteBuffer buffer = ByteBuffer.wrap(bytes);
		assertEquals("åäöåäö", decoder.decode(buffer).toString());
	}

	@Test
	public void testDecodingUtf8EmojiFollowedByIso885915() throws CharacterCodingException {
		// utf-8("👨ẞ") + iso-8859-15("¢£€")
		final byte[] bytes = {
				(byte) 0xF0, (byte) 0x9F, (byte) 0x91, (byte) 0xA8, (byte) 0xE1, (byte) 0xBA, (byte) 0x9E,
				(byte) 0xA2, (byte) 0xA3, (byte) 0xA4
		};
		final ByteBuffer buffer = ByteBuffer.wrap(bytes);
		assertEquals("\uD83D\uDC68ẞ¢£€", decoder.decode(buffer).toString());
	}

	@Test
	public void testDecodingIso885915FollowedByUtf8() throws CharacterCodingException {
		// iso-8859-15("åäö") + utf-8("åäö")
		final byte[] bytes = {
				(byte) 0xE5, (byte) 0xE4, (byte) 0xF6,
				(byte) 0xC3, (byte) 0xA5, (byte) 0xC3, (byte) 0xA4, (byte) 0xC3, (byte) 0xB6
		};
		final ByteBuffer buffer = ByteBuffer.wrap(bytes);
		assertEquals("åäöåäö", decoder.decode(buffer).toString());
	}

	@Ignore
	@Test
	public void testDecodingIso885915FollowedByUtf8Fail() throws CharacterCodingException {
		// iso-8859-15("åœ€") + utf-8("åœ€")
		final byte[] bytes = {
				(byte) 0xE5, (byte) 0xBD, (byte) 0xA4,
				(byte) 0xC3, (byte) 0xA5, (byte) 0xC5, (byte) 0x93, (byte) 0xE2, (byte) 0x82, (byte) 0xAC
		};
		final ByteBuffer buffer = ByteBuffer.wrap(bytes);
		assertEquals("åœ€åœ€", decoder.decode(buffer).toString()); // 彤åœ€
	}

	@Test
	public void testDecodingIllegalUtf8Bytes() throws CharacterCodingException {
		final String s = "üýþÿ";
		final ByteBuffer buffer = ByteBuffer.wrap(s.getBytes(FALLBACK_CHARSET));
		assertEquals(s, decoder.decode(buffer).toString());
	}

	@Test
	public void testDecodingSolitaryUtf8ContinuationByte() throws CharacterCodingException {
		final String s = "\u0094";
		final ByteBuffer buffer = ByteBuffer.wrap(s.getBytes(FALLBACK_CHARSET));
		assertEquals(s, decoder.decode(buffer).toString());
	}

	@Test
	public void testDecodingTwoByteEncodedAscii() throws CharacterCodingException {
		final String s = "\u00C1\u0081";
		final ByteBuffer buffer = ByteBuffer.wrap(s.getBytes(FALLBACK_CHARSET));
		assertEquals(s, decoder.decode(buffer).toString());
	}

	@Test
	public void testDecodingThreeByteEncodedAscii() throws CharacterCodingException {
		final String s = "\u00E0\u0081\u0081";
		final ByteBuffer buffer = ByteBuffer.wrap(s.getBytes(FALLBACK_CHARSET));
		assertEquals(s, decoder.decode(buffer).toString());
	}

	@Test
	public void testDecodingFourByteEncodedAscii() throws CharacterCodingException {
		final String s = "\u00F0\u0080\u0081\u0081";
		final ByteBuffer buffer = ByteBuffer.wrap(s.getBytes(FALLBACK_CHARSET));
		assertEquals(s, decoder.decode(buffer).toString());
	}

	@Test
	public void testDecodingOutOfRangeUnicodeCodePoint() throws CharacterCodingException {
		final String s = "\u00F5\u0080\u0080\u0080";
		final ByteBuffer buffer = ByteBuffer.wrap(s.getBytes(FALLBACK_CHARSET));
		assertEquals(s, decoder.decode(buffer).toString());
	}

	@Test
	public void testDecodingIncompleteUtf8Sequence() throws CharacterCodingException {
		final byte[] bytes = { (byte) 0xF0, (byte) 0x90, (byte) 0x8C };
		final ByteBuffer buffer = ByteBuffer.wrap(bytes);
		assertEquals("\u00F0\u0090\u008c", decoder.decode(buffer).toString());
	}

	@Test
	public void testMedialBufferOverflow() {
		// Input the first two bytes of a three byte sequence followed by ASCII
		// 'a'. Provide a one char buffer, so that overflow will occur when the
		// first two bytes are being flushed. Then, give the one char buffer
		// again, so overflow will occur one more time before proceeding.
		final byte[] bytes = { (byte) 0xE3, (byte) 0x81, (byte) 0x61 };
		final ByteBuffer in = ByteBuffer.wrap(bytes);
		final CharBuffer small = CharBuffer.allocate(1);
		final CharBuffer result = CharBuffer.allocate(10);

		decoder.reset();
		assertTrue(decoder.decode(in, small, true).isOverflow());
		small.flip();
		assertEquals(1, small.length());
		result.put(small.get());
		small.clear();
		assertTrue(decoder.decode(in, small, true).isOverflow());
		small.flip();
		assertEquals(1, small.length());
		result.put(small.get());
		assertTrue(decoder.decode(in, result, true).isUnderflow());
		assertTrue(decoder.flush(result).isUnderflow());
		result.flip();
		assertEquals("\u00E3\u0081a", result.toString());
	}

	@Test
	public void testFlushBufferOverflow() {
		// Input the first two bytes of a three byte sequence and stop there.
		// When flushing, provide a one char buffer, so that overflow will occur.
		final byte[] bytes = { (byte) 0xE3, (byte) 0x81 };
		final ByteBuffer in = ByteBuffer.wrap(bytes);
		final CharBuffer small = CharBuffer.allocate(1);
		final CharBuffer result = CharBuffer.allocate(10);

		decoder.reset();
		assertTrue(decoder.decode(in, result, true).isUnderflow());
		result.flip();
		assertFalse(result.hasRemaining());
		result.clear();
		assertTrue(decoder.flush(small).isOverflow());
		small.flip();
		assertEquals(1, small.remaining());
		result.put(small.get());
		small.clear();
		assertTrue(decoder.flush(result).isUnderflow());
		result.flip();
		assertEquals("\u00E3\u0081", result.toString());
	}
}
