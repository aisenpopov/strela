package ru.strela.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import ru.strela.model.auth.Person;


public interface PersonServer extends UserDetailsService{

	Person getCurrentPerson();
	
}


