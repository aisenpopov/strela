package ru.strela.model.payment;

import ru.strela.model.BaseEntity;
import ru.strela.model.auth.Person;

import javax.persistence.*;

/**
 * Created by Aisen on 08.07.2016.
 */
@Entity
@Table(name = "person_account", indexes = {
        @Index(name = "person_account_person", columnList="person_id")
})
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class PersonAccount extends BaseEntity {

    private Person person;
    private Double account;

    public PersonAccount() {}

    public PersonAccount(int id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = Person.class, fetch = FetchType.LAZY)
    @JoinColumn(name="person_id", nullable = false)
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Column(nullable = false)
    public Double getAccount() {
        return account;
    }

    public void setAccount(Double account) {
        this.account = account;
    }
}
