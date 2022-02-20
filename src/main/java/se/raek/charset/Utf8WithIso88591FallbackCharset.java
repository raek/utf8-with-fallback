package se.raek.charset;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public final class Utf8WithIso88591FallbackCharset extends Charset {
	private static final NonAsciiByteDecoder BYTE_DECODER = new Iso88591ByteDecoder();
	private static final String CANONICAL_NAME = "X-UTF-8_with_ISO-8859-1_fallback";
	private static final String[] ALIASES = {"X-UTF-8_with_ISO-LATIN-1_fallback"};
	private static final String DISPLAY_NAME = "UTF-8 with ISO-8859-1 fallback";
	
	public Utf8WithIso88591FallbackCharset() {
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
