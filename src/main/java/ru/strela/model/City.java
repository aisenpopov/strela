package ru.strela.model;

import javax.persistence.*;

@Entity
@Table(name="city", indexes = {
		@Index(name = "city_country", columnList="country_id")
})
public class City extends BaseEntityNamed {

	private Country country;
	
	public City() {}
	
	public City(int id) {
		this.id = id;
	}
	
	@ManyToOne(targetEntity=Country.class, fetch=FetchType.LAZY)
	@JoinColumn(name="country_id", nullable=false)
	public Country getCountry() {
		return country;
	}
	
	public void setCountry(Country country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return name;
	}
	
}
