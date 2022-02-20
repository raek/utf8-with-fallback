package se.raek.charset;

import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class Utf8WithWindows1252FallbackCharsetTest {
	private Charset charset;

	@Before
	public void setUp() {
		charset = new Utf8WithWindows1252FallbackCharset();
	}

	@Test
	public void canEncode_always_false() {
		assertFalse(charset.canEncode());
	}

	@Test
	public void containsCharset_utf8_true() {
		assertTrue(charset.contains(StandardCharsets.UTF_8));
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
		final CharsetDecoder decoder = charset.newDecoder();
		assertEquals(Utf8WithFallbackCharsetDecoder.class, decoder.getClass());
	}

	@Test(expected=UnsupportedOperationException.class)
	public void newEncoder_always_exceptionThrown() {
		charset.newEncoder();
	}
}
