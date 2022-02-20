package se.raek.charset;

import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;
import java.util.ArrayList;
import java.util.Iterator;

public final class Utf8WithFallbackCharsetProvider extends CharsetProvider {
	private static final ArrayList<Charset> providedCharsets;
	
	static {
		providedCharsets = new ArrayList<>();
		providedCharsets.add(new Utf8WithIso88591FallbackCharset());
		providedCharsets.add(new Utf8WithWindows1252FallbackCharset());
	}

	public Utf8WithFallbackCharsetProvider() {
	}

	@Override
	public Charset charsetForName(final String charsetName) {
		for (final Charset providedCharset : providedCharsets) {
			if (providedCharset.name().equalsIgnoreCase(charsetName)) {
				return providedCharset;
			}
			for (final String providedName : providedCharset.aliases()) {
				if (providedName.equalsIgnoreCase(charsetName)) {
					return providedCharset;
				}
			}
		}
		return null;
	}

	@Override
	public Iterator<Charset> charsets() {
		return providedCharsets.iterator();
	}
}
