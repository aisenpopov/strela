package ru.strela.web.controller.dto;

import ru.strela.model.City;

public class CityDTO {

    private int id;
    private String name;

    public CityDTO() {}

    public CityDTO(City city) {
        if (city != null) {
            id = city.getId();
            name = city.getName();
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
