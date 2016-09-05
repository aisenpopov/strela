package ru.strela.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.strela.model.Athlete;
import ru.strela.model.auth.Person;
import ru.strela.service.AthleteService;
import ru.strela.service.PersonService;
import ru.strela.util.validate.IValidateResult;
import ru.strela.util.validate.ValidateUtils;

@Service
@Transactional
public class AthleteServiceImpl implements AthleteService {

    @Autowired
    private PersonService personService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Athlete save(Athlete athlete) {
        Person person = athlete.getPerson();
        String newPassword = person.getPassword();
        if (person.getId() != 0) {
            Person existPerson = personService.findById(person);

            existPerson.setLogin(person.getLogin());
            existPerson.setAdmin(person.isAdmin());
            existPerson.setDisabled(person.isDisabled());

            person = existPerson;
        }
        person.setInstructor(athlete.isInstructor());
        if (StringUtils.isNotBlank(newPassword)) {
            person.setPassword(passwordEncoder.encode(newPassword));
        }

        if (athlete.getId() != 0) {
            Athlete existAthlete = personService.findById(new Athlete(athlete.getId()));

            existAthlete.setFirstName(athlete.getFirstName());
            existAthlete.setLastName(athlete.getLastName());
            existAthlete.setMiddleName(athlete.getMiddleName());
            existAthlete.setNickName(athlete.getNickName());
            existAthlete.setSex(athlete.getSex());
            existAthlete.setBirthday(athlete.getBirthday());
            existAthlete.setStartDate(athlete.getStartDate());
            existAthlete.setWeight(athlete.getWeight());
            existAthlete.setHeight(athlete.getHeight());
            existAthlete.setGiSize(athlete.getGiSize());
            existAthlete.setRashguardSize(athlete.getRashguardSize());
            existAthlete.setPassportNumber(athlete.getPassportNumber());
            existAthlete.setInstructor(athlete.isInstructor());
            existAthlete.setRetired(athlete.isRetired());

            existAthlete.setRegistrationRegion(athlete.getRegistrationRegion());
            existAthlete.setTeam(athlete.getTeam());

            existAthlete.setEmail(athlete.getEmail());
            existAthlete.setPhoneNumber(athlete.getPhoneNumber());
            existAthlete.setMobileNumber(athlete.getMobileNumber());
            existAthlete.setVk(athlete.getVk());
            existAthlete.setFacebook(athlete.getFacebook());
            existAthlete.setInstagram(athlete.getInstagram());
            existAthlete.setSkype(athlete.getSkype());
            existAthlete.setComment(athlete.getComment());

            athlete = existAthlete;
        }

        person = personService.save(person);
        athlete.setPerson(person);
        Athlete savedAthlete = personService.save(athlete);

        return savedAthlete;
    }

    @Override
    public boolean validate(Athlete athlete, IValidateResult validateResult) {
        Person person = athlete.getPerson();
        String loginCheckResult;
        if (person == null || StringUtils.isBlank(person.getLogin())) {
            validateResult.addError("person.login", ValidateUtils.REQUIRED_ERROR);
        } else if ((loginCheckResult = ValidateUtils.checkLogin(person.getLogin())) != null) {
            validateResult.addError("person.login", loginCheckResult);
        } else {
            Person saved = personService.findByLogin(person);
            if(saved != null && person.getId() != saved.getId()) {
                validateResult.addError("person.login", "Пользователь с таким login-ом уже существует");
            }
        }
        if (athlete.isInstructor() && athlete.getTeam() == null) {
            validateResult.addError("team", "Для инструктора необходимо выбрать команду");
        }
        String passwordCheckResult;
        if ((athlete.getId() == 0 || (athlete.getId() > 0 && StringUtils.isNotBlank(person.getPassword()))) && (passwordCheckResult = ValidateUtils.checkPassword(person.getPassword(), person.getLogin())) != null) {
            validateResult.addError("person.password", passwordCheckResult);
        }
        if (StringUtils.isBlank(athlete.getFirstName())) {
            validateResult.addError("firstName", ValidateUtils.REQUIRED_ERROR);
        }

        String emailCheckResult;
        if (StringUtils.isNotBlank(athlete.getEmail()) && (emailCheckResult = ValidateUtils.checkEmail(athlete.getEmail())) != null) {
            validateResult.addError("email", emailCheckResult);
        }

        String phoneCheckResult;
        if (StringUtils.isNotBlank(athlete.getPhoneNumber()) && (phoneCheckResult = ValidateUtils.checkPhone(athlete.getPhoneNumber())) != null) {
            validateResult.addError("phoneNumber", phoneCheckResult);
        }

        String mobileCheckResult;
        if (StringUtils.isNotBlank(athlete.getMobileNumber()) && (mobileCheckResult = ValidateUtils.checkPhone(athlete.getMobileNumber())) != null) {
            validateResult.addError("mobileNumber", mobileCheckResult);
        }

        return !validateResult.hasErrors();
    }

    @Override
    public void initNew(Athlete athlete) {
        if (athlete.getId() == 0) {
            Person person = athlete.getPerson();
            if (StringUtils.isBlank(person.getPassword())) {
                person.setPassword(ValidateUtils.DEFAULT_PASSWORD);
            }
            if (StringUtils.isBlank(person.getLogin())) {
                Person lastPerson = personService.findLastPerson();
                if (lastPerson != null) {
                    person.setLogin("user" + (lastPerson.getId() + 1));
                }
            }
        }
    }
}
