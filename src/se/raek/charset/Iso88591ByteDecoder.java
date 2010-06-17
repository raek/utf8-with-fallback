package se.raek.charset;

public class Iso88591ByteDecoder implements NonAsciiByteDecoder {

	public char decodeByte(byte b) {
		return (char) ((int) b & 0xFF);
	}

}
