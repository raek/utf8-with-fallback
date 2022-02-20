package se.raek.charset;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Locale;

public final class Utf8WithIso88591FallbackCharset extends Charset {
	
	private static final NonAsciiByteDecoder byteDecoder = new Iso88591ByteDecoder();
	private static final String canonicalName = "X-UTF-8_with_ISO-8859-1_fallback";
	private static final String[] aliases = {"X-UTF-8_with_ISO-LATIN-1_fallback"};
	private static final String displaylName = "UTF-8 with ISO-8859-1 fallback";
	
	private static final Charset utf8Charset = Charset.forName("UTF-8");

	public Utf8WithIso88591FallbackCharset() {
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
