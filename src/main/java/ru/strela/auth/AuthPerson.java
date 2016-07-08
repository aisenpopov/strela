package ru.strela.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.strela.model.auth.Person;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AuthPerson implements UserDetails {
	
	private static final long serialVersionUID = -1271299342523655379L;

	private Person person;
	
	public AuthPerson(Person person) {
		this.person = person;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		if (person.isInstructor()) {
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_INSTRUCTOR"));
		}
		if (person.isAdmin()) {
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
		return grantedAuthorities;
	}

	@Override
	public String getPassword() {
		return person.getPassword();
	}

	@Override
	public String getUsername() {
		return person.getLogin();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return !person.isDisabled();
	}
	
	public Person getPerson() {
		return person;
	}

}
