package ru.strela.web.controller.dto;

import ru.strela.model.payment.Tariff;

public class TariffDTO {

    private int id;
    private String name;

    public TariffDTO() {}

    public TariffDTO(Tariff tariff) {
        if (tariff != null) {
            id = tariff.getId();
            name= tariff.getName();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
