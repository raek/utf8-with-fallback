package se.raek.charset;

import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class Utf8WithFallbackCharsetProviderTest {
	
	private Utf8WithFallbackCharsetProvider charsetProvider;

	@Before
	public void setUp() throws Exception {
		charsetProvider = new Utf8WithFallbackCharsetProvider();
	}
	
	@Test
	public void testCharsetRegistered() {
		assertNotNull(Charset.forName("X-UTF-8_with_ISO-8859-1_fallback"));
	}

	@Test
	public void charsetForName_knownName_notNull() {
		assertNotNull(charsetProvider.charsetForName("X-UTF-8_with_ISO-8859-1_fallback"));
	}

	@Test
	public void charsetForName_knownNameWeirdCase_notNull() {
		assertNotNull(charsetProvider.charsetForName("X-uTf-8_wiTH_isO-8859-1_FALLback"));
	}

	@Test
	public void charsetForName_knownAlias_notNull() {
		assertNotNull(charsetProvider.charsetForName("X-UTF-8_with_ISO-LATIN-1_fallback"));
	}

	@Test
	public void charsetForName_unknownName_null() {
		assertNull(charsetProvider.charsetForName("X-BOGUS-CHARSET"));
	}

	@Test
	public void charsets_always_charsetPresent() {
		boolean found = false;
		Iterator<Charset> it = charsetProvider.charsets();
		while (it.hasNext()) {
			Charset charset = it.next();
			if (charset.name().equalsIgnoreCase("X-UTF-8_with_ISO-8859-1_fallback")) {
				found = true;
			}
		}
		assertTrue(found);
	}

}
