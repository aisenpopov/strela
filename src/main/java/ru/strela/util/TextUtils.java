package ru.strela.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import org.apache.commons.lang.StringUtils;

public class TextUtils {

	private static DecimalFormat format;
	
	static {
		format = (DecimalFormat)NumberFormat.getNumberInstance();
		format.setMinimumFractionDigits(2);
		format.setMaximumFractionDigits(2);
		
		DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(' ');
		symbols.setDecimalSeparator('.');
		format.setDecimalFormatSymbols(symbols);
	}
	
	public static String cutText(String text, int limit) {
		if (text.length() > limit) {
			limit -= 1;
			int idx = limit;
			char c = text.charAt(idx);
			while ((!Character.isSpaceChar(c) || c == ',') && idx > 0) {
				idx--;
				c = text.charAt(idx);
			}
			if (idx == 0) {
				return text.substring(0, limit) + ((char)0x2026);
			}
			return text.substring(0, idx).trim() + ((char)0x2026);
			
		}
		return text;
	}
	
	public static String priceNumber(Double price) {
		if (price == null) price = 0.0;
		
		return format.format(price).replace(".00", "");
	}
	
	public static String price(Double price) {
		if (price == null) return "По запросу";
		
		return priceNumber(price);
	}
	
	public static String fillMeta(String source, String alt, String template){
		if (StringUtils.isNotBlank(source)) 
			return source;
		
		return template.replaceAll("name", alt);
	}
	
	public static int getIntValue(String s) {
		try {
			return Integer.parseInt(s);
		} catch(NumberFormatException e) {
			return 0;
		}
	}
	
}
