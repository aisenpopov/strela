package ru.strela.export;

import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.HashMap;
import java.util.Map;

public class XLSBuilder {

	private Sheet sheet;
	
	private Map<String, CellStyle> styles;
	private String currentStyle;
	
	private Float currentHeightRow;
	
	private String fontName;
	private String fontStyle;
	private Integer fontSize;
	
	public XLSBuilder(Sheet sheet) {
		this(sheet, new HashMap<String, CellStyle>());
	}
	
	public XLSBuilder(Sheet sheet, Map<String, CellStyle> styles) {
		this.sheet = sheet;
		this.styles = styles;
	}
	
	public void setStyle(String currentStyle) {
		this.currentStyle = currentStyle;
	}
	
	public void clearStyle() {
		setStyle(null);
	}
	
	public void setHeightRow(Float heightRow) {
		this.currentHeightRow = heightRow;
	}
	
	public void clearHeightRow() {
		setHeightRow(null);
	}
	
	public void setFont(String fontName, String fontStyle, Integer fontSize) {
		this.fontName = fontName;
		this.fontStyle = fontStyle;
		this.fontSize = fontSize;
	}
	
	public void clearFont() {
		setFont(null, null, null);
	}
	
	public Row createRow(int num) {
		return createRow(num, null);
	}
	
	public Row createRow(int num, Float height) {
		Row row = sheet.getRow(num);
		if(row == null) {
			row = sheet.createRow(num);
		}
		if(height != null) {
			row.setHeightInPoints(height);
		}
		return row;
	}
	
	public Cell createCell(int row, int col) {
		return createCell(row, row, col, col, null);
	}
	
	public Cell createCell(int row, int col, String value) {
		return createCell(row, row, col, col, value);
	}
	
	public Cell createCell(Row row, int col) {
		return createCell(row, row, col, col, null);
	}
	
	public Cell createCell(Row rowFrom, Row rowTo, int colFrom, int colTo) {
		return createCell(rowFrom, rowTo, colFrom, colTo, null);
	}
	
	public Cell createCell(Row row, int col, String value) {
		return createCell(row, row, col, col, value);
	}
	
	public Cell createCell(Row rowFrom, Row rowTo, int colFrom, int colTo, String value) {
		return createCell(rowFrom.getRowNum(), rowTo.getRowNum(), colFrom, colTo, value);
	}
	
	public Cell createCell(int rowFrom, int rowTo, int colFrom, int colTo) {
		return createCell(rowFrom, rowTo, colFrom, colTo, null);
	}
	
	public Cell createCell(int rowFrom, int rowTo, int colFrom, int colTo, String value) {
		for (int r = rowFrom; r <= rowTo; r++) {
			Row row = sheet.getRow(r);
			if (row == null) {
				row = sheet.createRow(r);
				if (currentHeightRow != null) {
					row.setHeightInPoints(currentHeightRow);
				}
			}
			for (int c = colFrom; c <= colTo; c++) {
				Cell cell = row.getCell(c);
				if (cell == null) {
					cell = row.createCell(c);
				}
				if (currentStyle != null) {
					cell.setCellStyle(styles.get(currentStyle));
				}
			}
		}
		if (rowFrom != rowTo || colFrom != colTo) {
			sheet.addMergedRegion(new CellRangeAddress(rowFrom, rowTo, colFrom, colTo));
		}
		
		Cell cell = sheet.getRow(rowFrom).getCell(colFrom);
		if (value != null) {
			cell.setCellValue(value);
		}
		return cell;
	}
	
	public void setHeader(String title) {
		Header header = sheet.getHeader();
		
		StringBuffer buff = new StringBuffer();
		if(fontName != null && fontStyle != null) {
			buff.append(HSSFHeader.font("Arial", "Regular"));
		}
		if(fontSize != null) {
			buff.append(HSSFHeader.fontSize(fontSize.shortValue()));
		}
		buff.append(title);
		header.setCenter(buff.toString());
	} 
	
	public void setPrintArea(int rowFrom, int rowTo, int colFrom, int colTo) {
		Workbook workbook = sheet.getWorkbook();
		workbook.setPrintArea(workbook.getSheetIndex(sheet), colFrom, colTo, rowFrom, rowTo);
	}
}

