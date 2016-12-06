package ru.strela.model.payment;

import ru.strela.model.Athlete;
import ru.strela.model.BaseEntity;
import ru.strela.model.Gym;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "payment_status", indexes = {
        @Index(name = "payment_status_athlete", columnList="athlete_id"),
        @Index(name = "payment_status_gym", columnList="gym_id")
})
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class PaymentStatus extends BaseEntity {

    private Athlete athlete;
    private Gym gym;
    private Date payedTill;

    public PaymentStatus() {}

    public PaymentStatus(int id) {
        this.id = id;
    }

    @ManyToOne(targetEntity=Athlete.class, fetch= FetchType.LAZY)
    @JoinColumn(name="athlete_id", nullable = false)
    public Athlete getAthlete() {
        return athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    @ManyToOne(targetEntity=Gym.class, fetch= FetchType.LAZY)
    @JoinColumn(name="gym_id", nullable = false)
    public Gym getGym() {
        return gym;
    }

    public void setGym(Gym gym) {
        this.gym = gym;
    }

    public Date getPayedTill() {
        return payedTill;
    }

    public void setPayedTill(Date payedTill) {
        this.payedTill = payedTill;
    }
}
