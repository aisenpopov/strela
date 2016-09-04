package ru.strela.web.controller.dto;

import ru.strela.model.payment.Coupon;

public class CouponDTO {

    private int id;
    private String name;

    public CouponDTO() {}

    public CouponDTO(Coupon coupon) {
        if (coupon != null) {
            id = coupon.getId();
            name= coupon.getName();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
