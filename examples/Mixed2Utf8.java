import java.io.*;
import java.nio.charset.StandardCharsets;

/*
 * Example usage:
 *   java Mixed2Utf8 < mixed_encodings.txt
 * Output (in UTF-8):
 *   Thís lîñe ìs ïn UTF-8
 *   Thís lîñe ìs ïn ISO-8859-1
 */

public class Mixed2Utf8 {
	public static void main(String[] args) throws IOException {
		try (InputStreamReader reader = new InputStreamReader(System.in, "X-UTF-8_with_ISO-8859-1_fallback")) {
			OutputStreamWriter writer = new OutputStreamWriter(System.out, StandardCharsets.UTF_8);

			while (true) {
				int c = reader.read();

				if (c == -1) {
					break;
				}

				writer.write(c);
			}

			writer.flush();
		}
	}
}
