package ru.strela.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.strela.model.Athlete;
import ru.strela.model.Gym;
import ru.strela.model.filter.payment.*;
import ru.strela.model.payment.*;
import ru.strela.repository.payment.*;
import ru.strela.repository.spec.payment.*;
import ru.strela.service.PaymentService;
import ru.strela.util.PageRequestBuilder;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Aisen on 29.04.2016.
 */
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private TariffRepository tariffRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private AthleteTariffRepository athleteTariffRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentStatusRepository paymentStatusRepository;

    @Override
    public Tariff save(Tariff tariff) {
        return tariffRepository.save(tariff);
    }

    @Override
    public void remove(Tariff tariff) {
        tariffRepository.delete(tariff);
    }

    @Override
    public Tariff findById(Tariff tariff) {
        return tariffRepository.findOne(tariff.getId());
    }

    @Override
    public Page<Tariff> findTariffs(TariffFilter filter, int pageNumber, int pageSize) {
        return tariffRepository.findAll(TariffSpec.filter(filter), PageRequestBuilder.build(filter, pageNumber, pageSize));
    }

    @Override
    public List<Tariff> findTariffs(TariffFilter filter) {
        return tariffRepository.findAll(TariffSpec.filter(filter), PageRequestBuilder.getSort(filter));
    }


    @Override
    public Coupon save(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public void remove(Coupon coupon) {
        couponRepository.delete(coupon);
    }

    @Override
    public Coupon findById(Coupon coupon) {
        return couponRepository.findOne(coupon.getId());
    }

    @Override
    public Page<Coupon> findCoupons(CouponFilter filter, int pageNumber, int pageSize) {
        return couponRepository.findAll(CouponSpec.filter(filter), PageRequestBuilder.build(filter, pageNumber, pageSize));
    }

    @Override
    public List<Coupon> findCoupons(CouponFilter filter) {
        return couponRepository.findAll(CouponSpec.filter(filter), PageRequestBuilder.getSort(filter));
    }


    @Override
    public AthleteTariff save(AthleteTariff athleteTariff) {
        return athleteTariffRepository.save(athleteTariff);
    }

    @Override
    public void remove(AthleteTariff athleteTariff) {
        athleteTariffRepository.delete(athleteTariff);
    }

    @Override
    public AthleteTariff findById(AthleteTariff athleteTariff) {
        return athleteTariffRepository.findOne(athleteTariff.getId());
    }

    @Override
    public Page<AthleteTariff> findAthleteTariffs(AthleteTariffFilter filter, int pageNumber, int pageSize) {
        return athleteTariffRepository.findAll(AthleteTariffSpec.filter(filter), PageRequestBuilder.build(filter, pageNumber, pageSize));
    }

    @Override
    public List<AthleteTariff> findAthleteTariffs(AthleteTariffFilter filter) {
        return athleteTariffRepository.findAll(AthleteTariffSpec.filter(filter), PageRequestBuilder.getSort(filter));
    }


    @Override
    public Payment save(Payment payment) {
        PaymentStatus paymentStatus = createOrGetPaymentStatus(payment, true);
        if (payment.getId() > 0) {
            Payment old = findById(new Payment(payment.getId()));
            paymentStatus.setPayedTill(calculatePayedTill(paymentStatus.getPayedTill(),
                                                            old.getAthleteTariff(),
                                                            -old.getAmount()));
        }
        Payment saved = paymentRepository.save(payment);
        paymentStatus.setPayedTill(calculatePayedTill(paymentStatus.getPayedTill(),
                                                        saved.getAthleteTariff(),
                                                        saved.getAmount()));
        paymentStatusRepository.save(paymentStatus);

        return saved;
    }

    private PaymentStatus createOrGetPaymentStatus(Payment payment, boolean create) {
        Athlete athlete = payment.getAthleteTariff().getAthlete();
        Gym gym = payment.getAthleteTariff().getTariff().getGym();

        PaymentStatusFilter filter = new PaymentStatusFilter();
        filter.setAthlete(athlete);
        filter.setGym(gym);
        PaymentStatus paymentStatus = paymentStatusRepository.findOne(PaymentStatusSpec.filter(filter));
        if (create && paymentStatus == null) {
            paymentStatus = new PaymentStatus();
            paymentStatus.setAthlete(athlete);
            paymentStatus.setGym(gym);
            paymentStatus.setPayedTill(new Date());
            paymentStatus = paymentStatusRepository.save(paymentStatus);
        }

        return paymentStatus;
    }

    private Date calculatePayedTill(Date date, AthleteTariff athleteTariff, Double amount) {
        boolean add = amount >= 0.0d;
        amount = amount >= 0.0d ? amount : -amount;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Tariff tariff = athleteTariff.getTariff();
        Coupon coupon = athleteTariff.getCoupon();
        double k = coupon != null ? (1 - coupon.getDiscountPercent() / 100) : 1;
        Double price;
        Double priceYear = tariff.getPriceYear();
        if (priceYear != null && priceYear > 0.0d && amount >= (price = priceYear * k)) {
            int years = (int) (amount / price);
            calendar.add(Calendar.YEAR, add ? years : -years);
            amount -= years * price;
        }
        Double priceHalfYear = tariff.getPriceHalfYear();
        if (priceHalfYear != null && priceHalfYear > 0.0d && amount >= (price = priceHalfYear * k)) {
            int halfYears = (int) (amount / price);
            calendar.add(Calendar.MONTH, (add ? halfYears : -halfYears) * 6);
            amount -= halfYears * price;
        }
        Double priceQuarter = tariff.getPriceQuarter();
        if (priceQuarter != null && priceQuarter > 0.0d && amount >= (price = priceQuarter * k)) {
            int quarters = (int) (amount / price);
            calendar.add(Calendar.MONTH, (add ? quarters : -quarters) * 3);
            amount -= quarters * price;
        }
        Double priceMonth = tariff.getPriceMonth();
        if (priceMonth != null && priceMonth > 0.0d && amount >= (price = priceMonth * k)) {
            int months = (int) (amount / price);
            calendar.add(Calendar.MONTH, add ? months : -months);
            amount -= months * price;
        }
        if (priceMonth != null && priceMonth > 0.0d && amount >= 0.0d) {
            price = priceMonth * k;
            int days = (int) (30 * amount / price);
            calendar.add(Calendar.DAY_OF_YEAR, add ? days : -days);
            amount -= days * price / 30;
        }
//        Double priceOnce = tariff.getPriceOnce();
//        if (priceOnce != null && priceOnce > 0.0d && amount >= priceOnce) {
//            int days = (int) (amount / priceOnce);
//            calendar.add(Calendar.DAY_OF_YEAR, add ? days : -days);
//            amount -= days * priceOnce;
//        }

        return calendar.getTime();
    }


    @Override
    public void remove(Payment payment) {
        Payment old = paymentRepository.findOne(payment.getId());
        PaymentStatus paymentStatus = createOrGetPaymentStatus(old, false);
        if (paymentStatus != null) {
            paymentStatus.setPayedTill(calculatePayedTill(paymentStatus.getPayedTill(),
                                                            old.getAthleteTariff(),
                                                            -old.getAmount()));
            paymentStatusRepository.save(paymentStatus);
        }

        paymentRepository.delete(old);
    }

    @Override
    public Payment findById(Payment payment) {
        return paymentRepository.findOne(payment.getId());
    }

    @Override
    public Page<Payment> findPayments(PaymentFilter filter, int pageNumber, int pageSize) {
        return paymentRepository.findAll(PaymentSpec.filter(filter), PageRequestBuilder.build(filter, pageNumber, pageSize));
    }

    @Override
    public List<Payment> findPayments(PaymentFilter filter) {
        return paymentRepository.findAll(PaymentSpec.filter(filter), PageRequestBuilder.getSort(filter));
    }


    @Override
    public PaymentStatus save(PaymentStatus paymentStatus) {
        return paymentStatusRepository.save(paymentStatus);
    }

    @Override
    public void remove(PaymentStatus paymentStatus) {
        paymentStatusRepository.delete(paymentStatus);
    }

    @Override
    public PaymentStatus findById(PaymentStatus paymentStatus) {
        return paymentStatusRepository.findOne(paymentStatus.getId());
    }

    @Override
    public Page<PaymentStatus> findPaymentStatuses(PaymentStatusFilter filter, int pageNumber, int pageSize) {
        return paymentStatusRepository.findAll(PaymentStatusSpec.filter(filter), PageRequestBuilder.build(filter, pageNumber, pageSize));
    }

    @Override
    public List<PaymentStatus> findPaymentStatuses(PaymentStatusFilter filter) {
        return paymentStatusRepository.findAll(PaymentStatusSpec.filter(filter), PageRequestBuilder.getSort(filter));
    }
}
