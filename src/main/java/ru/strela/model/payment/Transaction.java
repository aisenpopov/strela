package ru.strela.model.payment;

import ru.strela.model.BaseEntity;
import ru.strela.model.auth.Person;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Aisen on 08.07.2016.
 */
@Entity
@Table(name = "transaction", indexes = {
        @Index(name = "transaction_person", columnList="person_id")
})
public class Transaction extends BaseEntity {

    private Person person;
    private Date date;
    private Double amount;

    public Transaction() {}

    public Transaction(int id) {
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
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(nullable = false)
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
