package se.raek.charset;

import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;
import java.util.*;

public final class Utf8WithFallbackCharsetProvider extends CharsetProvider {
	private static final List<Charset> PROVIDED_CHARSETS = Arrays.asList(
			new Utf8WithIso88591FallbackCharset(),
			new Utf8WithIso885915FallbackCharset(),
			new Utf8WithWindows1252FallbackCharset()
	);

	private static final Map<String, Charset> PROVIDED_CHARSET_MAP = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	static {
		for (final Charset providedCharset : PROVIDED_CHARSETS) {
			PROVIDED_CHARSET_MAP.putIfAbsent(providedCharset.name(), providedCharset);
			for (final String alias : providedCharset.aliases()) {
				PROVIDED_CHARSET_MAP.putIfAbsent(alias, providedCharset);
			}
		}
	}

	@Override
	public Charset charsetForName(final String charsetName) {
		return PROVIDED_CHARSET_MAP.get(charsetName);
	}

	@Override
	public Iterator<Charset> charsets() {
		return PROVIDED_CHARSETS.iterator();
	}
}
