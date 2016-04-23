package ru.strela.util.ajax;

import java.util.HashMap;
import java.util.Map;

public class JsonResponse {
	
	public static final String ERROR = "error";
	public static final String SUCCESS = "success";
	
	private String status = SUCCESS;
	private String redirect;
    private Map<String, Object> data;
     
    public String getStatus() {
    	return status;
    }
     
    public void setStatus(String status) {
    	this.status = status;
    }

    public String getRedirect() {
    	return redirect;
    }
     
    public void setRedirect(String redirect) {
    	this.redirect = redirect;
    }
    
    public Map<String, Object> getData() {
    	if(data == null) {
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
    	if(data == null) {
    		data = new HashMap<String, Object>();
    	}
    	return new JsonData(data);
    }
    
	public boolean isStatusError() {
		return ERROR.equals(getStatus());
	}
	
}
