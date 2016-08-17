package ru.strela.util.ajax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonData {
	
	private Map<String, Object> data;
    
	public JsonData(Map<String, Object> data) {
		this.data = data;
	}
	
    public Map<String, Object> getData() {
		return data;
	}
    
    public void setData(Map<String, Object> data) {
		this.data = data;
	}
    
	public void put(String key, Object object) {
		data.put(key, object);
	}
	
	@SuppressWarnings("unchecked")
	public JsonData createCollection(String key) {
		List<Map<String, Object>> collection = (List<Map<String, Object>>)data.get(key);
		if (collection == null) {
			collection = new ArrayList<Map<String,Object>>();
			data.put(key, collection);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		collection.add(map);
	
		return new JsonData(map);
	}

	public JsonData addJsonData(String key) {
		Map<String, Object> map = new HashMap<>();
		data.put(key, map);

		return new JsonData(map);
	}

}
