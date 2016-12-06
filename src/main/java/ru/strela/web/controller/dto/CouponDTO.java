package ru.strela.web.controller.dto;

import ru.strela.model.payment.Coupon;

public class CouponDTO extends BaseDTO {

    private String name;

    public CouponDTO() {}

    public CouponDTO(int id, String name) {
        super(id);
        this.name = name;
    }

    public CouponDTO(Coupon coupon) {
        if (coupon != null) {
            id = coupon.getId();
            name= coupon.getName();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
