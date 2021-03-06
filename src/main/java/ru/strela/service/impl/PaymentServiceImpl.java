package ru.strela.service.impl;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.strela.model.Athlete;
import ru.strela.model.Gym;
import ru.strela.model.auth.Person;
import ru.strela.model.filter.payment.*;
import ru.strela.model.payment.*;
import ru.strela.repository.payment.*;
import ru.strela.repository.spec.payment.*;
import ru.strela.service.ApplicationService;
import ru.strela.service.PaymentService;
import ru.strela.service.PersonService;
import ru.strela.util.PageRequestBuilder;
import ru.strela.util.validate.IValidateResult;
import ru.strela.util.validate.ValidateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PersonService personService;

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

    @Autowired
    private PersonAccountRepository personAccountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ApplicationService applicationService;

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
        personService.updateFilter(filter);
        return tariffRepository.findAll(TariffSpec.filter(filter), PageRequestBuilder.build(filter, pageNumber, pageSize));
    }

    @Override
    public List<Tariff> findTariffs(TariffFilter filter, boolean checkPermissions) {
        if (checkPermissions) {
            personService.updateFilter(filter);
        }
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
        String checkResult = checkRemove(athleteTariff);
        if (checkResult != null) {
            throw new IllegalArgumentException(checkResult);
        }

        athleteTariffRepository.delete(athleteTariff);
    }

    @Override
    public String checkRemove(AthleteTariff athleteTariff) {
        if (athleteTariff != null) {
            PaymentFilter filter = new PaymentFilter();
            filter.setAthleteTariff(athleteTariff);
            if (!findPayments(filter, false).isEmpty()) {
                return "Невозможно удалить тариф атлета т.к по нему имеются платежи!";
            }
        }

        return null;
    }

    @Override
    public AthleteTariff findById(AthleteTariff athleteTariff) {
        return athleteTariffRepository.findOne(athleteTariff.getId());
    }

    @Override
    public Page<AthleteTariff> findAthleteTariffs(AthleteTariffFilter filter, int pageNumber, int pageSize) {
        personService.updateFilter(filter);
        return athleteTariffRepository.findAll(AthleteTariffSpec.filter(filter), PageRequestBuilder.build(filter, pageNumber, pageSize));
    }

    @Override
    public List<AthleteTariff> findAthleteTariffs(AthleteTariffFilter filter, boolean checkPermissions) {
        if (checkPermissions) {
            personService.updateFilter(filter);
        }
        return athleteTariffRepository.findAll(AthleteTariffSpec.filter(filter), PageRequestBuilder.getSort(filter));
    }

    @Override
    public AthleteTariff getOrCreateAthleteTariff(Athlete athlete, Gym gym) {
        Tariff tariff = null;
        TariffFilter tariffFilter = new TariffFilter();
        tariffFilter.setGym(gym);
        List<Tariff> tariffs = findTariffs(tariffFilter, false);
        if (tariffs.isEmpty()) {
            throw new IllegalArgumentException();
        }
        tariff = tariffs.get(0);

        AthleteTariffFilter filter = new AthleteTariffFilter();
        filter.setAthlete(athlete);
        filter.setTariff(tariff);

        List<AthleteTariff> athleteTariffs = findAthleteTariffs(filter, false);
        if (!athleteTariffs.isEmpty()) {
            return athleteTariffs.get(0);
        }

        AthleteTariff athleteTariff = new AthleteTariff();
        athleteTariff.setAthlete(athlete);
        athleteTariff.setTariff(tariff);
        athleteTariff = save(athleteTariff);

        return athleteTariff;
    }


    @Override
    public PersonAccount save(PersonAccount personAccount) {
        return personAccountRepository.save(personAccount);
    }

    @Override
    public void remove(PersonAccount personAccount) {
        personAccountRepository.delete(personAccount);
    }

    @Override
    public PersonAccount findById(PersonAccount personAccount) {
        return personAccountRepository.findOne(personAccount.getId());
    }

    @Override
    public PersonAccount findByPerson(Person person) {
        return personAccountRepository.findByPerson(person);
    }

    @Override
    public Page<PersonAccount> findPersonAccounts(PersonAccountFilter filter, int pageNumber, int pageSize) {
        return personAccountRepository.findAll(PersonAccountSpec.filter(filter), PageRequestBuilder.build(filter, pageNumber, pageSize));
    }

    @Override
    public List<PersonAccount> findPersonAccounts(PersonAccountFilter filter) {
        return personAccountRepository.findAll(PersonAccountSpec.filter(filter), PageRequestBuilder.getSort(filter));
    }


    @Override
    public Transaction save(Transaction transaction) {
        Transaction existTransaction = findById(transaction);
        Person person = transaction.getPerson();
        if (person != null) {
            PersonAccount personAccount = findByPerson(person);
            if (personAccount == null) {
                personAccount = createOrGetPersonAccount(person);
            }
            if (transaction.getId() == 0) {
                personAccount.setAccount(personAccount.getAccount() - transaction.getAmount());
                save(personAccount);
            } else if (transaction.getAmount() < existTransaction.getAmount()) {
                personAccount.setAccount(personAccount.getAccount() + existTransaction.getAmount() - transaction.getAmount());
                save(personAccount);
            }
        }

        return transactionRepository.save(transaction);
    }

    @Override
    public void remove(Transaction transaction) {
        Transaction existTransaction = findById(transaction);
        Person person = existTransaction.getPerson();
        if (person != null) {
            PersonAccount personAccount = findByPerson(person);
            if (personAccount != null) {
                personAccount.setAccount(personAccount.getAccount() + existTransaction.getAmount());
                save(personAccount);
            }
        }

        transactionRepository.delete(existTransaction);
    }

    @Override
    public Transaction findById(Transaction transaction) {
        return transactionRepository.findOne(transaction.getId());
    }

    @Override
    public Page<Transaction> findTransactions(TransactionFilter filter, int pageNumber, int pageSize) {
        return transactionRepository.findAll(TransactionSpec.filter(filter), PageRequestBuilder.build(filter, pageNumber, pageSize));
    }

    @Override
    public List<Transaction> findTransactions(TransactionFilter filter) {
        return transactionRepository.findAll(TransactionSpec.filter(filter), PageRequestBuilder.getSort(filter));
    }


    @Override
    public Payment save(Payment payment) {
        PaymentStatus paymentStatus = createOrGetPaymentStatus(payment, true);
        if (payment.getId() > 0) {
            Payment old = findById(new Payment(payment.getId()));
            paymentStatus.setPayedTill(calculatePayedTill(paymentStatus.getPayedTill(), old, false));
        }
        Payment saved = paymentRepository.save(payment);
        paymentStatus.setPayedTill(calculatePayedTill(paymentStatus.getPayedTill(), saved, true));
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
            paymentStatus.setPayedTill(new Date(0));
            paymentStatus = paymentStatusRepository.save(paymentStatus);
        }

        return paymentStatus;
    }

    private Date calculatePayedTill(Date payedTill, Payment payment, boolean add) {
        updatePersonAccount(payment, add);

        AthleteTariff athleteTariff = payment.getAthleteTariff();
        Double amount = payment.getAmount();

        Calendar calendar = Calendar.getInstance();
        Integer accountDay = applicationService.getSettings().getAccountDay();
        if (!add || payedTill.compareTo(payment.getDate()) >= 0) {
            calendar.setTime(payedTill);
        } else {
            boolean sameMonth = DateUtils.truncate(payedTill, Calendar.MONTH).compareTo(DateUtils.truncate(payment.getDate(), Calendar.MONTH)) == 0;
            calendar.setTime(payment.getDate());
            calendar = DateUtils.truncate(calendar, Calendar.MONTH);
            calendar.add(Calendar.DAY_OF_YEAR, accountDay);
            if (payment.getDate().getTime() >= calendar.getTimeInMillis()) {
                calendar = DateUtils.truncate(calendar, Calendar.MONTH);
                calendar.add(Calendar.MONTH, 1);
            } else if(sameMonth) {
                calendar.setTime(payedTill);
            } else {
                calendar = DateUtils.truncate(calendar, Calendar.MONTH);
            }
        }

        Tariff tariff = athleteTariff.getTariff();
        Coupon coupon = athleteTariff.getCoupon();
        double k = coupon != null ? (1 - coupon.getDiscountPercent() / 100) : 1;

        Double price;
        Double priceYear = tariff.getPriceYear();
        Double priceHalfYear = tariff.getPriceHalfYear();
        Double priceQuarter = tariff.getPriceQuarter();
        Double priceMonth = tariff.getPriceMonth();
        if (priceYear != null && amount >= (price = priceYear * k)) {
            int years = (int) (amount / price);
            calendar.add(Calendar.YEAR, add ? years : -years);
            amount -= years * price;

            if (priceHalfYear == null && priceQuarter == null && priceMonth == null && amount > 0.0d) {
                int days = (int) (365 * amount / price);
                calendar.add(Calendar.DAY_OF_YEAR, add ? days : -days);
                amount -= days * price / 365;
            }
        }
        if (priceHalfYear != null && amount >= (price = priceHalfYear * k)) {
            int halfYears = (int) (amount / price);
            calendar.add(Calendar.MONTH, (add ? halfYears : -halfYears) * 6);
            amount -= halfYears * price;

            if (priceQuarter == null && priceMonth == null && amount > 0.0d) {
                int days = (int) (180 * amount / price);
                calendar.add(Calendar.DAY_OF_YEAR, add ? days : -days);
                amount -= days * price / 180;
            }
        }
        if (priceQuarter != null && amount >= (price = priceQuarter * k)) {
            int quarters = (int) (amount / price);
            calendar.add(Calendar.MONTH, (add ? quarters : -quarters) * 3);
            amount -= quarters * price;

            if (priceMonth == null && amount > 0.0d) {
                int days = (int) (90 * amount / price);
                calendar.add(Calendar.DAY_OF_YEAR, add ? days : -days);
                amount -= days * price / 90;
            }
        }
        if (priceMonth != null && amount >= (price = priceMonth * k)) {
            int months = (int) (amount / price);
            calendar.add(Calendar.MONTH, add ? months : -months);
            amount -= months * price;

            if (amount > 0.0d) {
                int days = (int) (30 * amount / price);
                calendar.add(Calendar.DAY_OF_YEAR, add ? days : -days);
                amount -= days * price / 30;
            }
        }

//        Double priceOnce = tariff.getPriceOnce();
//        if (priceOnce != null && priceOnce > 0.0d && amount >= priceOnce) {
//            int days = (int) (amount / priceOnce);
//            calendar.add(Calendar.DAY_OF_YEAR, add ? days : -days);
//            amount -= days * priceOnce;
//        }

        return calendar.getTime();
    }

    private PersonAccount createOrGetPersonAccount(Person person) {
        PersonAccount personAccount = null;
        if (person != null) {
            personAccount = findByPerson(person);
            if (personAccount == null) {
                personAccount = new PersonAccount();
                personAccount.setPerson(person);
                personAccount.setAccount(0.0d);
                personAccount = save(personAccount);
            }
        }

        return personAccount;
    }

    private void updatePersonAccount(Payment payment, boolean add) {
        PersonAccount personAccount = createOrGetPersonAccount(payment.getOperator());
        if (personAccount != null) {
            Double amount = payment.getAmount();
            personAccount.setAccount(personAccount.getAccount() + (add ? amount : -amount));
            save(personAccount);
        }
    }


    @Override
    public void remove(Payment payment) {
        Payment old = paymentRepository.findOne(payment.getId());
        PaymentStatus paymentStatus = createOrGetPaymentStatus(old, false);
        if (paymentStatus != null) {
            paymentStatus.setPayedTill(calculatePayedTill(paymentStatus.getPayedTill(), old, false));
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
        personService.updateFilter(filter);
        return paymentRepository.findAll(PaymentSpec.filter(filter), PageRequestBuilder.build(filter, pageNumber, pageSize));
    }

    @Override
    public List<Payment> findPayments(PaymentFilter filter, boolean checkPermissions) {
        if (checkPermissions) {
            personService.updateFilter(filter);
        }
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
        personService.updateFilter(filter);
        return paymentStatusRepository.findAll(PaymentStatusSpec.filter(filter), PageRequestBuilder.build(filter, pageNumber, pageSize));
    }

    @Override
    public List<PaymentStatus> findPaymentStatuses(PaymentStatusFilter filter) {
        personService.updateFilter(filter);
        return paymentStatusRepository.findAll(PaymentStatusSpec.filter(filter), PageRequestBuilder.getSort(filter));
    }

    @Override
    public boolean validate(Payment payment, IValidateResult validateResult) {
        if (payment.getAmount() == null) {
            validateResult.addError("amount", ValidateUtils.REQUIRED_ERROR);
        } else if (payment.getAmount() <= 0.0d) {
            validateResult.addError("amount", ValidateUtils.POSITIVE_NUMBER_ERROR);
        }
        Gym gym = payment.getAthleteTariff().getTariff().getGym();
        if (gym == null) {
            validateResult.addError("athleteTariff.tariff.gym", ValidateUtils.REQUIRED_ERROR);
        } else {
            TariffFilter tariffFilter = new TariffFilter();
            tariffFilter.setGym(gym);
            List<Tariff> tariffs = findTariffs(tariffFilter, false);
            if (tariffs.isEmpty()) {
                validateResult.addError("athleteTariff.tariff.gym", "Для данного зала нету тарифов. Создайте тариф.");
            }
        }

        if (payment.getAthleteTariff().getAthlete() == null) {
            validateResult.addError("athleteTariff.athlete", ValidateUtils.REQUIRED_ERROR);
        }

        if (payment.getDate() == null) {
            validateResult.addError("date", ValidateUtils.REQUIRED_ERROR);
        }
        if (payment.getOperator() == null) {
            validateResult.addError("operator", ValidateUtils.REQUIRED_ERROR);
        }

        return !validateResult.hasErrors();
    }

    @Override
    public Payment savePayment(Payment payment) {
        AthleteTariff athleteTariff = getOrCreateAthleteTariff(payment.getAthleteTariff().getAthlete(), payment.getAthleteTariff().getTariff().getGym());
        payment.setAthleteTariff(athleteTariff);
        if(payment.getId() != 0) {
            Payment saved = findById(new Payment(payment.getId()));

            saved.setAmount(payment.getAmount());
            saved.setOperator(payment.getOperator());
            saved.setDate(payment.getDate());
            saved.setAthleteTariff(payment.getAthleteTariff());

            payment = saved;
        }

        Payment savedPayment = save(payment);

        return savedPayment;
    }
}
