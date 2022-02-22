package se.raek.charset;

import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class Utf8WithFallbackCharsetProviderTest {
	private Utf8WithFallbackCharsetProvider charsetProvider;

	@Before
	public void setUp() {
		charsetProvider = new Utf8WithFallbackCharsetProvider();
	}
	
	@Test
	public void testCharsetIso88591Registered() {
		assertNotNull(Charset.forName("X-UTF-8_with_ISO-8859-1_fallback"));
	}

	@Test
	public void testCharsetIso885915Registered() {
		assertNotNull(Charset.forName("X-UTF-8_with_ISO-8859-15_fallback"));
	}

	@Test
	public void testCharsetWindows1252Registered() {
		assertNotNull(Charset.forName("X-UTF-8_with_windows-1252_fallback"));
	}

	@Test
	public void charsetForNameKnownNameIso88591NotNull() {
		assertNotNull(charsetProvider.charsetForName("X-UTF-8_with_ISO-8859-1_fallback"));
	}

	@Test
	public void charsetForNameKnownNameIso885915NotNull() {
		assertNotNull(charsetProvider.charsetForName("X-UTF-8_with_ISO-8859-15_fallback"));
	}

	@Test
	public void charsetForNameKnownNameWindows1252NotNull() {
		assertNotNull(charsetProvider.charsetForName("X-UTF-8_with_windows-1252_fallback"));
	}

	@Test
	public void charsetForNameKnownNameIso88591WeirdCaseNotNull() {
		assertNotNull(charsetProvider.charsetForName("X-uTf-8_wiTH_isO-8859-1_FALLback"));
	}

	@Test
	public void charsetForNameKnownNameIso885915WeirdCaseNotNull() {
		assertNotNull(charsetProvider.charsetForName("X-uTf-8_wiTH_isO-8859-15_FALLback"));
	}

	@Test
	public void charsetForNameKnownNameWindows1252WeirdCaseNotNull() {
		assertNotNull(charsetProvider.charsetForName("X-uTf-8_wiTH_wiNDowS-1252_FALLback"));
	}

	@Test
	public void charsetForNameKnownAliasIso88591NotNull() {
		assertNotNull(charsetProvider.charsetForName("X-UTF-8_with_ISO-LATIN-1_fallback"));
	}

	@Test
	public void charsetForNameKnownAliasIso885915NotNull() {
		assertNotNull(charsetProvider.charsetForName("X-UTF-8_with_ISO-LATIN-9_fallback"));
	}

	@Test
	public void charsetForNameKnownAliasWindows1252NotNull() {
		assertNotNull(charsetProvider.charsetForName("X-UTF-8_with_cp1252_fallback"));
	}

	@Test
	public void charsetForNameUnknownNameNull() {
		assertNull(charsetProvider.charsetForName("X-BOGUS-CHARSET"));
	}

	@Test
	public void charsetsAlwaysCharsetPresent() {
		final Set<String> charsetsToFind = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		charsetsToFind.addAll(Arrays.asList(
				"X-UTF-8_with_ISO-8859-1_fallback",
				"X-UTF-8_with_ISO-8859-15_fallback",
				"X-UTF-8_with_windows-1252_fallback"
		));
		final Iterator<Charset> it = charsetProvider.charsets();
		while (it.hasNext() && !charsetsToFind.isEmpty()) {
			final Charset charset = it.next();
			charsetsToFind.remove(charset.name());
		}
		if (!charsetsToFind.isEmpty()) {
			fail(String.format("Charset %s not provided", charsetsToFind.iterator().next()));
		}
	}
}
