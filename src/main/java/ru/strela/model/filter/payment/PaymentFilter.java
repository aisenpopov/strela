package ru.strela.model.filter.payment;

import ru.strela.model.filter.BaseFilter;
import ru.strela.model.payment.AthleteTariff;

public class PaymentFilter extends BaseFilter {

    private AthleteTariff athleteTariff;

    public AthleteTariff getAthleteTariff() {
        return athleteTariff;
    }

    public void setAthleteTariff(AthleteTariff athleteTariff) {
        this.athleteTariff = athleteTariff;
    }
}
