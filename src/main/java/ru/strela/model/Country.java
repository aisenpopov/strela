package ru.strela.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="country")
public class Country extends BaseEntityNamed {
	
	public Country() {}
	
	public Country(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return name;
	}
	
}
