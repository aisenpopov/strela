package ru.strela.export;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.HashMap;
import java.util.Map;

public class StylesFactory {
	public static Map<String, CellStyle> createStyles(Workbook workbook) {
		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();

		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		XLSStyleUtils.setFont(workbook, style, "Calibri", 10, false);

		CellStyle cell = workbook.createCellStyle();
		cell.cloneStyleFrom(style);
		XLSStyleUtils.setBorder(cell, BorderStyle.THIN);
		styles.put("cell", cell);

		CellStyle cellTitle = workbook.createCellStyle();
		cellTitle.cloneStyleFrom(style);
		cellTitle.setWrapText(false);
		cellTitle.setAlignment(HorizontalAlignment.LEFT);
		XLSStyleUtils.setFont(workbook, cellTitle, "Calibri", 12, true);
		styles.put("cellTitle", cellTitle);

		CellStyle cellHeader = workbook.createCellStyle();
		cellHeader.cloneStyleFrom(style);
		XLSStyleUtils.setFont(workbook, cellHeader, "Calibri", 10, true);
		XLSStyleUtils.setBorder(cellHeader, BorderStyle.MEDIUM);
		styles.put("cellHeader", cellHeader);
		
		CellStyle cellGray = workbook.createCellStyle();
		cellGray.cloneStyleFrom(cell);
		XLSStyleUtils.fillBackground(cellGray, 165, 165, 165);
		styles.put("cellGray", cellGray);
		
		CellStyle cellLightGray = workbook.createCellStyle();
		cellLightGray.cloneStyleFrom(cell);
		XLSStyleUtils.fillBackground(cellLightGray, 224, 224, 224);
		styles.put("cellLightGray", cellLightGray);
		
		CellStyle cellBlue = workbook.createCellStyle();
		cellBlue.cloneStyleFrom(cell);
		XLSStyleUtils.fillBackground(cellBlue, 67, 126, 221);
		styles.put("cellBlue", cellBlue);
		
		CellStyle cellPurple = workbook.createCellStyle();
		cellPurple.cloneStyleFrom(cell);
		XLSStyleUtils.fillBackground(cellPurple, 173, 163, 224);
		styles.put("cellPurple", cellPurple);

		DataFormat format = workbook.createDataFormat();
		CellStyle cellStyleCurrency = workbook.createCellStyle();
		cellStyleCurrency.cloneStyleFrom(cell);
		cellStyleCurrency.setDataFormat(format.getFormat("#,##0.00"));
		styles.put("cellStyleCurrency", cellStyleCurrency);
		
		CellStyle cellStyleCurrencyGray = workbook.createCellStyle();
		cellStyleCurrencyGray.cloneStyleFrom(cellStyleCurrency);
		XLSStyleUtils.fillBackground(cellStyleCurrencyGray, 165, 165, 165);
		styles.put("cellStyleCurrencyGray", cellStyleCurrencyGray);
		
		CellStyle cellStyleCurrencyLightGray = workbook.createCellStyle();
		cellStyleCurrencyLightGray.cloneStyleFrom(cellStyleCurrency);
		XLSStyleUtils.fillBackground(cellStyleCurrencyLightGray, 224, 224, 224);
		styles.put("cellStyleCurrencyLightGray", cellStyleCurrencyLightGray);
		
		CellStyle cellStyleCurrencyBlue = workbook.createCellStyle();
		cellStyleCurrencyBlue.cloneStyleFrom(cellStyleCurrency);
		XLSStyleUtils.fillBackground(cellStyleCurrencyBlue, 67, 126, 221);
		styles.put("cellStyleCurrencyBlue", cellStyleCurrencyBlue);
		
		return styles;
	}

}
