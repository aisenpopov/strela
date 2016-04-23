package ru.strela.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

public class PagerUtils {

    public static String getPagerPath(HttpServletRequest request) {
        StringBuilder path = new StringBuilder();
        path.append(request.getRequestURI());
        path.append("?");
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            if( "page".equals(name)) continue;
            if( "size".equals(name)) continue;
            String[] values = request.getParameterValues(name);
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                path.append(name);
                path.append("=");
                path.append(value);
                path.append("&");
            }
        }
        return path.toString();
    }

}
