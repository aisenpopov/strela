package ru.strela.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.strela.model.Athlete;
import ru.strela.model.auth.Person;
import ru.strela.model.filter.AthleteFilter;
import ru.strela.model.filter.BaseFilter;
import ru.strela.model.filter.PermissionFilter;
import ru.strela.model.filter.PersonFilter;
import ru.strela.model.filter.payment.AthleteTariffFilter;
import ru.strela.model.payment.AthleteTariff;
import ru.strela.repository.AthleteRepository;
import ru.strela.repository.auth.PersonRepository;
import ru.strela.repository.spec.AthleteSpec;
import ru.strela.repository.spec.PersonSpec;
import ru.strela.service.PaymentService;
import ru.strela.service.PersonServer;
import ru.strela.service.PersonService;
import ru.strela.util.PageRequestBuilder;

import java.util.List;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;
    
    @Autowired
    private AthleteRepository athleteRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PersonServer personServer;

    @Override
    public void updateFilter(BaseFilter filter) {
        Person currentPerson = personServer.getCurrentPerson();
        if (currentPerson != null && !currentPerson.isRoot()) {
            Athlete athlete = findByPerson(currentPerson);
            if (athlete != null && athlete.getTeam() != null) {
                PermissionFilter permissionFilter = new PermissionFilter();
                permissionFilter.setTeam(athlete.getTeam());
                filter.setPermissionFilter(permissionFilter);
            }
        }
    }
    
    @Override
    public Person save(Person person) {
        return personRepository.save(person);
    }

	@Override
	public Person save(Person person, Athlete athlete) {		
		if(StringUtils.isBlank(person.getLogin())) {
			person.setLogin("id");
			person = save(person);
			person.setLogin(person.getLogin() + person.getId());
		}
		Person user = save(person);
		
		athlete.setPerson(user);
		save(athlete);
		
		return user;
	}
    
    @Override
    public void remove(Person person) {
    	Athlete athlete = findByPerson(person);
    	if(athlete != null) {
    		athleteRepository.delete(athlete);
    	}
        personRepository.delete(person);
    }

    @Override
    public Person findById(Person person) {
        return personRepository.findOne(person.getId());
    }
    
    @Override
    public Person findByEmail(String email) {
    	return personRepository.findByEmail(email);
    }

    @Override
    public Person findByLogin(Person person) {
        return personRepository.findByLogin(person.getLogin());
    }
    
    @Override
    public Page<Person> find(PersonFilter filter, int pageNumber, int pageSize) {
    	return personRepository.findAll(PersonSpec.filter(filter), PageRequestBuilder.build(filter, pageNumber, pageSize));
    }
    
    @Override
    public List<Person> find(PersonFilter filter) {
    	return personRepository.findAll(PersonSpec.filter(filter), PageRequestBuilder.getSort(filter));
    }
    
    @Override
    public List<Person> findHasNotAthlete(String query) {
    	return personRepository.findHasNotAthlete(query);
    }
    
    
    @Override
    public Athlete save(Athlete athlete) {
        return athleteRepository.save(athlete);
    }
    
    @Override
	public void remove(Athlete athlete) {
        AthleteTariffFilter filter = new AthleteTariffFilter();
        filter.setAthlete(athlete);
        for (AthleteTariff athleteTariff : paymentService.findAthleteTariffs(filter, false)) {
            paymentService.remove(athleteTariff);
        }

    	athleteRepository.delete(athlete);
	}

	@Override
    public Athlete findByPerson(Person person) {
        return athleteRepository.findByPerson(person);
    }

	@Override
	public Athlete findById(Athlete athlete) {
		return athleteRepository.findOne(athlete.getId());
	}

	@Override
	public Page<Athlete> findAthletes(AthleteFilter filter, int pageNumber, int pageSize) {
        updateFilter(filter);
		return athleteRepository.findAll(AthleteSpec.filter(filter), PageRequestBuilder.build(filter, pageNumber, pageSize));
	}

	@Override
	public List<Athlete> findAthletes(AthleteFilter filter) {
        updateFilter(filter);
		return athleteRepository.findAll(AthleteSpec.filter(filter), PageRequestBuilder.getSort(filter));
	}
    
}
