package ru.strela.web.controller.account;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.model.Athlete;
import ru.strela.model.auth.Person;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.model.filter.payment.AthleteTariffFilter;
import ru.strela.util.ModelBuilder;
import ru.strela.util.ValidateUtils;
import ru.strela.web.controller.core.WebController;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = {"/account"})
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

    @RequestMapping(value = "/", method = {RequestMethod.GET})
    public ModelAndView get() {
        return getModel();
    }

    private ModelAndView getModel() {
        ModelBuilder model = new ModelBuilder("account/account");

        Person person = personServer.getCurrentPerson();
        if (person != null) {
            Athlete athlete = personService.findByPerson(person);
            model.put("athlete", athlete);
            if (athlete != null) {
                AthleteTariffFilter filter = new AthleteTariffFilter();
                filter.setAthlete(athlete);
                filter.addOrder(new Order("id", OrderDirection.Asc));
                model.put("athleteTariffs", paymentService.findAthleteTariffs(filter, false));

                model.put("passwordForm", new PasswordForm());
            }
        }

        return model;
    }

    @RequestMapping(value="/ajax/", method=RequestMethod.POST)
    public ModelAndView onAjax(HttpServletRequest req,
                               Athlete athlete, BindingResult athleteResult,
                               PasswordForm passwordForm, BindingResult passwordResult) {
        String action = req.getParameter("action");

        if ("refresh-crop-image".equals(action)) {
            ajaxUpdate(req, "cropImagePanel");
        } else if ("save-athlete".equals(action)) {
            if (validateAthlete(req, athleteResult, athlete)) {
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

            if (athleteResult.hasErrors()) {
                return new ModelBuilder("account/account");
            }
        } else if ("save-password".equals(action)) {
            Person currentPerson = personServer.getCurrentPerson();
            if (validatePassword(req, currentPerson, passwordForm, passwordResult)) {
                Person person = personService.findById(currentPerson);
                person.setPassword(passwordEncoder.encode(passwordForm.getNewPassword()));
                personService.save(person);
            }

            if (passwordResult.hasErrors()) {
                return new ModelBuilder("account/account");
            }
        }

        return getModel();
    }

    private void ajaxUpdateAthleteFields(HttpServletRequest req) {
        ajaxUpdate(req, "loginField");
        ajaxUpdate(req, "mobileNumberField");
        ajaxUpdate(req, "firstNameField");
        ajaxUpdate(req, "birthdayField");
        ajaxUpdate(req, "emailField");
    }

    private void ajaxUpdatePasswordFields(HttpServletRequest req) {
        ajaxUpdate(req, "oldPasswordField");
        ajaxUpdate(req, "newPasswordField");
        ajaxUpdate(req, "repPasswordField");
    }

    private boolean validateAthlete(HttpServletRequest request, BindingResult result, Athlete athlete) {
        Person person = athlete.getPerson();
        String loginCheckResult;
        if (StringUtils.isBlank(person.getLogin())) {
            result.rejectValue("person.login", "field.required", FIELD_REQUIRED);
        } else if ((loginCheckResult = ValidateUtils.checkLogin(person.getLogin())) != null) {
            result.rejectValue("person.login", "field.required", loginCheckResult);
        } else {
            Person saved = personService.findByLogin(person);
            if(saved != null && person.getId() != saved.getId()) {
                result.rejectValue("person.login", "field.required", "Пользователь с таким login-ом уже существует");
            }
        }
        if (StringUtils.isBlank(athlete.getFirstName())) {
            result.rejectValue("firstName", "field.required", FIELD_REQUIRED);
        }

        String emailCheckResult;
        if (StringUtils.isNotBlank(athlete.getEmail()) && (emailCheckResult = ValidateUtils.checkEmail(athlete.getEmail())) != null) {
            result.rejectValue("email", "field.required", emailCheckResult);
        }

        String phoneCheckResult;
        if (StringUtils.isNotBlank(athlete.getPhoneNumber()) && (phoneCheckResult = ValidateUtils.checkPhone(athlete.getPhoneNumber())) != null) {
            result.rejectValue("phoneNumber", "field.required", phoneCheckResult);
        }

        String mobileCheckResult;
        if (StringUtils.isNotBlank(athlete.getMobileNumber()) && (mobileCheckResult = ValidateUtils.checkPhone(athlete.getMobileNumber())) != null) {
            result.rejectValue("mobileNumber", "field.required", mobileCheckResult);
        }
        ajaxUpdateAthleteFields(request);

        return !result.hasErrors();
    }

    private boolean validatePassword(HttpServletRequest req, Person person, PasswordForm passwordForm, BindingResult result) {
        if (StringUtils.isBlank(passwordForm.getOldPassword())) {
            result.rejectValue("oldPassword", "field.required", FIELD_REQUIRED);
        } else if (!passwordEncoder.matches(passwordForm.getOldPassword(), person.getPassword())) {
            result.rejectValue("oldPassword", "field.required", "Неправильный старый пароль");
        }

        if (StringUtils.isBlank(passwordForm.getNewPassword())) {
            result.rejectValue("newPassword", "field.required", FIELD_REQUIRED);
        } else {
            String verifyPass = ValidateUtils.checkPassword(passwordForm.getNewPassword(), person.getLogin());
            if (verifyPass != null) {
                result.rejectValue("newPassword", "field.required", verifyPass);
            } else if (!passwordForm.getNewPassword().equals(passwordForm.getRepPassword())) {
                result.rejectValue("repPassword", "field.required", "Пароли не совпадают");
            }
        }
        ajaxUpdatePasswordFields(req);

        return !result.hasErrors();
    }

}
