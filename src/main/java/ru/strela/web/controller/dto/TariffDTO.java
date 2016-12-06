package ru.strela.web.controller.dto;

import ru.strela.model.payment.Tariff;

public class TariffDTO extends BaseDTO {

    private String name;
    private GymDTO gym;

    private Double priceOnce;
    private Double priceMonth;
    private Double priceQuarter;
    private Double priceHalfYear;
    private Double priceYear;

    public TariffDTO() {}

    public TariffDTO(int id, String name) {
        super(id);
        this.name = name;
    }

    public TariffDTO(Tariff tariff) {
        if (tariff != null) {
            id = tariff.getId();
            name= tariff.getName();

            if (tariff.getGym() != null) {
                gym = new GymDTO(tariff.getGym());
            }

            priceMonth = tariff.getPriceMonth();
            priceQuarter = tariff.getPriceQuarter();
            priceHalfYear = tariff.getPriceHalfYear();
            priceYear = tariff.getPriceYear();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GymDTO getGym() {
        return gym;
    }

    public void setGym(GymDTO gym) {
        this.gym = gym;
    }

    public Double getPriceOnce() {
        return priceOnce;
    }

    public void setPriceOnce(Double priceOnce) {
        this.priceOnce = priceOnce;
    }

    public Double getPriceMonth() {
        return priceMonth;
    }

    public void setPriceMonth(Double priceMonth) {
        this.priceMonth = priceMonth;
    }

    public Double getPriceQuarter() {
        return priceQuarter;
    }

    public void setPriceQuarter(Double priceQuarter) {
        this.priceQuarter = priceQuarter;
    }

    public Double getPriceHalfYear() {
        return priceHalfYear;
    }

    public void setPriceHalfYear(Double priceHalfYear) {
        this.priceHalfYear = priceHalfYear;
    }

    public Double getPriceYear() {
        return priceYear;
    }

    public void setPriceYear(Double priceYear) {
        this.priceYear = priceYear;
    }
}
