package ru.strela.util;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	private static DateFormatSymbols dateFormatSymbols = new DateFormatSymbols() {

		private static final long serialVersionUID = 1L;

		@Override
        public String[] getMonths() {
            return new String[]{"Января", "Февраля", "Марта", "Апреля", "Мая", "Июня",
                "Июля", "Августа", "Сентября", "Октября", "Ноября", "Декабря"};
        }
        
    };
    
    private static SimpleDateFormat formatDayMonth = new SimpleDateFormat("dd MMMM", dateFormatSymbols);

    public static String formatDayMonth(Date date) {
    	if (date != null) {
    		return formatDayMonth.format(date);
    	}

    	return StringUtils.EMPTY;
    }

    private static SimpleDateFormat formatDayMonthYear = new SimpleDateFormat("dd MMMM yyyy", dateFormatSymbols);

    public static String formatDayMonthYear(Date date) {
    	if (date != null) {
    		return formatDayMonthYear.format(date);
    	}

    	return StringUtils.EMPTY;
    }

	private static SimpleDateFormat formatFull = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

	public static String formatFull(Date date) {
		if (date != null) {
			return formatFull.format(date);
		}

		return StringUtils.EMPTY;
	}

	private  static SimpleDateFormat formatDDMMYYYY = new SimpleDateFormat("dd.MM.yyyy");

	public static String formatDDMMYYYY(Date date) {
		if (date != null) {
			return formatDDMMYYYY.format(date);
		}

		return StringUtils.EMPTY;
	}

}
