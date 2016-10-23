package ru.strela.export;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import ru.strela.export.filter.BaseExportFilter;
import ru.strela.service.PersonService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractExporter {

	@Autowired
	protected PersonService personService;
	
	protected static Logger logger = Logger.getLogger(AbstractExporter.class);
	
	public boolean export(BaseExportFilter filter, OutputStream os) {
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
	
	protected abstract void fillDocument(BaseExportFilter filter, Workbook workbook);
	
}
