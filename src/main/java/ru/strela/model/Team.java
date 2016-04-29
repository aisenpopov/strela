package ru.strela.model;

import javax.persistence.*;

@Entity
@Table(name="team", indexes = {
		@Index(name = "team_city", columnList="city_id"),
		@Index(name = "team_chief_instructor", columnList="chief_instructor_id")
})
public class Team extends BaseEntityNamed {

	private City city;
	private Athlete chiefInstructor;
	
	public Team() {}
	
	public Team(int id) {
		this.id = id;
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
	@JoinColumn(name="chief_instructor_id")
	public Athlete getChiefInstructor() {
		return chiefInstructor;
	}

	public void setChiefInstructor(Athlete chiefInstructor) {
		this.chiefInstructor = chiefInstructor;
	}
	
}
