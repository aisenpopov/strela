package ru.strela.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "registration_region")
public class RegistrationRegion extends BaseEntityNamed {

	public RegistrationRegion() {}
	
	public RegistrationRegion(int id) {
		this.id = id;
	}
	
}
