package ru.strela.model.filter.payment;

import ru.strela.model.Gym;
import ru.strela.model.filter.BaseFilter;

/**
 * Created by Aisen on 28.04.2016.
 */
public class TariffFilter extends BaseFilter {

    private Gym gym;

    public Gym getGym() {
        return gym;
    }

    public void setGym(Gym gym) {
        this.gym = gym;
    }
}
