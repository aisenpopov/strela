package ru.strela.export;

import java.util.Map;

public class ExportersList {
	
	private Map<String, AbstractExporter> exporters;
	
	public Map<String, AbstractExporter> getExporters() {
		return exporters;
	}
	
	public void setExporters(Map<String, AbstractExporter> exporters) {
		this.exporters = exporters;
	}
	
}
