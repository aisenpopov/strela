package ru.strela.util.ajax;

import java.util.HashMap;
import java.util.Map;

public class JsonResponse {
	
	public static final String ERROR = "error";
	public static final String SUCCESS = "success";
	
	private String status = SUCCESS;
	private String redirect;
    private Map<String, Object> data;

	private Map<String, String> fieldsMessages;

    public String getStatus() {
    	return status;
    }
     
    public void setStatus(String status) {
    	this.status = status;
    }

	public void setErrorStatus() {
		this.status = ERROR;
	}

	public void setSuccessStatus() {
		this.status = SUCCESS;
	}

    public String getRedirect() {
    	return redirect;
    }
     
    public void setRedirect(String redirect) {
    	this.redirect = redirect;
    }
    
    public Map<String, Object> getData() {
    	if (data == null) {
    		data = new HashMap<String, Object>();
    	}
		return data;
	}
    
    public void setData(Map<String, Object> data) {
		this.data = data;
	}
    
    public void addData(String key, Object value) {
    	getData().put(key, value);
    }
    
    public JsonData createJsonData() {
    	if (data == null) {
    		data = new HashMap<String, Object>();
    	}
    	return new JsonData(data);
    }
    
	public boolean isStatusError() {
		return ERROR.equals(getStatus());
	}

	public Map<String, String> getFieldsMessages() {
		if (fieldsMessages == null) {
			fieldsMessages = new HashMap<>();
		}

		return fieldsMessages;
	}

	public void setFieldsMessages(Map<String, String> fieldsMessages) {
		this.fieldsMessages = fieldsMessages;
	}

	public void addFieldMessage(String field, String message) {
		setErrorStatus();
		getFieldsMessages().put(field, message);
	}
}
