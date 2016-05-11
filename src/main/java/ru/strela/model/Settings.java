package ru.strela.model;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "settings")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class Settings extends BaseEntity {

	private String email;
	private Integer accountDay;

    public Settings() {
    }

    public Settings(int id) {
       this.id = id;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getAccountDay() {
		return accountDay;
	}

	public void setAccountDay(Integer accountDay) {
		this.accountDay = accountDay;
	}
}
