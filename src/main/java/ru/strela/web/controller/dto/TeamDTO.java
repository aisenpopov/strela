package ru.strela.web.controller.dto;

import ru.strela.model.Team;

public class TeamDTO {

    private int id;
    private String name;
    private CityDTO city;
    private AthleteDTO chiefInstructor;

    public TeamDTO() {}

    public TeamDTO(Team team) {
        if (team != null) {
            id = team.getId();
            name = team.getName();
            if (team.getCity() != null) {
                city = new CityDTO(team.getCity());
            }
            if (team.getChiefInstructor() != null) {
                chiefInstructor = new AthleteDTO(team.getChiefInstructor(), false);
            }
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

    public CityDTO getCity() {
        return city;
    }

    public void setCity(CityDTO city) {
        this.city = city;
    }

    public AthleteDTO getChiefInstructor() {
        return chiefInstructor;
    }

    public void setChiefInstructor(AthleteDTO chiefInstructor) {
        this.chiefInstructor = chiefInstructor;
    }
}
