package se.raek.charset;

/**
 * Fallback decoder using ISO 8859-1.
 */
public final class Iso88591ByteDecoder implements NonAsciiByteDecoder {
	@Override
	public char decodeByte(final byte b) {
		return (char) Byte.toUnsignedInt(b);
	}
}
