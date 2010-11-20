package se.raek.charset;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Locale;

public final class Utf8WithWindows1252FallbackCharset extends Charset {
	
	private static final NonAsciiByteDecoder byteDecoder = new Windows1252ByteDecoder();
	private static final String canonicalName = "X-UTF-8_with_windows-1252_fallback";
	private static final String[] aliases = {"X-UTF-8_with_CP-1252_fallback"};
	private static final String displaylName = "UTF-8 with windows-1252 fallback";
	
	private static final Charset utf8Charset = Charset.forName("UTF-8");

	public Utf8WithWindows1252FallbackCharset() {
		super(canonicalName, aliases);
	}
	
	@Override
	public boolean canEncode() {
		return false;
	}

	@Override
	public boolean contains(final Charset cs) {
		return utf8Charset.contains(cs);
	}

	@Override
	public String displayName()	{
		return displaylName;
	}

	@Override
	public String displayName(final Locale locale) {
		return displaylName;
	}	

	@Override
	public CharsetDecoder newDecoder() {
		return new Utf8WithFallbackCharsetDecoder(this, 1, 1, byteDecoder);
	}

	@Override
	public CharsetEncoder newEncoder() {
		throw new UnsupportedOperationException();
	}

}
