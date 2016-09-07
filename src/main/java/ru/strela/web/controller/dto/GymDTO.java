package ru.strela.web.controller.dto;

import ru.strela.model.Gym;

public class GymDTO {

    private int id;
    private String name;

    public GymDTO() {}

    public GymDTO(Gym gym) {
        if (gym != null) {
            id = gym.getId();
            name = gym.getName();
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
