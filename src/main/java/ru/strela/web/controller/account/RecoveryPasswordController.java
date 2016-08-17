package ru.strela.web.controller.account;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.strela.auth.AuthPerson;
import ru.strela.auth.AuthenticationProcessor;
import ru.strela.mail.SendMailHelper;
import ru.strela.model.Athlete;
import ru.strela.model.auth.Person;
import ru.strela.util.ValidateUtils;
import ru.strela.util.ajax.JsonResponse;
import ru.strela.web.controller.core.WebController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/recovery")
public class RecoveryPasswordController extends WebController {

    public static class PasswordForm {

        private String email;
        private String newPassword;
        private String repPassword;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
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

    @ResponseBody
    @RequestMapping(value="/check_code", method = RequestMethod.POST)
    public JsonResponse get(@RequestParam(value = "email", required = true) String email,
                            @RequestParam(value = "code", required = true) String code) {
        JsonResponse response = new JsonResponse();

        if (StringUtils.isNotBlank(email) && StringUtils.isNotBlank(code)) {
            Person person = personService.findByEmail(email);
            if (person != null && person.getRecoveryCode() != null && person.getRecoveryCode().equals(code)) {
                return response;
            }
        }

        response.setErrorStatus();
        return response;
    }

    @ResponseBody
    @RequestMapping(value="/send_code", method = RequestMethod.POST)
    public JsonResponse sendRecoveryCode(@RequestBody(required = true) EmailForm emailForm) {
        JsonResponse response = new JsonResponse();

        if (validateEmail(emailForm, response)) {
            String email = emailForm.getEmail();
            Person person = personService.findByEmail(email);
            if (person != null) {
                String code = RandomStringUtils.randomAlphanumeric(10);
                person.setRecoveryCode(code);
                personService.save(person);

                Athlete athlete = personService.findByPerson(person);
                sendMailHelper.sendRecoveryMail(athlete != null ? athlete.getDisplayName() : "", email, code);
                return response;
            }
        }

        response.setErrorStatus();
        return response;
    }

    @ResponseBody
    @RequestMapping(value="/save_new_password", method = RequestMethod.POST)
    public JsonResponse saveNewPassword(HttpServletRequest req, HttpServletResponse res,
                                        @RequestBody(required = true) PasswordForm passwordForm) {
        JsonResponse response = new JsonResponse();

        String email = passwordForm.getEmail();
        if (email != null) {
            Person person = personService.findByEmail(email);
            if (person != null && validatePassword(passwordForm, person.getLogin(), response)) {
                person.setRecoveryCode(null);
                person.setPassword(passwordEncoder.encode(passwordForm.getNewPassword()));
                personService.save(person);

                if (personServer.getCurrentPerson() == null) {
                    authenticationProcessor.startRememberMeSession(req, res, new AuthPerson(person));
                }

                return response;
            }
        }

        return response;
    }

    private boolean validateEmail(EmailForm emailForm, JsonResponse response) {
        String email = emailForm.getEmail();
        String verifyEmail = ValidateUtils.checkEmail(email);
        if (verifyEmail != null) {
            response.addFieldMessage("email", verifyEmail);
        } else if (personService.findByEmail(email) == null) {
            response.addFieldMessage("email", "Пользователя с таким email не существует");
        }

        return !response.isStatusError();
    }

    private boolean validatePassword(PasswordForm passwordForm, String login, JsonResponse response) {
        String newPassword = passwordForm.getNewPassword();
        String passwordCheckResult;
        if ((passwordCheckResult = ValidateUtils.checkPassword(newPassword, login)) != null) {
            response.addFieldMessage("newPassword", passwordCheckResult);
        }

        if (newPassword != null && !newPassword.equals(passwordForm.getRepPassword())) {
            response.addFieldMessage("repPassword", "Пароли не совпадают");
        }

        return !response.isStatusError();
    }

}
