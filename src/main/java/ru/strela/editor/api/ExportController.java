package ru.strela.editor.api;

import org.apache.commons.lang.CharEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.strela.export.AbstractExporter;
import ru.strela.export.ExportersList;
import ru.strela.export.filter.BaseExportFilter;
import ru.strela.model.auth.Person;
import ru.strela.service.PersonServer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Controller
@RequestMapping("/account/export")
public class ExportController {

    @Autowired
    private PersonServer personServer;

    @Autowired
    private ExportersList exportersList;

    @RequestMapping(value = "/payment", method = RequestMethod.GET)
    public void exportPayment(HttpServletResponse response, BaseExportFilter filter) throws IOException {
        export(response, filter);
    }

    @RequestMapping(value = "/athlete", method = RequestMethod.GET)
    public void exportAthlete(HttpServletResponse response, BaseExportFilter filter) throws IOException {
        export(response, filter);
    }

    private void export(HttpServletResponse response, BaseExportFilter filter) throws IOException {
        Person currentPerson = personServer.getCurrentPerson();

        if (currentPerson != null) {
            AbstractExporter exporter = exportersList.getExporters().get(filter.getExporterName());
            if (exporter != null) {
                String name = URLEncoder.encode(filter.getFileName() + "." + filter.getFileExtension(), CharEncoding.UTF_8).replace('+', ' ');
                response.setHeader("Content-disposition", "attachment; filename*=utf-8''" + name + ";");
                exporter.export(filter, response.getOutputStream());

                response.flushBuffer();
            }
        }
    }

}
