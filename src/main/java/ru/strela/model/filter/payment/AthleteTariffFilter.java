package ru.strela.model.filter.payment;

import ru.strela.model.Athlete;
import ru.strela.model.filter.BaseFilter;

/**
 * Created by Aisen on 28.04.2016.
 */
public class AthleteTariffFilter extends BaseFilter {

    private Athlete athlete;

    public Athlete getAthlete() {
        return athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

}
