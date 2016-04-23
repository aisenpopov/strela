package ru.strela.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntityNamed extends BaseEntity {

	protected boolean visible;
	protected String name;

	@Column(columnDefinition="BOOLEAN DEFAULT FALSE")
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}
