package ru.strela.web.controller.dto;

import ru.strela.model.payment.PaymentStatus;
import ru.strela.util.DateUtils;

public class PaymentStatusDTO extends BaseDTO {

    private AthleteDTO athlete;
    private GymDTO gym;
    private String payedTill;

    public PaymentStatusDTO() {}

    public PaymentStatusDTO(int id) {
        super(id);
    }

    public PaymentStatusDTO(PaymentStatus paymentStatus) {
        if (paymentStatus != null) {
            id = paymentStatus.getId();
            payedTill = DateUtils.formatDDMMYYYY(paymentStatus.getPayedTill());
            if (paymentStatus.getAthlete() != null) {
                athlete = new AthleteDTO(paymentStatus.getAthlete());
            }
            if (paymentStatus.getGym() != null) {
                gym = new GymDTO(paymentStatus.getGym());
            }
        }
    }

    public AthleteDTO getAthlete() {
        return athlete;
    }

    public void setAthlete(AthleteDTO athlete) {
        this.athlete = athlete;
    }

    public GymDTO getGym() {
        return gym;
    }

    public void setGym(GymDTO gym) {
        this.gym = gym;
    }

    public String getPayedTill() {
        return payedTill;
    }

    public void setPayedTill(String payedTill) {
        this.payedTill = payedTill;
    }
}
