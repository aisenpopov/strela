package ru.strela.web.controller.account;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.auth.AuthPerson;
import ru.strela.auth.AuthenticationProcessor;
import ru.strela.mail.SendMailHelper;
import ru.strela.model.Athlete;
import ru.strela.model.auth.Person;
import ru.strela.util.ModelBuilder;
import ru.strela.util.ValidateUtils;
import ru.strela.web.controller.core.WebController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Aisen on 07.07.2016.
 */
@Controller
@RequestMapping("/recovery")
public class RecoveryPasswordController extends WebController {

    private enum State {
        recovery,
        send_code,
        wrong_code,
        change_password,
        complete_recovery
    }

    public static class PasswordForm {

        private String login;
        private String newPassword;
        private String repPassword;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
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

    public static class EmailForm {

        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    @Autowired
    private SendMailHelper sendMailHelper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationProcessor authenticationProcessor;


    @RequestMapping(value="/", method = RequestMethod.GET)
    public String get(HttpServletRequest req, Model model) {
        String email = req.getParameter("email");
        String code = req.getParameter("code");
        if (StringUtils.isNotBlank(email) && StringUtils.isNotBlank(code)) {
            Person person = personService.findByEmail(email);
            if (person != null) {
                if (person.getRecoveryCode() != null && person.getRecoveryCode().equals(code)) {
                    model.addAttribute("state", State.change_password);
                    PasswordForm passwordForm = new PasswordForm();
                    passwordForm.setLogin(person.getLogin());
                    model.addAttribute("passwordForm", passwordForm);
                } else {
                    model.addAttribute("state", State.wrong_code);
                }
            }
        } else {
            model.addAttribute("state", State.recovery);
            model.addAttribute("emailForm", new EmailForm());
        }

        return "recoveryPassword";
    }

    @RequestMapping(value="/send_code", method = RequestMethod.POST)
    public ModelAndView sendRecoveryCode(@ModelAttribute("emailForm") EmailForm emailForm, BindingResult emailResult) {
        if (validateEmail(emailForm, emailResult)) {
            String email = emailForm.getEmail();
            Person person = personService.findByEmail(email);
            if (person != null) {
                String code = RandomStringUtils.randomAlphanumeric(10);
                person.setRecoveryCode(code);
                personService.save(person);

                Athlete athlete = personService.findByPerson(person);
                sendMailHelper.sendRecoveryMail(athlete != null ? athlete.getDisplayName() : "", email, code);
                return (new ModelBuilder("recoveryPassword")).put("state", State.send_code);
            }
        }

        return (new ModelBuilder("recoveryPassword")).put("state", State.recovery);
    }

    @RequestMapping(value="/save_new_password", method = RequestMethod.POST)
    public ModelAndView saveNewPassword(HttpServletRequest req, HttpServletResponse res,
                                        @ModelAttribute("passwordForm") PasswordForm passwordForm, BindingResult passwordResult) {
        String login = passwordForm.getLogin();
        if (login != null) {
            Person person = personService.findByLogin(new Person(login));
            if (person != null && validatePassword(passwordForm, passwordResult)) {
                person.setRecoveryCode(null);
                person.setPassword(passwordEncoder.encode(passwordForm.getNewPassword()));
                personService.save(person);

                if (personServer.getCurrentPerson() == null) {
                    authenticationProcessor.startRememberMeSession(req, res, new AuthPerson(person));
                }

                return (new ModelBuilder("recoveryPassword")).put("state", State.complete_recovery);
            }
        }

        return (new ModelBuilder("recoveryPassword")).put("state", State.change_password);
    }

    private boolean validateEmail(EmailForm emailForm, BindingResult emailResult) {
        String email = emailForm.getEmail();
        String verifyEmail = ValidateUtils.checkEmail(email);
        if (verifyEmail != null) {
            emailResult.rejectValue("email", "field.required", verifyEmail);
        } else if (personService.findByEmail(email) == null) {
            emailResult.rejectValue("email", "field.required", "Пользователя с таким email не существует");
        }

        return !emailResult.hasErrors();
    }

    private boolean validatePassword(PasswordForm passwordForm, BindingResult passwordResult) {
        String newPassword = passwordForm.getNewPassword();
        String passwordCheckResult;
        if ((passwordCheckResult = ValidateUtils.checkPassword(newPassword, passwordForm.getLogin())) != null) {
            passwordResult.rejectValue("newPassword", "field.required", passwordCheckResult);
        }

        if (newPassword != null && !newPassword.equals(passwordForm.getRepPassword())) {
            passwordResult.rejectValue("repPassword", "field.required", "Пароли не совпадают");
        }

        return !passwordResult.hasErrors();
    }

}
