package ru.strela.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="team", indexes = {
		@Index(name = "team_city", columnList="city_id"),
		@Index(name = "team_chief_instructor", columnList="chief_instructor_id")
})
public class Team extends BaseEntity {

	private String name;
	private City city;
	private Athlete chiefInstructor;
	
	public Team() {}
	
	public Team(int id) {
		this.id = id;
	}

	@Column(nullable=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(targetEntity=City.class, fetch=FetchType.LAZY)
	@JoinColumn(name="city_id", nullable=false)
	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	@ManyToOne(targetEntity=Athlete.class, fetch=FetchType.LAZY)
	@JoinColumn(name="chief_instructor_id", nullable=false)
	public Athlete getChiefInstructor() {
		return chiefInstructor;
	}

	public void setChiefInstructor(Athlete chiefInstructor) {
		this.chiefInstructor = chiefInstructor;
	}
	
}
