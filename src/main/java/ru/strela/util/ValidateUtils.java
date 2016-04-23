package ru.strela.util;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class ValidateUtils {
	
	public static final String REQUIRED_ERROR_RU = "Необходимо заполнить поле";
	public static final String REQUIRED_ERROR_EN = "Field required";
	
	public static final String[] REQUIRED_RU = {"lastNameRus", "firstNameRus", "secondNameRus", "organisationRus", "positionRus", 
											"lastName", "firstName", "phone", "email", "country"};
	public static final String[] REQUIRED_EN = { "lastName", "firstName", "organisation", "position", "phone", "email", "country"};
	
	protected final static int MIN_PASSWORD_LENGTH = 6;
	
	private static final Pattern emailCheckPattern = Pattern.compile(
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z]{2,}){1}$)",
            Pattern.CASE_INSENSITIVE);
	
	protected static final Pattern phoneCheckPattern = Pattern.compile(
            "^[0-9-()+]+$",
           Pattern.CASE_INSENSITIVE);
	
	public static String checkField(String field) {
		if(StringUtils.isBlank(field)) {
			return REQUIRED_ERROR_RU;
		}
        return null;
    }
	
	public static String checkEmail(String email) {
		if(StringUtils.isBlank(email)) {
			return REQUIRED_ERROR_RU;
		}
        if(!emailCheckPattern.matcher(email).matches()) {
           return "Некорректный email";
        }
        return null;
    }
	
	public static String checkPhone(String phone) {
		if(StringUtils.isBlank(phone)) {
			return REQUIRED_ERROR_RU;
		}
        if(!phoneCheckPattern.matcher(phone).matches()) {
           return "Некорректный телефон";
        }
        return null;
    }
	
	public static String checkLogin(String login) {
		if(login == null || login.trim().isEmpty()) {
			return "Login can not be empty";
		} else if(login.length() < 3) {
			return "Login can not be shorter than 3 characters";
		} else if(login.length() > 20) {
			return "Login can not be longer than 20 characters";
		} 
		return null;
	}
	
	public static String checkPassword(String password, String login) {
		if (password.length() < MIN_PASSWORD_LENGTH) {
	        return "Пароль не может быть короче " + MIN_PASSWORD_LENGTH + " символов";
	    } else if (password.length() > 20) {
	        return "Пароль не может быть длиннее 20 символов";
	    } else if(password.equalsIgnoreCase(login)) {
	    	return "Пароль не должен совпадать с именем пользователя";
	    } else if(!password.matches("\\w*")) {
			return "Пароль может содержать только латинские буквы, цифры и символ нижнего подчеркивания";
		}
        
        return null;
    }
	
}
