package ru.strela.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;
import ru.strela.model.auth.Person;
import ru.strela.service.PersonServer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class ExportServlet implements HttpRequestHandler {
	
	@Autowired
	private PersonServer personServer;
	
	@Autowired
	private ExportersList exportersList;
	
	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Person currentPerson = personServer.getCurrentPerson();

	   	if (currentPerson != null) {
	   	   	String[] components = request.getPathInfo().substring(1).split("/");
			if (components.length >= 1) {
				String type = components[0];

				AbstractExporter exporter = exportersList.getExporters().get(type);
				if (exporter != null) {
					response.setHeader("Content-Disposition", "attachment");
					exporter.export(request, response.getOutputStream());

					response.flushBuffer();
				}
	  		}
	   	}
	}
   
}