package ru.strela.model.filter;

import ru.strela.model.Team;

/**
 * Created by Aisen on 28.04.2016.
 */
public class GymFilter extends BaseFilter {

    private Team team;

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
