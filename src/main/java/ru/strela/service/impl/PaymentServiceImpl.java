package ru.strela.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.strela.model.filter.payment.CouponFilter;
import ru.strela.model.filter.payment.TariffFilter;
import ru.strela.model.payment.Coupon;
import ru.strela.model.payment.Tariff;
import ru.strela.repository.payment.CouponRepository;
import ru.strela.repository.payment.TariffRepository;
import ru.strela.repository.spec.payment.CouponSpec;
import ru.strela.repository.spec.payment.TariffSpec;
import ru.strela.service.PaymentService;
import ru.strela.util.PageRequestBuilder;

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
        return couponRepository.findAll(CouponSpec.filter(filter));
    }

}
