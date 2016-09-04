package ru.strela.web.controller.dto;

import ru.strela.model.Team;

public class TeamDTO {

    private int id;
    private String name;

    public TeamDTO() {}

    public TeamDTO(Team team) {
        if (team != null) {
            id = team.getId();
            name = team.getName();
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
