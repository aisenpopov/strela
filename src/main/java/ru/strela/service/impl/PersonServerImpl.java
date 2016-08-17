package ru.strela.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ru.strela.auth.AuthPerson;
import ru.strela.model.auth.Person;
import ru.strela.service.PersonServer;
import ru.strela.service.PersonService;

@Service
public class PersonServerImpl implements PersonServer {
	
	@Autowired
	private PersonService personService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Person person = personService.findByLogin(new Person(username.trim()));

        if(person == null) throw new UsernameNotFoundException("User " + username + " not found");
        return new AuthPerson(person);
	}
	
	@Override
	public Person getCurrentPerson() {
		AuthPerson authPerson = getCurrentAuthPerson();
		if (authPerson != null) {
			return authPerson.getPerson();
		}
		return null;
	}

	@Override
	public AuthPerson getCurrentAuthPerson() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if((auth instanceof UsernamePasswordAuthenticationToken || auth instanceof RememberMeAuthenticationToken)
				&& auth.isAuthenticated()) {
			return ((AuthPerson)auth.getPrincipal());
		}
		return null;
	}
}
