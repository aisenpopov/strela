package ru.strela.model.filter.payment;

import ru.strela.model.Athlete;
import ru.strela.model.Gym;
import ru.strela.model.filter.BaseFilter;

/**
 * Created by Aisen on 28.04.2016.
 */
public class PaymentStatusFilter extends BaseFilter {

    private Athlete athlete;
    private Gym gym;

    public Athlete getAthlete() {
        return athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    public Gym getGym() {
        return gym;
    }

    public void setGym(Gym gym) {
        this.gym = gym;
    }
}
