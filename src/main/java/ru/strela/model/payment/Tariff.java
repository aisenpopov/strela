package ru.strela.model.payment;

import ru.strela.model.BaseEntity;
import ru.strela.model.Gym;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Aisen on 28.04.2016.
 */
@Entity
@Table(name = "tariff", indexes = {
        @Index(name = "tariff_gym", columnList="gym_id")
})
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class Tariff extends BaseEntity {

    private String name;
    private Gym gym;
    private Date expiration;

    private Double priceOnce;
    private Double priceMonth;
    private Double priceQuarter;
    private Double priceHalfYear;
    private Double priceYear;

    public Tariff() {}

    public Tariff(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(targetEntity=Gym.class, fetch= FetchType.LAZY)
    @JoinColumn(name="gym_id", nullable = false)
    public Gym getGym() {
        return gym;
    }

    public void setGym(Gym gym) {
        this.gym = gym;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    @Column(name = "price_once")
    public Double getPriceOnce() {
        return priceOnce;
    }

    public void setPriceOnce(Double priceOnce) {
        this.priceOnce = priceOnce;
    }

    @Column(name = "price_month")
    public Double getPriceMonth() {
        return priceMonth;
    }

    public void setPriceMonth(Double priceMonth) {
        this.priceMonth = priceMonth;
    }

    @Column(name = "price_quarter")
    public Double getPriceQuarter() {
        return priceQuarter;
    }

    public void setPriceQuarter(Double priceQuarter) {
        this.priceQuarter = priceQuarter;
    }

    @Column(name = "price_half_year")
    public Double getPriceHalfYear() {
        return priceHalfYear;
    }

    public void setPriceHalfYear(Double priceHalfYear) {
        this.priceHalfYear = priceHalfYear;
    }

    @Column(name = "price_year")
    public Double getPriceYear() {
        return priceYear;
    }

    public void setPriceYear(Double priceYear) {
        this.priceYear = priceYear;
    }
}
