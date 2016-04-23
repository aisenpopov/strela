package ru.strela.service;

import java.util.List;

import org.springframework.data.domain.Page;

import ru.strela.model.Athlete;
import ru.strela.model.auth.Person;
import ru.strela.model.filter.AthleteFilter;
import ru.strela.model.filter.PersonFilter;

public interface PersonService {

    Person save(Person person);
    
    Person save(Person person, Athlete athlete);

    void remove(Person person);

    Person findById(Person person);
    
    Person findByEmail(String email);

    Person findByLogin(Person person);
    
    Page<Person> find(PersonFilter filter, int pageNumber, int pageSize);
    
	List<Person> find(PersonFilter filter);
	
	List<Person> findHasNotAthlete(String query);
    
    
    Athlete save(Athlete athlete);
    
    void remove(Athlete athlete);
    
    Athlete findById(Athlete athlete);
	
    Athlete findByPerson(Person person);
    
    Page<Athlete> findAthletes(AthleteFilter filter, int pageNumber, int pageSize);
    
    List<Athlete> findAthletes(AthleteFilter filter);

}
