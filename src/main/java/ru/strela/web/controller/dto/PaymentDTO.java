package ru.strela.web.controller.dto;

import ru.strela.model.payment.Payment;
import ru.strela.util.DateUtils;

public class PaymentDTO {

    private int id;
    private AthleteTariffDTO athleteTariff;
    private PersonDTO operator;
    private Double amount;
    private String date;

    public PaymentDTO() {}

    public PaymentDTO(Payment payment) {
        if (payment != null) {
            id = payment.getId();
            if (payment.getAthleteTariff() != null) {
                athleteTariff = new AthleteTariffDTO(payment.getAthleteTariff());
            }
            if (payment.getOperator() != null) {
                operator = new PersonDTO(payment.getOperator());
            }
            amount = payment.getAmount();
            date = DateUtils.formatDDMMYYYY(payment.getDate());
        }
    }

    public AthleteTariffDTO getAthleteTariff() {
        return athleteTariff;
    }

    public void setAthleteTariff(AthleteTariffDTO athleteTariff) {
        this.athleteTariff = athleteTariff;
    }

    public PersonDTO getOperator() {
        return operator;
    }

    public void setOperator(PersonDTO operator) {
        this.operator = operator;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
