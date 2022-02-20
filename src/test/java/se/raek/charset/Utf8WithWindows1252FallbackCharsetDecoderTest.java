package se.raek.charset;

import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class Utf8WithWindows1252FallbackCharsetDecoderTest {
	private CharsetDecoder decoder;

	@Before
	public void setUp() {
		decoder = new Utf8WithWindows1252FallbackCharset().newDecoder();
	}

	@Test
	public void testDecodingAscii() throws CharacterCodingException {
		final ByteBuffer buffer = ByteBuffer.wrap("abc".getBytes(StandardCharsets.US_ASCII));
		assertEquals("abc", decoder.decode(buffer).toString());
	}
	
	@Test
	public void testDecodingControlCharReplacements() throws CharacterCodingException {
		final byte[] testBytes = new byte[32];
		for (int i = 0; i < testBytes.length; i++) {
			testBytes[i] = (byte) (i + 0x80);
		}
		assertEquals("€\u0081‚ƒ„…†‡ˆ‰Š‹Œ\u008DŽ\u008F\u0090‘’“”•–—˜™š›œ\u009DžŸ",
				decoder.decode(ByteBuffer.wrap(testBytes)).toString());
	}
	
	@Test
	public void testDecodingIso88591Overlap() throws CharacterCodingException {
		final byte[] testBytes = new byte[32];
		for (int i = 0; i < testBytes.length; i++) {
			testBytes[i] = (byte) (i + 0xC0);
		}
		assertEquals("ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞß",
				decoder.decode(ByteBuffer.wrap(testBytes)).toString());
	}
}
