package se.raek.charset;

/**
 * Fallback single-byte decoder.
 */
public interface NonAsciiByteDecoder {
	/**
	 * Decodes one byte into one character.
	 */
	char decodeByte(byte b);
}
