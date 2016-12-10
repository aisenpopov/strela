package ru.strela.service;

import org.springframework.data.domain.Page;
import ru.strela.model.Athlete;
import ru.strela.model.Gym;
import ru.strela.model.auth.Person;
import ru.strela.model.filter.payment.AthleteTariffFilter;
import ru.strela.model.filter.payment.CouponFilter;
import ru.strela.model.filter.payment.PaymentFilter;
import ru.strela.model.filter.payment.PaymentStatusFilter;
import ru.strela.model.filter.payment.PersonAccountFilter;
import ru.strela.model.filter.payment.TariffFilter;
import ru.strela.model.filter.payment.TransactionFilter;
import ru.strela.model.payment.AthleteTariff;
import ru.strela.model.payment.Coupon;
import ru.strela.model.payment.Payment;
import ru.strela.model.payment.PaymentStatus;
import ru.strela.model.payment.PersonAccount;
import ru.strela.model.payment.Tariff;
import ru.strela.model.payment.Transaction;
import ru.strela.util.validate.IValidateResult;

import java.util.List;

public interface PaymentService {

    Tariff save(Tariff tariff);

    void remove(Tariff tariff);

    Tariff findById(Tariff tariff);

    Page<Tariff> findTariffs(TariffFilter filter, int pageNumber, int pageSize);

    List<Tariff> findTariffs(TariffFilter filter, boolean checkPermissions);


    Coupon save(Coupon coupon);

    void remove(Coupon coupon);

    Coupon findById(Coupon coupon);

    Page<Coupon> findCoupons(CouponFilter filter, int pageNumber, int pageSize);

    List<Coupon> findCoupons(CouponFilter filter);


    AthleteTariff save(AthleteTariff athleteTariff);

    void remove(AthleteTariff athleteTariff);

    String checkRemove(AthleteTariff athleteTariff);

    AthleteTariff findById(AthleteTariff athleteTariff);

    Page<AthleteTariff> findAthleteTariffs(AthleteTariffFilter filter, int pageNumber, int pageSize);

    List<AthleteTariff> findAthleteTariffs(AthleteTariffFilter filter, boolean checkPermissions);

    AthleteTariff getOrCreateAthleteTariff(Athlete athlete, Gym gym);


    PersonAccount save(PersonAccount personAccount);

    void remove(PersonAccount personAccount);

    PersonAccount findById(PersonAccount personAccount);

    PersonAccount findByPerson(Person person);

    Page<PersonAccount> findPersonAccounts(PersonAccountFilter filter, int pageNumber, int pageSize);

    List<PersonAccount> findPersonAccounts(PersonAccountFilter filter);


    Transaction save(Transaction transaction);

    void remove(Transaction transaction);

    Transaction findById(Transaction transaction);

    Page<Transaction> findTransactions(TransactionFilter filter, int pageNumber, int pageSize);

    List<Transaction> findTransactions(TransactionFilter filter);


    Payment save(Payment payment);

    void remove(Payment payment);

    Payment findById(Payment payment);

    Page<Payment> pagePayments(PaymentFilter filter, boolean checkPermissions);

    List<Payment> findPayments(PaymentFilter filter, boolean checkPermissions);


    PaymentStatus save(PaymentStatus paymentStatus);

    void remove(PaymentStatus paymentStatus);

    PaymentStatus findById(PaymentStatus paymentStatus);

    Page<PaymentStatus> findPaymentStatuses(PaymentStatusFilter filter, int pageNumber, int pageSize);

    List<PaymentStatus> findPaymentStatuses(PaymentStatusFilter filter, boolean checkPermissions);


    boolean validate(Payment payment, IValidateResult validateResult);

    Payment savePayment(Payment payment);

}
