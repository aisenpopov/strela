package ru.strela.web.controller.dto;

import ru.strela.model.payment.AthleteTariff;

public class AthleteTariffDTO {

    private int id;
    private AthleteDTO athlete;
    private TariffDTO tariff;
    private CouponDTO coupon;

    public AthleteTariffDTO() {}

    public AthleteTariffDTO(AthleteTariff athleteTariff) {
        if (athleteTariff != null) {
            id = athleteTariff.getId();
            if (athleteTariff.getAthlete() != null) {
                athlete = new AthleteDTO(athleteTariff.getAthlete());
            }
            if (athleteTariff.getTariff() != null) {
                tariff = new TariffDTO(athleteTariff.getTariff());
            }
            if (athleteTariff.getCoupon() != null) {
                coupon = new CouponDTO(athleteTariff.getCoupon());
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AthleteDTO getAthlete() {
        return athlete;
    }

    public void setAthlete(AthleteDTO athlete) {
        this.athlete = athlete;
    }

    public TariffDTO getTariff() {
        return tariff;
    }

    public void setTariff(TariffDTO tariff) {
        this.tariff = tariff;
    }

    public CouponDTO getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponDTO coupon) {
        this.coupon = coupon;
    }
}
