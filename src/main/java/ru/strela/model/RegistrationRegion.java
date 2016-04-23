package ru.strela.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "registration_region")
public class RegistrationRegion extends BaseEntity {

	private String name;

	public RegistrationRegion() {}
	
	public RegistrationRegion(int id) {
		this.id = id;
	}
	
	@Column(nullable=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
