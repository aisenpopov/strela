package ru.strela.util;

import java.util.HashMap;
import java.util.Map;

public class TranslitHelper {

	public static final Map<Character, String> RUS_TO_LAT;
	
	static {
		RUS_TO_LAT = new HashMap<Character, String>();
		RUS_TO_LAT.put('а', "a");
		RUS_TO_LAT.put('б', "b");
		RUS_TO_LAT.put('в', "v");
		RUS_TO_LAT.put('г', "g");
		RUS_TO_LAT.put('д', "d");
		RUS_TO_LAT.put('е', "e");
		RUS_TO_LAT.put('ё', "e");
		RUS_TO_LAT.put('ж', "zh");
		RUS_TO_LAT.put('з', "z");
		RUS_TO_LAT.put('и', "i");
		RUS_TO_LAT.put('й', "j");
		RUS_TO_LAT.put('к', "k");
		RUS_TO_LAT.put('л', "l");
		RUS_TO_LAT.put('м', "m");
		RUS_TO_LAT.put('н', "n");
		RUS_TO_LAT.put('о', "o");
		RUS_TO_LAT.put('п', "p");
		RUS_TO_LAT.put('р', "r");
		RUS_TO_LAT.put('с', "s");
		RUS_TO_LAT.put('т', "t");
		RUS_TO_LAT.put('у', "u");
		RUS_TO_LAT.put('ф', "f");
		RUS_TO_LAT.put('х', "h");
		RUS_TO_LAT.put('ц', "c");
		RUS_TO_LAT.put('ч', "ch");
		RUS_TO_LAT.put('ш', "sh");
		RUS_TO_LAT.put('щ', "sh");
		RUS_TO_LAT.put('ъ', "");
		RUS_TO_LAT.put('ь', "");
		RUS_TO_LAT.put('ы', "y");
		RUS_TO_LAT.put('э', "je");
		RUS_TO_LAT.put('ю', "ju");
		RUS_TO_LAT.put('я', "ja");
		RUS_TO_LAT.put('&', "and");
		RUS_TO_LAT.put('/', "_");
		RUS_TO_LAT.put('"', "");
		RUS_TO_LAT.put('\'', "'");
		RUS_TO_LAT.put('$', "dollar");
		RUS_TO_LAT.put('~', "-");
		RUS_TO_LAT.put(' ', "-");
		RUS_TO_LAT.put('!', "exclamation");
		RUS_TO_LAT.put('#', "hash");
		RUS_TO_LAT.put('%', "percent");
		RUS_TO_LAT.put('?', "question");
		RUS_TO_LAT.put('№', "number");
		RUS_TO_LAT.put(':', "");
		RUS_TO_LAT.put('.', "");
		RUS_TO_LAT.put(',', "");
		RUS_TO_LAT.put('´', "");
		RUS_TO_LAT.put('`', "");
		RUS_TO_LAT.put('’', "");
		RUS_TO_LAT.put('º', "");
		RUS_TO_LAT.put('(', "");
		RUS_TO_LAT.put(')', "");
		RUS_TO_LAT.put('[', "");
		RUS_TO_LAT.put(']', "");
		RUS_TO_LAT.put('+', "");
		RUS_TO_LAT.put('ó', "o");
		RUS_TO_LAT.put('é', "e");
		RUS_TO_LAT.put('á', "a");
		RUS_TO_LAT.put('í', "i");
		RUS_TO_LAT.put('ú', "u");
	}
	
	public static String translit(String text) {
		StringBuffer buf = new StringBuffer();
		if (text != null) {
			for (int i = 0; i < text.length(); i++) {
				char ch = Character.toLowerCase(text.charAt(i));
				String rus = RUS_TO_LAT.get(ch);
				if (rus != null) {
					buf.append(rus);
				} else {
					buf.append(ch);
				}
			}
		}
		
		return buf.toString();
	}
}
