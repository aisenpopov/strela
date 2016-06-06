package ru.strela.util.processor;

public abstract class AbstractProcessor {
	
	public static final String CENSOR_WORD = "&lt;цензура&gt;";
	abstract public String process( String text );
	
	public static String cutText(String text, int limit) {
		if (text != null && text.length() > limit) {
			limit -= 1;
			int idx = limit;
			char c = text.charAt(idx);
			while ((!Character.isSpaceChar(c) || c == ',') && idx > 0) {
				idx--;
				c = text.charAt(idx);
			}
			if (idx == 0)
				return text.substring(0, limit) + ((char) 0x2026);

			return text.substring(0, idx).trim() + ((char) 0x2026);
		}

		return text;
	}
	
	public static String escapeText(String str) {
		StringBuffer sb = new StringBuffer();
		escapeText(str, sb);
		return sb.toString();
	}

	public static void escapeText(String str, StringBuffer writer) {
		if (str == null) return;
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			// prevent escape entities
//            if (ch == '&') {
//                writer.append("&amp;");
//            } else 
			if (ch == '>') {
				writer.append("&gt;");
			} else if (ch == '<') {
				writer.append("&lt;");
			} else if (ch == '"') {
				writer.append("&quot;");

				// this is a nnobreakable space - if we write is as a regular UTF-8 character,
				// browsers ignore it. It seems to work only when encoded as an entity.
			} else if (ch == 0xa0) {
				writer.append("&#xA0;");
			} else if (ch < ' ' && ch != '\n' && ch != '\r' && ch != '\t') {
				writer.append(" ");
			} else {
				writer.append(ch);
			}
		}
	}
}
