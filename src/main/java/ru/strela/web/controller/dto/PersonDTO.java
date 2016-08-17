package ru.strela.web.controller.dto;

import ru.strela.model.auth.Person;

public class PersonDTO {

	private int id;
	private String login;
	private String password;
	private boolean disabled;
	private boolean admin;
	private boolean instructor;
	private String recoveryCode;

    public PersonDTO() {}

    public PersonDTO(Person person) {
		if (person != null) {
			id = person.getId();
			login = person.getLogin();
			password = person.getPassword();
			disabled = person.isDisabled();
			admin = person.isAdmin();
			instructor = person.isInstructor();
			recoveryCode = person.getRecoveryCode();
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public boolean isInstructor() {
		return instructor;
	}

	public void setInstructor(boolean instructor) {
		this.instructor = instructor;
	}

	public String getRecoveryCode() {
		return recoveryCode;
	}

	public void setRecoveryCode(String recoveryCode) {
		this.recoveryCode = recoveryCode;
	}
}
