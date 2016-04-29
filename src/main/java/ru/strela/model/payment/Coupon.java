package ru.strela.model.payment;

import ru.strela.model.BaseEntityNamed;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Aisen on 29.04.2016.
 */

@Entity
@Table(name = "coupon")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class Coupon extends BaseEntityNamed {

    private Double discountPercent;
    private Date expiration;

    public Coupon() {
    }

    public Coupon(int id) {
        this.id = id;
    }

    @Column(name = "discount_percent")
    public Double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }
}
