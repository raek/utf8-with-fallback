import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/*
 * Example usage:
 *   java Mixed2Utf8 < mixed_encodings.txt
 * Output (in UTF-8):
 *   Thís lîñe ìs ïn UTF-8
 *   Thís lîñe ìs ïn ISO-8859-1
 */

public class Mixed2Utf8 {
	
	public static void main(String[] args) {
		try {			
			BufferedReader reader =
				new BufferedReader(new InputStreamReader(System.in, "X-UTF-8_with_ISO-8859-1_fallback"));
			BufferedWriter writer =
				new BufferedWriter(new OutputStreamWriter(System.out, "UTF-8"));
			
			while (true) {
				int c = reader.read();
				
				if (c == -1)
					break;
				
				writer.write(c);
			}
			
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
