package ru.strela.util;

import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelBuilder extends ModelAndView {

	private Map<String, Object> model;
	
	public ModelBuilder() {
		super();
		model = super.getModel();
	}
	
	public ModelBuilder(String viewName) {
		super(viewName);
		model = super.getModel();
	}
	
	public ModelBuilder(ModelAndView model) {
		this(model.getModel());
	}
	
	public ModelBuilder(Model model) {
		this(model.asMap());
	}
	
	public ModelBuilder(Map<String, Object> map) {
		this.model = map;
	}
	
	@SuppressWarnings("unchecked")
	public ModelBuilder createCollection(String name) {
		List<Map<String, Object>> collection = (List<Map<String, Object>>)model.get(name);
		if(collection == null) {
			collection = new ArrayList<Map<String,Object>>();
			model.put(name, collection);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		collection.add(map);
		return new ModelBuilder(map);
	}
	
	public ModelBuilder put(String name, Object object) {
		model.put(name, object);
		return this;
	}
	
	@Override
	public Map<String, Object> getModel() {
		return model;
	}
	
	public void addTableColumn(String name, String field) {
		ModelBuilder model = this.createCollection("tableColumns");
		model.put("name", name);
		model.put("field", field);
	}
	
}
