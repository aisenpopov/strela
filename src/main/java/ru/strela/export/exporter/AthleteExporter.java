package ru.strela.export.exporter;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import ru.strela.export.AbstractExporter;
import ru.strela.export.StylesFactory;
import ru.strela.export.XLSBuilder;
import ru.strela.export.filter.BaseExportFilter;
import ru.strela.model.Athlete;
import ru.strela.model.auth.Person;
import ru.strela.model.filter.AthleteFilter;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;

import java.util.Map;

public class AthleteExporter extends AbstractExporter {

    @Override
    protected void fillDocument(BaseExportFilter filter, Workbook workbook) {
        Sheet sheet = workbook.getSheetAt(0);
        Map<String, CellStyle> styles = StylesFactory.createStyles(workbook);

        XLSBuilder builder = new XLSBuilder(sheet, styles);

        builder.setStyle("cellTitle");
        builder.createCell(0, 0, filter.getFileName());

        builder.setStyle("cellHeader");
        // id.
        sheet.setColumnWidth(0, 5 * 256);
        builder.createCell(2, 0, "ID");
        // ФИО.
        sheet.setColumnWidth(1, 40 * 256);
        builder.createCell(2, 1, "ФИО");
        // Пользователь.
        sheet.setColumnWidth(2, 20 * 256);
        builder.createCell(2, 2, "Пользователь");
        // Инструктор.
        sheet.setColumnWidth(3, 5 * 256);
        builder.createCell(2, 3, "Инструктор");
        // Админ.
        sheet.setColumnWidth(4, 5 * 256);
        builder.createCell(2, 4, "Админ");
        // Заблокирован.
        sheet.setColumnWidth(5, 5 * 256);
        builder.createCell(2, 5, "Заблокирован");
        // Команда.
        sheet.setColumnWidth(6, 20 * 256);
        builder.createCell(2, 6, "Команда");
        // Регион регистрации.
        sheet.setColumnWidth(7, 40 * 256);
        builder.createCell(2, 7, "Регион регистрации");

        AthleteFilter athleteFilter = new AthleteFilter();
        athleteFilter.setQuery(filter.getQuery());
        athleteFilter.addOrder(new Order("id", OrderDirection.Desc));

        builder.setStyle("cell");
        Cell cell;
        int currentRow = 3;

        int pageNumber = 0;
        Page<Athlete> page;
        do {
            page = personService.findAthletes(athleteFilter, pageNumber++, 100);
            for (Athlete athlete : page) {
                Person person = athlete.getPerson();
                cell = builder.createCell(currentRow, 0);
                cell.setCellValue(athlete.getId());

                builder.createCell(currentRow, 1, athlete.getDisplayName());
                builder.createCell(currentRow, 2, person.getLogin());
                builder.createCell(currentRow, 3, person.isInstructor() ? "Да" : "Нет");
                builder.createCell(currentRow, 4, person.isAdmin() ? "Да" : "Нет");
                builder.createCell(currentRow, 5, person.isDisabled() ? "Да" : "Нет");
                builder.createCell(currentRow, 6, athlete.getTeam() != null ? athlete.getTeam().getName() : StringUtils.EMPTY);
                builder.createCell(currentRow, 7, athlete.getRegistrationRegion() != null ? athlete.getRegistrationRegion().getName() : StringUtils.EMPTY);

                currentRow++;
            }
        } while (page.hasNext());
    }
}
