package ru.strela.editor.menu;

import java.util.ArrayList;
import java.util.List;

public class MenuItem {
	
	private String caption;
	private String href;
	private String icon;
	private List<MenuItem> items;
	private int level;
	
	public String getCaption() {
		return caption;
	}
	
	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getHref() {
		return href;
	}
	
	public void setHref(String href) {
		this.href = href;
	}
	
	public String getIcon() {
		return icon;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
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
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
}
