package se.raek.charset;

public final class Windows1252ByteDecoder implements NonAsciiByteDecoder {
	private final char[] controlCharReplacements = {
			'\u20AC', '\u0081', '\u201A', '\u0192', '\u201E', '\u2026', '\u2020', '\u2021',
			'\u02C6', '\u2030', '\u0160', '\u2039', '\u0152', '\u008D', '\u017D', '\u008F',
			'\u0090', '\u2018', '\u2019', '\u201C', '\u201D', '\u2022', '\u2013', '\u2014',
			'\u02DC', '\u2122', '\u0161', '\u203A', '\u0153', '\u009D', '\u017E', '\u0178'
	};

	@Override
	public char decodeByte(final byte b) {
		final int i = Byte.toUnsignedInt(b);
		if (i < 0x80 || i >= 0xA0) {
			return (char) i;
		}
		return controlCharReplacements[i - 0x80];
	}
}
