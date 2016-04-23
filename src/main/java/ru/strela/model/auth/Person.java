package ru.strela.model.auth;

import javax.persistence.*;

import ru.strela.model.BaseEntity;

@Entity
@Table(name = "person")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class Person extends BaseEntity {

	private String login;
	private String password;
	private boolean disabled;
	private boolean admin;

    public Person() {}

    public Person(int id) {
    	setId(id);
    }

    public Person(String login) {
        this.login = login;
    }

    @Column(unique = true, nullable = false)
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isDisabled() {
		return disabled;
	}
	
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	public boolean isAdmin() {
		return admin;
	}
	
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

}
