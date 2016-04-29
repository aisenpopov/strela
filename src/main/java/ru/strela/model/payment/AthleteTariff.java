package ru.strela.model.payment;

import ru.strela.model.Athlete;
import ru.strela.model.BaseEntity;

import javax.persistence.*;

/**
 * Created by Aisen on 29.04.2016.
 */

@Entity
@Table(name = "athlete_tariff", indexes = {
        @Index(name = "athlete_tariff_athlete", columnList="athlete_id"),
        @Index(name = "athlete_tariff_tariff", columnList="tariff_id"),
        @Index(name = "athlete_tariff_coupon", columnList="coupon_id")
})
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class AthleteTariff extends BaseEntity {

    private Athlete athlete;
    private Tariff tariff;
    private Coupon coupon;

    public AthleteTariff() {
    }

    public AthleteTariff(int id) {
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

    @ManyToOne(targetEntity=Tariff.class, fetch= FetchType.LAZY)
    @JoinColumn(name="tariff_id", nullable = false)
    public Tariff getTariff() {
        return tariff;
    }

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }


    @ManyToOne(targetEntity=Coupon.class, fetch= FetchType.LAZY)
    @JoinColumn(name="coupon_id")
    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }
}
