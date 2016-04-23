package ru.strela.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import ru.strela.model.Athlete;
import ru.strela.model.auth.Person;

@Repository
public interface AthleteRepository extends JpaRepository<Athlete, Integer>, JpaSpecificationExecutor<Athlete> {
	
	public Athlete findByPerson(Person person);
	
}
