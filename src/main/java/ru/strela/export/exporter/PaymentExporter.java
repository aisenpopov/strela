package ru.strela.export.exporter;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import ru.strela.export.AbstractExporter;
import ru.strela.export.StylesFactory;
import ru.strela.export.XLSBuilder;
import ru.strela.export.filter.BaseExportFilter;
import ru.strela.model.Athlete;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.model.filter.payment.PaymentFilter;
import ru.strela.model.payment.AthleteTariff;
import ru.strela.model.payment.Payment;
import ru.strela.service.PaymentService;
import ru.strela.util.DateUtils;

import java.util.Map;

public class PaymentExporter extends AbstractExporter {

    @Autowired
    private PaymentService paymentService;

    @Override
    protected void fillDocument(BaseExportFilter filter, Workbook workbook) {
        Sheet sheet = workbook.getSheetAt(0);
        Map<String, CellStyle> styles = StylesFactory.createStyles(workbook);

        XLSBuilder builder = new XLSBuilder(sheet, styles);

        builder.setStyle("cellTitle");
        builder.createCell(0, 0, filter.getFileName());

        builder.setStyle("cellHeader");
        // id
        sheet.setColumnWidth(0, 5 * 256);
        builder.createCell(2, 0, "ID");
        // Атлет
        sheet.setColumnWidth(1, 40 * 256);
        builder.createCell(2, 1, "Атлет");
        // Тариф
        sheet.setColumnWidth(2, 20 * 256);
        builder.createCell(2, 2, "Тариф");
        // Скидка
        sheet.setColumnWidth(3, 20 * 256);
        builder.createCell(2, 3, "Скидка");
        // Сумма
        sheet.setColumnWidth(4, 15 * 256);
        builder.createCell(2, 4, "Сумма");
        // Оператор
        sheet.setColumnWidth(5, 40 * 256);
        builder.createCell(2, 5, "Оператор");
        // Дата
        sheet.setColumnWidth(6, 20 * 256);
        builder.createCell(2, 6, "Дата");

        PaymentFilter paymentFilter = new PaymentFilter();
        paymentFilter.setQuery(filter.getQuery());
        paymentFilter.setPageSize(100);
        paymentFilter.addOrder(new Order("id", OrderDirection.Desc));

        builder.setStyle("cell");
        Cell cell;
        int currentRow = 3;

        int pageNumber = 1;
        Page<Payment> page;
        do {
            paymentFilter.setPageNumber(pageNumber++);
            page = paymentService.pagePayments(paymentFilter, true);
            for (Payment payment : page) {
                cell = builder.createCell(currentRow, 0);
                cell.setCellValue(payment.getId());

                AthleteTariff athleteTariff = payment.getAthleteTariff();
                builder.createCell(currentRow, 1, athleteTariff.getAthlete().getDisplayName());
                builder.createCell(currentRow, 2, athleteTariff.getTariff().getName());
                builder.createCell(currentRow, 3, athleteTariff.getCoupon() != null ? athleteTariff.getCoupon().getName() : StringUtils.EMPTY);

                cell = builder.createCell(currentRow, 4);
                cell.setCellValue(payment.getAmount());

                Athlete operatorAthlete = personService.findByPerson(payment.getOperator());
                builder.createCell(currentRow, 5, payment.getOperator().getLogin() + (operatorAthlete != null ? (", " + operatorAthlete.getDisplayName()) : StringUtils.EMPTY));
                builder.createCell(currentRow, 6, DateUtils.formatDayMonthYear(payment.getDate()));

                currentRow++;
            }
        } while (page.hasNext());
    }
}
