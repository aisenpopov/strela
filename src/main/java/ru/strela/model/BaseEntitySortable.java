package ru.strela.model;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntitySortable extends BaseEntityNamed implements IsSortable {

	protected Integer position;

	public Integer getPosition() {
		return position;
	}
	
	@Override
	public void setPosition(Integer position) {
		this.position = position;
	}
    
}
