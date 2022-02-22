package se.raek.charset;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public final class Utf8WithWindows1252FallbackCharset extends Charset {
	private static final NonAsciiByteDecoder BYTE_DECODER = new Windows1252ByteDecoder();
	private static final String CANONICAL_NAME = "X-UTF-8_with_windows-1252_fallback";
	private static final String[] ALIASES = {"X-UTF-8_with_cp1252_fallback"};
	private static final String DISPLAY_NAME = "UTF-8 with windows-1252 fallback";

	public Utf8WithWindows1252FallbackCharset() {
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
