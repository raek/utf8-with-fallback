package se.raek.charset;

public final class Iso88591ByteDecoder implements NonAsciiByteDecoder {
	@Override
	public char decodeByte(final byte b) {
		return (char) Byte.toUnsignedInt(b);
	}
}
