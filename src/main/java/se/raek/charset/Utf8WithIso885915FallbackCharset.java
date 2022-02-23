package se.raek.charset;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * A charset that tries to decode in UTF-8 and falls back to ISO 8859-15 for invalid UTF-8 byte sequences.
 * Encoding is not supported.
 * The canonical name is: X-UTF-8_with_ISO-8859-15_fallback
 */
public final class Utf8WithIso885915FallbackCharset extends Charset {
	private static final NonAsciiByteDecoder BYTE_DECODER = new Iso885915ByteDecoder();
	private static final String CANONICAL_NAME = "X-UTF-8_with_ISO-8859-15_fallback";
	private static final String[] ALIASES = {"X-UTF-8_with_ISO-LATIN-9_fallback"};
	private static final String DISPLAY_NAME = "UTF-8 with ISO-8859-15 fallback";

	public Utf8WithIso885915FallbackCharset() {
		super(CANONICAL_NAME, ALIASES);
	}
	
	@Override
	public boolean canEncode() {
		return false;
	}

	@Override
	public boolean contains(final Charset cs) {
		return StandardCharsets.UTF_8.contains(cs);
	}

	@Override
	public String displayName()	{
		return DISPLAY_NAME;
	}

	@Override
	public String displayName(final Locale locale) {
		return DISPLAY_NAME;
	}	

	@Override
	public CharsetDecoder newDecoder() {
		return new Utf8WithFallbackCharsetDecoder(this, 1, 1, BYTE_DECODER);
	}

	@Override
	public CharsetEncoder newEncoder() {
		throw new UnsupportedOperationException();
	}
}
