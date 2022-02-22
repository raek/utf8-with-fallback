package se.raek.charset;

public final class Iso885915ByteDecoder implements NonAsciiByteDecoder {
	@Override
	public char decodeByte(final byte b) {
		final int i = Byte.toUnsignedInt(b);
		switch (i) {
			case 0xA4: return '\u20AC';
			case 0xA6: return '\u0160';
			case 0xA8: return '\u0161';
			case 0xB4: return '\u017D';
			case 0xB8: return '\u017E';
			case 0xBC: return '\u0152';
			case 0xBD: return '\u0153';
			case 0xBE: return '\u0178';
			default: return (char) i;
		}
	}
}
