package test.test;

import java.util.List;

public class RecognizeManager {
	
	public static final int OTSU_BIN = 1;
	public static final int SAUVOLA_BIN = 2;
	public static final int SIMPLE_BIN = 3;
	
	public void recognize(List<String> langs, int binarizationType, int quality) throws RecognizerExeption {
			Recognizer2000 rec = new Recognizer2000();
			String out = rec.recognize("1232.bmp", getCorrectLangs(langs), binarizationType, quality);
			System.err.println(out);
		
	}

	private String getCorrectLangs(List<String> langs) {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (String s : langs) {
			if (first) {
				result.append(s);
				first = false;
			} else {
				result.append("+");
				result.append(s);
			}

		}
		return result.toString();
	}
}
