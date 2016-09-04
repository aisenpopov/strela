package ru.strela.service;

import org.springframework.data.domain.Page;
import ru.strela.model.Athlete;
import ru.strela.model.Gym;
import ru.strela.model.auth.Person;
import ru.strela.model.filter.payment.*;
import ru.strela.model.payment.*;

import java.util.List;

/**
 * Created by Aisen on 29.04.2016.
 */
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

    Page<Payment> findPayments(PaymentFilter filter, int pageNumber, int pageSize);

    List<Payment> findPayments(PaymentFilter filter, boolean checkPermissions);


    PaymentStatus save(PaymentStatus paymentStatus);

    void remove(PaymentStatus paymentStatus);

    PaymentStatus findById(PaymentStatus paymentStatus);

    Page<PaymentStatus> findPaymentStatuses(PaymentStatusFilter filter, int pageNumber, int pageSize);

    List<PaymentStatus> findPaymentStatuses(PaymentStatusFilter filter);

}
