package se.raek.charset;

import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class Utf8WithIso88591FallbackCharsetTest {
	private Charset charset;

	@Before
	public void setUp() {
		charset = new Utf8WithIso88591FallbackCharset();
	}

	@Test
	public void canEncodeAlwaysFalse() {
		assertFalse(charset.canEncode());
	}

	@Test
	public void containsCharsetUtf8True() {
		assertTrue(charset.contains(StandardCharsets.UTF_8));
	}

	@Test
	public void displayNameAlwaysNotNull() {
		assertNotNull(charset.displayName());
	}

	@Test
	public void displayNameLocaleAlwaysNotNull() {
		assertNotNull(charset.displayName(Locale.getDefault()));
	}

	@Test
	public void newDecoderAlwaysCorrectClass() {
		final CharsetDecoder decoder = charset.newDecoder();
		assertEquals(Utf8WithFallbackCharsetDecoder.class, decoder.getClass());
	}

	@Test(expected=UnsupportedOperationException.class)
	public void newEncoderAlwaysExceptionThrown() {
		charset.newEncoder();
	}
}
