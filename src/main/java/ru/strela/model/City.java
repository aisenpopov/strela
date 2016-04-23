package ru.strela.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="city", indexes = {
		@Index(name = "city_country", columnList="country_id")
})
public class City extends BaseEntity {

	private String name;
	private Country country;
	
	public City() {}
	
	public City(int id) {
		this.id = id;
	}
	
	@Column(nullable=false)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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
