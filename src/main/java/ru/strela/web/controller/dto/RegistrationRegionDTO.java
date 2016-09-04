package ru.strela.web.controller.dto;

import ru.strela.model.RegistrationRegion;

public class RegistrationRegionDTO {

    private int id;
    private String name;

    public RegistrationRegionDTO() {}

    public RegistrationRegionDTO(RegistrationRegion registrationRegion) {
        if (registrationRegion != null) {
            id = registrationRegion.getId();
            name = registrationRegion.getName();
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
