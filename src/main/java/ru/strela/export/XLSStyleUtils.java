package ru.strela.export;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.awt.Color;


public class XLSStyleUtils {
	
	public static void setBorder(CellStyle style, BorderStyle borderStyle) {
		setBorder(style, borderStyle, true, true, true, true);
	}
	
	public static void setBorder(CellStyle style, BorderStyle borderStyle, boolean top, boolean right, boolean bottom, boolean left) {
		style.setBorderTop(BorderStyle.NONE);
		if (top) {
			style.setBorderTop(borderStyle);
		}
		
		style.setBorderRight(BorderStyle.NONE);
		if (right) {
			style.setBorderRight(borderStyle);
		}
		
		style.setBorderBottom(BorderStyle.NONE);
		if (bottom) {
			style.setBorderBottom(borderStyle);
		}
		
		style.setBorderLeft(BorderStyle.NONE);
		if (left) {
			style.setBorderLeft(borderStyle);
		}
	}
	
	public static void fillBackground(CellStyle style, int r, int g, int b) {
		((XSSFCellStyle)style).setFillForegroundColor(new XSSFColor(new Color(r, g, b)));
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	}
	
	public static void setFont(Workbook workbook, CellStyle style, int size) {
		setFont(workbook, style, null, size, null);
	}
	
	public static void setFont(Workbook workbook, CellStyle style, Boolean bold) {
		setFont(workbook, style, null, null, bold);
	}
	
	public static void setFont(Workbook workbook, CellStyle style, String name, Integer size, Boolean bold) {
		setFont(workbook, style, name, size, bold, HSSFColor.BLACK.index);
	}
	
	public static void setFont(Workbook workbook, CellStyle style, String name, Integer size, Boolean bold, short index) {
		setFont(workbook, style, name, size, bold, index, null);
	}
	
	public static void setFont(Workbook workbook, CellStyle style, String name, Integer size, Boolean bold, short index, Boolean italic) {
		Font font = workbook.createFont();
		if (size != null) font.setFontHeightInPoints(size.shortValue());
		if (name != null) font.setFontName(name);
		if (bold != null && bold) font.setBold(true);
		if (italic != null && italic) font.setItalic(true);
		font.setColor(index);
		style.setFont(font);
	}
	
}
