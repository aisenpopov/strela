package ru.strela.model.filter.payment;

import ru.strela.model.Athlete;
import ru.strela.model.filter.BaseFilter;
import ru.strela.model.payment.AthleteTariff;

public class PaymentFilter extends BaseFilter {

    private Athlete athlete;

    private AthleteTariff athleteTariff;


    public Athlete getAthlete() {
        return athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    public AthleteTariff getAthleteTariff() {
        return athleteTariff;
    }

    public void setAthleteTariff(AthleteTariff athleteTariff) {
        this.athleteTariff = athleteTariff;
    }
}
