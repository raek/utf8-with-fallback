package se.raek.charset;

import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;
import java.util.ArrayList;
import java.util.Iterator;

public final class Utf8WithFallbackCharsetProvider extends CharsetProvider {
	
	private static final ArrayList<Charset> providedCharsets;
	
	static {
		providedCharsets = new ArrayList<Charset>();
		providedCharsets.add(new Utf8WithIso88591FallbackCharset());
	}

	public Utf8WithFallbackCharsetProvider() {
	}

	@Override
	public Charset charsetForName(final String charsetName) {
		for (Charset providedCharset : providedCharsets) {
			if (providedCharset.name().equalsIgnoreCase(charsetName)) {
				return providedCharset;
			}
			for (String providedName : providedCharset.aliases()) {
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
