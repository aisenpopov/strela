package ru.strela.web.controller.account;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.strela.model.Athlete;
import ru.strela.model.Gym;
import ru.strela.model.auth.Person;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.model.filter.payment.AthleteTariffFilter;
import ru.strela.model.filter.payment.PaymentStatusFilter;
import ru.strela.model.payment.AthleteTariff;
import ru.strela.model.payment.Coupon;
import ru.strela.model.payment.PaymentStatus;
import ru.strela.model.payment.Tariff;
import ru.strela.util.DateUtils;
import ru.strela.util.ajax.JsonData;
import ru.strela.util.ajax.JsonResponse;
import ru.strela.util.validate.ValidateUtils;
import ru.strela.web.controller.core.WebController;
import ru.strela.web.controller.dto.AthleteDTO;
import ru.strela.web.controller.dto.AthleteTariffDTO;
import ru.strela.web.controller.dto.CouponDTO;
import ru.strela.web.controller.dto.GymDTO;
import ru.strela.web.controller.dto.PaymentStatusDTO;
import ru.strela.web.controller.dto.PersonDTO;
import ru.strela.web.controller.dto.TariffDTO;

import java.util.List;

@Controller
public class AccountController extends WebController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static class PasswordForm {
        private String oldPassword;
        private String newPassword;
        private String repPassword;

