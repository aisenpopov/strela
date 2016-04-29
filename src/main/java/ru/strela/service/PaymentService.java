package ru.strela.service;

import org.springframework.data.domain.Page;
import ru.strela.model.filter.payment.CouponFilter;
import ru.strela.model.filter.payment.TariffFilter;
import ru.strela.model.payment.Coupon;
import ru.strela.model.payment.Tariff;

import java.util.List;

/**
 * Created by Aisen on 29.04.2016.
 */
public interface PaymentService {

    Tariff save(Tariff tariff);

    void remove(Tariff tariff);

    Tariff findById(Tariff tariff);

    Page<Tariff> findTariffs(TariffFilter filter, int pageNumber, int pageSize);

    List<Tariff> findTariffs(TariffFilter filter);


    Coupon save(Coupon coupon);

    void remove(Coupon coupon);

    Coupon findById(Coupon coupon);

    Page<Coupon> findCoupons(CouponFilter filter, int pageNumber, int pageSize);

    List<Coupon> findCoupons(CouponFilter filter);

}
