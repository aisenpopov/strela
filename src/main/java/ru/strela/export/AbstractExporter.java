package ru.strela.export;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import ru.strela.export.filter.AbstractExportFilter;
import ru.strela.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractExporter {

	@Autowired
	protected PersonService personService;
	
	protected static Logger logger = Logger.getLogger(AbstractExporter.class);
	
	public boolean export(HttpServletRequest request, OutputStream os) {
		return export(getExportDataAdapter(request), os);
	}

	public byte[] export(IExportDataAdapter dataAdapter) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if(export(dataAdapter, baos)) {
			return baos.toByteArray();
		}
		return null;
	}

	public boolean export(IExportDataAdapter dataAdapter, OutputStream os) {
		Map<String, Object> modal = new HashMap<String, Object>();
		String templateName = fillModel(dataAdapter, modal);
		
		try {
			InputStream templateStream = getClass().getResourceAsStream("/ru/strela/export/" + templateName + ".xlsx");
			Workbook workbook = new XSSFWorkbook(templateStream);
			fillTemplate(modal, workbook);
			workbook.write(os);
			return true;
		} catch (IOException e) {
			logger.error("Error export to excel", e);
		}
		
		return false;
	}

	public boolean export(AbstractExportFilter filter, OutputStream os) {
		try {
			InputStream templateStream = getClass().getResourceAsStream("/ru/strela/export/" + filter.getExporterName() + ".xlsx");
			Workbook workbook = new XSSFWorkbook(templateStream);
			fillDocument(filter, workbook);
			workbook.write(os);
			return true;
		} catch (IOException e) {
			logger.error("Error export to excel", e);
		}

		return false;
	}
	
	protected abstract IExportDataAdapter getExportDataAdapter(HttpServletRequest request);

	protected abstract String fillModel(IExportDataAdapter dataAdapter, Map<String, Object> model);

	protected abstract void fillTemplate(Map<String, Object> model, Workbook workbook);

	protected abstract void fillDocument(AbstractExportFilter filter, Workbook workbook);
	
}
