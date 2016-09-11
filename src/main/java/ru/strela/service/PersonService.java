package ru.strela.service;

import org.springframework.data.domain.Page;
import ru.strela.model.Athlete;
import ru.strela.model.auth.Person;
import ru.strela.model.filter.AthleteFilter;
import ru.strela.model.filter.BaseFilter;
import ru.strela.model.filter.PersonFilter;
import ru.strela.util.validate.IValidateResult;

import java.util.List;

public interface PersonService {

    void updateFilter(BaseFilter filter);


    Person save(Person person);
    
    Person save(Person person, Athlete athlete);

    void remove(Person person);

    Person findById(Person person);
    
    Person findByEmail(String email);

    Person findByLogin(Person person);

    Person findLastPerson();

    Page<Person> find(PersonFilter filter, int pageNumber, int pageSize);
    
	List<Person> find(PersonFilter filter);
	
	List<Person> findHasNotAthlete(String query);
    
    
    Athlete save(Athlete athlete);
    
    void remove(Athlete athlete);
    
    Athlete findById(Athlete athlete);

    String checkRemove(Athlete athlete);

    Athlete findByPerson(Person person);
    
    Page<Athlete> findAthletes(AthleteFilter filter, int pageNumber, int pageSize);
    
    List<Athlete> findAthletes(AthleteFilter filter);


    Athlete saveAthlete(Athlete athlete);

    boolean validateAthlete(Athlete athlete, IValidateResult validateResult);

    void initNewAthlete(Athlete athlete);

}
