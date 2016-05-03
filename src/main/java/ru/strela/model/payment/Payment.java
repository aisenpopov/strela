package ru.strela.model.payment;

import ru.strela.model.BaseEntity;
import ru.strela.model.auth.Person;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Aisen on 30.04.2016.
 */

@Entity
@Table(name = "payment", indexes = {
        @Index(name = "payment_athlete_tariff", columnList="athlete_tariff_id"),
        @Index(name = "payment_person", columnList="operator_id")
})
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class Payment extends BaseEntity {

    private AthleteTariff athleteTariff;
    private Double amount;
    private Date date;
    private Person operator;

    public Payment() {
    }

    public Payment(int id) {
        this.id = id;
    }

    @ManyToOne(targetEntity=AthleteTariff.class, fetch= FetchType.LAZY)
    @JoinColumn(name="athlete_tariff_id", nullable=false)
    public AthleteTariff getAthleteTariff() {
        return athleteTariff;
    }

    public void setAthleteTariff(AthleteTariff athleteTariff) {
        this.athleteTariff = athleteTariff;
    }

    @Column(nullable = false)
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Column(nullable = false)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @ManyToOne(targetEntity=Person.class, fetch= FetchType.LAZY)
    @JoinColumn(name="operator_id", nullable=false)
    public Person getOperator() {
        return operator;
    }

    public void setOperator(Person operator) {
        this.operator = operator;
    }
}
