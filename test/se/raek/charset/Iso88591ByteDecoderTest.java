package se.raek.charset;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Iso88591ByteDecoderTest {
	
	private NonAsciiByteDecoder byteDecoder;

	@Before
	public void setUp() throws Exception {
		byteDecoder = new Iso88591ByteDecoder();
	}

	@Test
	public void testDecodeByte() {
		char[] testChars = {
				'\u0080', '\u0087', '\u0098', '\u009F',
				'\u00A0', '§', '¸', '¿',
				'À', 'Ç', 'Ø', 'ß',
				'à', 'ç', 'ø', 'ÿ'
		};
		for (char testChar : testChars) {
			byte b = (byte) testChar;
			assertEquals(testChar, byteDecoder.decodeByte(b));
		}
	}

}
