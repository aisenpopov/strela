package ru.strela.util;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class DateUtils {
	
	private static DateFormatSymbols dateFormatSymbolsRu = new DateFormatSymbols() {

		private static final long serialVersionUID = 1L;

		@Override
        public String[] getMonths() {
            return new String[]{"Января", "Февраля", "Марта", "Апреля", "Мая", "Июня",
                "Июля", "Августа", "Сентября", "Октября", "Ноября", "Декабря"};
        }
        
    };
    
//	private static DateFormatSymbols dateFormatSymbolsLt = new DateFormatSymbols() {
//
//		private static final long serialVersionUID = 1L;
//
//		@Override
//        public String[] getMonths() {
//            return new String[]{"Sausis", "Vasaris", "Kovas", "Balandis", "Geguze", "Birzelis",
//                "Liepa", "Rugpjutis", "Rugsejis", "Spalis", "Lapkritis", "Gruodis"};
//        }
//        
//    };
    
    private static SimpleDateFormat formatRu = new SimpleDateFormat("dd MMMM", dateFormatSymbolsRu);
//    private static SimpleDateFormat formatLt = new SimpleDateFormat("dd MMMM", dateFormatSymbolsLt);
//    private static SimpleDateFormat formatEn = new SimpleDateFormat("dd MMMM", Locale.ENGLISH);
    
    public static String format(Date date) {
    	if(date != null) {
    		return formatRu.format(date);
    	}

    	return StringUtils.EMPTY;
    }
    
    
    private static SimpleDateFormat formatFullRu = new SimpleDateFormat("dd MMMM yyyy", dateFormatSymbolsRu);
//    private static SimpleDateFormat formatFullLt = new SimpleDateFormat("dd MMMM yyyy", dateFormatSymbolsLt);
//    private static SimpleDateFormat formatFullEn = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
    
    public static String formatFull(Date date) {
    	if(date != null) {
    		return formatFullRu.format(date);
    	}

    	return StringUtils.EMPTY;
    }
    
}
