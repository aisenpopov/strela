package ru.strela.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class AutocompleteHelper {
	
	private StringBuffer multipleValue = new StringBuffer();
	
	public void clearValue() {
		multipleValue = new StringBuffer();
	}
	
	public void addValue(int id) {
		addValue(String.valueOf(id));
	}
	
	public void addValue(String id) {
		multipleValue.append(id).append(",");
	}
	
	public String getValue() {
		return multipleValue.toString();
	}
	
	public static List<Integer> parseMultipleValue(String value) {
		List<Integer> ids = new ArrayList<Integer>();
		for(String id : value.split(",")) {
			if(StringUtils.isNotBlank(id)) {
				try {
					ids.add(Integer.parseInt(id));
				} catch(NumberFormatException e) {}
			}
		}
		return ids;
	}
	
	public static List<String> parseMultipleStringValue(String value) {
		List<String> ids = new ArrayList<String>();
		for(String id : value.split(",")) {
			ids.add(id);
		}
		return ids;
	}
	
}
