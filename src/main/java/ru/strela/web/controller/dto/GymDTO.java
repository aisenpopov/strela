package ru.strela.web.controller.dto;

import ru.strela.model.Gym;

public class GymDTO extends BaseDTO {

    private String name;

    public GymDTO() {}

    public GymDTO(Gym gym) {
        if (gym != null) {
            id = gym.getId();
            name = gym.getName();
        }
    }

    public GymDTO(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
