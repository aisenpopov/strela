package ru.strela.model.filter.payment;

import ru.strela.model.filter.BaseFilter;
import ru.strela.model.payment.AthleteTariff;

/**
 * Created by Aisen on 28.04.2016.
 */
public class PaymentFilter extends BaseFilter {

    private AthleteTariff athleteTariff;

    public AthleteTariff getAthleteTariff() {
        return athleteTariff;
    }

    public void setAthleteTariff(AthleteTariff athleteTariff) {
        this.athleteTariff = athleteTariff;
    }
}
