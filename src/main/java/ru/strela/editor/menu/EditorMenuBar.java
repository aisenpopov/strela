package ru.strela.editor.menu;

import java.util.ArrayList;
import java.util.List;


public class EditorMenuBar {
	private List<MenuItem> items;
	
	public List<MenuItem> getItems() {
		if(items == null) {
			items = new ArrayList<MenuItem>();
		}
		return items;
	}
	
	public void setItems(List<MenuItem> items) {
		this.items = items;
	}
	
	public void addItem(MenuItem item) {
		getItems().add(item);
	}
	
}
