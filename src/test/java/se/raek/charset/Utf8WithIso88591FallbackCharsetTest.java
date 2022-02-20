package se.raek.charset;

import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class Utf8WithIso88591FallbackCharsetTest {
	
	private Charset charset;

	@Before
	public void setUp() throws Exception {
		charset = new Utf8WithIso88591FallbackCharset();
	}

	@Test
	public void canEncode_always_false() {
		assertFalse(charset.canEncode());
	}

	@Test
	public void containsCharset_utf8_true() {
		assertTrue(charset.contains(Charset.forName("UTF-8")));
	}

	@Test
	public void displayName_always_notNull() {
		assertNotNull(charset.displayName());
	}

	@Test
	public void displayNameLocale_always_notNull() {
		assertNotNull(charset.displayName(Locale.getDefault()));
	}

	@Test
	public void newDecoder_always_correctClass() {
		CharsetDecoder decoder = charset.newDecoder();
		assertEquals(Utf8WithFallbackCharsetDecoder.class, decoder.getClass());
	}

	@Test(expected=UnsupportedOperationException.class)
	public void newEncoder_always_exceptionThrown() {
		charset.newEncoder();
	}

}
