package ru.strela.model.filter;


import ru.strela.model.Athlete;

public class TeamFilter extends BaseFilter {

    private Athlete chiefInstructor;

    public Athlete getChiefInstructor() {
        return chiefInstructor;
    }

    public void setChiefInstructor(Athlete chiefInstructor) {
        this.chiefInstructor = chiefInstructor;
    }
}