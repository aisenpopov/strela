package ru.strela.model.filter.payment;

import ru.strela.model.Athlete;
import ru.strela.model.Gym;
import ru.strela.model.filter.BaseFilter;
import ru.strela.model.payment.Tariff;

public class AthleteTariffFilter extends BaseFilter {

    private Athlete athlete;
    private Tariff tariff;
    private Gym gym;

    public Athlete getAthlete() {
        return athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    public Tariff getTariff() {
        return tariff;
    }

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }

    public Gym getGym() {
        return gym;
    }

    public void setGym(Gym gym) {
        this.gym = gym;
    }
}
