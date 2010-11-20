package se.raek.charset;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;

import org.junit.Before;
import org.junit.Test;

public class Utf8WithWindows1252FallbackCharsetDecoderTest {

	private CharsetDecoder decoder;

	@Before
	public void setUp() throws Exception {
		decoder = new Utf8WithWindows1252FallbackCharset().newDecoder();
	}

	@Test
	public void testDecodingAscii() throws UnsupportedEncodingException,
			CharacterCodingException {
		ByteBuffer buffer = ByteBuffer.wrap("abc".getBytes("US-ASCII"));
		assertEquals("abc", decoder.decode(buffer).toString());
	}
	
	@Test
	public void testDecodingControlCharReplacements() throws CharacterCodingException {
		byte[] testBytes = new byte[32];
		for (int i = 0; i < testBytes.length; i++) {
			testBytes[i] = (byte) (i + 0x80);
		}
		assertEquals("€\u0081‚ƒ„…†‡ˆ‰Š‹Œ\u008DŽ\u008F\u0090‘’“”•–—˜™š›œ\u009DžŸ",
				decoder.decode(ByteBuffer.wrap(testBytes)).toString());
	}
	
	@Test
	public void testDecodingIso88591Overlap() throws CharacterCodingException {
		byte[] testBytes = new byte[32];
		for (int i = 0; i < testBytes.length; i++) {
			testBytes[i] = (byte) (i + 0xC0);
		}
		assertEquals("ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞß",
				decoder.decode(ByteBuffer.wrap(testBytes)).toString());
	}
}