        public String getOldPassword() {
            return oldPassword;
        }

        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }

        public String getRepPassword() {
            return repPassword;
        }

        public void setRepPassword(String repPassword) {
            this.repPassword = repPassword;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getCurrentPerson", method = RequestMethod.POST)
    public JsonResponse getCurrentPerson() {
        JsonResponse response = new JsonResponse();
        JsonData data = response.createJsonData();

        Person person = personServer.getCurrentPerson();
        if (person != null) {
            data.put("person", new PersonDTO(person));
        }

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/account/getCurrentAthlete", method = RequestMethod.POST)
    public JsonResponse getCurrentAthlete() {
        JsonResponse response = new JsonResponse();
        JsonData data = response.createJsonData();

        Person person = personServer.getCurrentPerson();
        if (person != null) {
            Athlete athlete = personService.findByPerson(person);
            if (athlete != null) {
                data.put("athlete", new AthleteDTO(athlete));

                data.put("changePassword", new PasswordForm());

                AthleteTariffFilter filter = new AthleteTariffFilter();
                filter.setAthlete(athlete);
                filter.addOrder(new Order("id", OrderDirection.Asc));
                List<AthleteTariff> athleteTariffList = paymentService.findAthleteTariffs(filter, false);
                for (AthleteTariff athleteTariff : athleteTariffList) {
                    JsonData at = data.createCollection("athleteTariffs");
                    at.put("id", athleteTariff.getId());
                    at.put("tariffName", athleteTariff.getTariff() != null ? athleteTariff.getTariff().getName() : "");
                    at.put("couponName", athleteTariff.getCoupon() != null ? athleteTariff.getCoupon().getName() : "");
                }
            }
        }

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/account/getCurrentAthleteInfo", method = RequestMethod.POST)
    public JsonResponse getCurrentAthleteInfo() {
        JsonResponse response = new JsonResponse();
        JsonData data = response.createJsonData();

        Person person = personServer.getCurrentPerson();
        if (person != null) {
            Athlete athlete = personService.findByPerson(person);
            if (athlete != null) {
                data.put("athlete", new AthleteDTO(athlete));

                PaymentStatusFilter paymentStatusFilter = new PaymentStatusFilter();
                paymentStatusFilter.setAthlete(athlete);
                for (PaymentStatus paymentStatus : paymentService.findPaymentStatuses(paymentStatusFilter, false)) {
                    PaymentStatusDTO paymentStatusDTO = new PaymentStatusDTO(paymentStatus.getId());
                    paymentStatusDTO.setPayedTill(DateUtils.formatDDMMYYYY(paymentStatus.getPayedTill()));
                    Gym gym = paymentStatus.getGym();
                    if (gym != null) {
                        paymentStatusDTO.setGym(new GymDTO(gym.getId(), gym.getName()));
                    }
                    data.addCollectionItem("paymentStatuses", paymentStatusDTO);
                }

                AthleteTariffFilter filter = new AthleteTariffFilter();
                filter.setAthlete(athlete);
                filter.addOrder(new Order("id", OrderDirection.Asc));
                for (AthleteTariff athleteTariff : paymentService.findAthleteTariffs(filter, false)) {
                    AthleteTariffDTO athleteTariffDTO = new AthleteTariffDTO(athleteTariff.getId());

                    Tariff tariff = athleteTariff.getTariff();
                    if (tariff != null) {
                        athleteTariffDTO.setTariff(new TariffDTO(tariff.getId(), tariff.getName()));
                    }

                    Coupon coupon = athleteTariff.getCoupon();
                    if (coupon != null) {
                        athleteTariffDTO.setCoupon(new CouponDTO(coupon.getId(), coupon.getName()));
                    }
                    data.addCollectionItem("athleteTariffs", athleteTariffDTO);
                }
            }
        }

        return response;
    }

    @ResponseBody
    @RequestMapping(value="/account/saveAthlete", method=RequestMethod.POST)
    public JsonResponse saveAthlete(Athlete athlete) {
        JsonResponse response = new JsonResponse();

        if (validateAthlete(response, athlete)) {
            Person person = athlete.getPerson();
            Person existPerson = personService.findById(person);
            if (existPerson != null) {
                existPerson.setLogin(person.getLogin());

                person = personService.save(existPerson);
            }

            Athlete existAthlete = personService.findById(new Athlete(athlete.getId()));
            if (existAthlete != null) {
                existAthlete.setFirstName(athlete.getFirstName());
                existAthlete.setLastName(athlete.getLastName());
                existAthlete.setMiddleName(athlete.getMiddleName());
                existAthlete.setSex(athlete.getSex());
                existAthlete.setBirthday(athlete.getBirthday());
                existAthlete.setWeight(athlete.getWeight());
                existAthlete.setHeight(athlete.getHeight());
                existAthlete.setGiSize(athlete.getGiSize());
                existAthlete.setRashguardSize(athlete.getRashguardSize());

                existAthlete.setEmail(athlete.getEmail());
                existAthlete.setPhoneNumber(athlete.getPhoneNumber());
                existAthlete.setMobileNumber(athlete.getMobileNumber());
                existAthlete.setVk(athlete.getVk());
                existAthlete.setFacebook(athlete.getFacebook());
                existAthlete.setInstagram(athlete.getInstagram());
                existAthlete.setSkype(athlete.getSkype());

                existAthlete.setPerson(person);
                personService.save(existAthlete);
            }
        }

        return response;
    }

    @ResponseBody
    @RequestMapping(value="/account/savePassword", method=RequestMethod.POST)
    public JsonResponse savePassword(PasswordForm passwordForm) {
        JsonResponse response = new JsonResponse();

        Person currentPerson = personServer.getCurrentPerson();
        if (validatePassword(response, currentPerson, passwordForm)) {
            Person person = personService.findById(currentPerson);
            person.setPassword(passwordEncoder.encode(passwordForm.getNewPassword()));
            personService.save(person);
        }

        return response;
    }

    private boolean validateAthlete(JsonResponse response, Athlete athlete) {
        Person person = athlete.getPerson();
        String loginCheckResult;
        if (StringUtils.isBlank(person.getLogin())) {
            response.addFieldMessage("personLogin", FIELD_REQUIRED);
        } else if ((loginCheckResult = ValidateUtils.checkLogin(person.getLogin())) != null) {
            response.addFieldMessage("personLogin", loginCheckResult);
        } else {
            Person saved = personService.findByLogin(person);
            if(saved != null && person.getId() != saved.getId()) {
                response.addFieldMessage("personLogin", "Пользователь с таким login-ом уже существует");
            }
        }
        if (StringUtils.isBlank(athlete.getFirstName())) {
            response.addFieldMessage("firstName", FIELD_REQUIRED);
        }

        String emailCheckResult;
        if (StringUtils.isNotBlank(athlete.getEmail()) && (emailCheckResult = ValidateUtils.checkEmail(athlete.getEmail())) != null) {
            response.addFieldMessage("email", emailCheckResult);
        }

        String phoneCheckResult;
        if (StringUtils.isNotBlank(athlete.getPhoneNumber()) && (phoneCheckResult = ValidateUtils.checkPhone(athlete.getPhoneNumber())) != null) {
            response.addFieldMessage("phoneNumber", phoneCheckResult);
        }

        String mobileCheckResult;
        if (StringUtils.isNotBlank(athlete.getMobileNumber()) && (mobileCheckResult = ValidateUtils.checkPhone(athlete.getMobileNumber())) != null) {
            response.addFieldMessage("mobileNumber", mobileCheckResult);
        }

        return !response.isStatusError();
    }

    private boolean validatePassword(JsonResponse response, Person person, PasswordForm passwordForm) {
        if (StringUtils.isBlank(passwordForm.getOldPassword())) {
            response.addFieldMessage("oldPassword", FIELD_REQUIRED);
        } else if (!passwordEncoder.matches(passwordForm.getOldPassword(), person.getPassword())) {
            response.addFieldMessage("oldPassword", "Неправильный старый пароль");
        }

        if (StringUtils.isBlank(passwordForm.getNewPassword())) {
            response.addFieldMessage("newPassword", FIELD_REQUIRED);
        } else {
            String verifyPass = ValidateUtils.checkPassword(passwordForm.getNewPassword(), person.getLogin());
            if (verifyPass != null) {
                response.addFieldMessage("newPassword", verifyPass);
            } else if (!passwordForm.getNewPassword().equals(passwordForm.getRepPassword())) {
                response.addFieldMessage("repPassword", "Пароли не совпадают");
            }
        }

        return !response.isStatusError();
    }

}
