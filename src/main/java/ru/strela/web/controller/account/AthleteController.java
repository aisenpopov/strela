package ru.strela.web.controller.account;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.strela.model.Athlete;
import ru.strela.model.auth.Person;
import ru.strela.model.filter.AthleteFilter;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.model.filter.payment.AthleteTariffFilter;
import ru.strela.model.payment.AthleteTariff;
import ru.strela.util.ValidateUtils;
import ru.strela.util.ajax.JsonData;
import ru.strela.util.ajax.JsonResponse;
import ru.strela.web.controller.core.WebController;
import ru.strela.web.controller.dto.AthleteDTO;
import ru.strela.web.controller.dto.AthleteTariffDTO;

import java.util.List;

@Controller
@RequestMapping("/account/athlete")
public class AthleteController extends WebController {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResponse list(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "size", required = false, defaultValue = "50") int pageSize,
							 @RequestParam(value = "query", required = false) String query) {
		JsonResponse response = new JsonResponse();
		JsonData data = response.createJsonData();

		AthleteFilter filter = new AthleteFilter();
		filter.setQuery(query);
		if (filter.getOrders() == null || filter.getOrders().isEmpty()) {
			filter.addOrder(new Order("id", OrderDirection.Desc));
		}

        Page<Athlete> page = personService.findAthletes(filter, pageNumber - 1, pageSize);
		for (Athlete a : page) {
			Person person = a.getPerson();
			JsonData jsonData = data.createCollection("athletes");
			jsonData.put("id", a.getId());
			jsonData.put("displayName", a.getDisplayName());
			jsonData.put("person", person.getLogin());
			jsonData.put("instructor", person.isInstructor());
			jsonData.put("admin", person.isAdmin());
			jsonData.put("disabled", person.isDisabled());
			jsonData.put("team", a.getTeam() != null ? a.getTeam().getName() : "");
			jsonData.put("registrationRegion", a.getRegistrationRegion() != null ? a.getRegistrationRegion().getName() : "");
		}

		fillPage(data, page);
        
        return response;
    }

	@ResponseBody
	@RequestMapping(value = "/getAthlete", method = RequestMethod.POST)
	public JsonResponse getAthlete(@RequestParam(value = "id", required = true, defaultValue = "0") Integer id) {
		JsonResponse response = new JsonResponse();
		JsonData data = response.createJsonData();

		Athlete athlete = personService.findById(new Athlete(id));
		if (athlete != null) {
			data.put("athlete", new AthleteDTO(athlete));
		}

		return response;
	}

	@ResponseBody
	@RequestMapping(value = "/getAthleteTariffs", method = RequestMethod.POST)
	public JsonResponse getAthleteTariffs(@RequestParam(value = "id", required = true, defaultValue = "0") Integer id) {
		JsonResponse response = new JsonResponse();
		JsonData data = response.createJsonData();

		Athlete athlete = personService.findById(new Athlete(id));
		if (athlete != null) {
			AthleteTariffFilter filter = new AthleteTariffFilter();
			filter.setAthlete(athlete);
			filter.addOrder(new Order("id", OrderDirection.Asc));
			for (AthleteTariff athleteTariff : paymentService.findAthleteTariffs(filter, false)) {
				data.addCollectionItem("athleteTariffs", new AthleteTariffDTO(athleteTariff));
			}
		}

		return response;
	}

	@ResponseBody
	@RequestMapping(value = "/getAthleteTariff", method = RequestMethod.POST)
	public JsonResponse getAthleteTariff(@RequestParam(value = "id", required = true, defaultValue = "0") Integer id) {
		JsonResponse response = new JsonResponse();
		JsonData data = response.createJsonData();

		AthleteTariff athleteTariff = paymentService.findById(new AthleteTariff(id));
		if (athleteTariff != null) {
			data.put("athleteTariff", new AthleteTariffDTO(athleteTariff));
		}

		return response;
	}

	@ResponseBody
	@RequestMapping(value = "/saveAthleteTariff", method = RequestMethod.POST)
	public JsonResponse saveAthleteTariff(AthleteTariff athleteTariff) {
		JsonResponse response = new JsonResponse();

		if (validate(response, athleteTariff)) {
			if (athleteTariff.getId() > 0) {
				AthleteTariff exist = paymentService.findById(new AthleteTariff(athleteTariff.getId()));

				exist.setTariff(athleteTariff.getTariff());
				exist.setCoupon(athleteTariff.getCoupon());

				athleteTariff = exist;
			}

			paymentService.save(athleteTariff);
		}


		return response;
	}

	@ResponseBody
    @RequestMapping(value = "/saveAthlete", method = RequestMethod.POST)
    public JsonResponse save(Athlete athlete) {
		JsonResponse response = new JsonResponse();
    	if (validate(response, athlete)) {
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
			athlete = personService.save(athlete);

			response.addData("id", athlete.getId());
        }

		return response;
    }

    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse remove(@PathVariable("id") int id) {
        JsonResponse response = new JsonResponse();

		Athlete athlete = personService.findById(new Athlete(id));
		if (athlete != null) {
			String checkResult = personService.checkRemove(athlete);
			if (checkResult != null) {
				response.setErrorMessage(checkResult);
			} else {
				try {
					personService.remove(athlete);
				} catch(Exception e) {
					e.printStackTrace();
					response.setErrorMessage(e.getMessage());
				}
			}
		} else {
			response.setErrorMessage("Атлет с id: " + id + " не найден!");
		}

        return response;
    }

	@ResponseBody
	@RequestMapping(value = "/checkRemoveAthleteTariff", method = RequestMethod.POST)
	public JsonResponse checkRemoveAthleteTariff(@RequestParam(value = "athleteTariffId") int athleteTariffId) {
		JsonResponse response = new JsonResponse();

		AthleteTariff athleteTariff = paymentService.findById(new AthleteTariff(athleteTariffId));
		String checkResult = paymentService.checkRemove(athleteTariff);
		if (checkResult != null) {
			response.setErrorMessage(checkResult);
		}

		return response;
	}

	@ResponseBody
	@RequestMapping(value = "/removeAthleteTariff", method = RequestMethod.POST)
	public JsonResponse removeAthleteTariff(@RequestParam(value = "athleteTariffId") int athleteTariffId) {
		JsonResponse response = new JsonResponse();

		AthleteTariff athleteTariff = paymentService.findById(new AthleteTariff(athleteTariffId));
		try {
			paymentService.remove(athleteTariff);
		} catch (Exception e) {
			e.printStackTrace();
			response.setErrorMessage(e.getMessage());
		}

		return response;
	}
    
    private boolean validate(JsonResponse response, Athlete athlete) {
		Person person = athlete.getPerson();
		String loginCheckResult;
		if (person == null || StringUtils.isBlank(person.getLogin())) {
			response.addFieldMessage("personLogin", FIELD_REQUIRED);
		} else if ((loginCheckResult = ValidateUtils.checkLogin(person.getLogin())) != null) {
			response.addFieldMessage("personLogin", loginCheckResult);
		} else {
			Person saved = personService.findByLogin(person);
			if(saved != null && person.getId() != saved.getId()) {
				response.addFieldMessage("personLogin", "Пользователь с таким login-ом уже существует");
			}
		}
		if (athlete.isInstructor() && athlete.getTeam() == null) {
			response.addFieldMessage("team", "Для инструктора необходимо выбрать команду");
		}
		String passwordCheckResult;
		if ((athlete.getId() == 0 || (athlete.getId() > 0 && StringUtils.isNotBlank(person.getPassword()))) && (passwordCheckResult = ValidateUtils.checkPassword(person.getPassword(), person.getLogin())) != null) {
			response.addFieldMessage("personPassword", passwordCheckResult);
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

	private boolean validate(JsonResponse response, AthleteTariff athleteTariff) {
		if (athleteTariff.getAthlete() == null) {
			response.addFieldMessage("athlete", FIELD_REQUIRED);
		}
		if (athleteTariff.getTariff() == null) {
			response.addFieldMessage("tariff", FIELD_REQUIRED);
		} else if(athleteTariff.getAthlete() != null) {
			AthleteTariffFilter filter = new AthleteTariffFilter();
			filter.setAthlete(athleteTariff.getAthlete());
			filter.setTariff(athleteTariff.getTariff());
			List<AthleteTariff> athleteTariffs = paymentService.findAthleteTariffs(filter, true);
			if (!athleteTariffs.isEmpty() && athleteTariffs.get(0).getId() != athleteTariff.getId()) {
				response.addFieldMessage("tariff", "Для данного пользователя уже создан такой тариф");
			}
		}

		return !response.isStatusError();
	}
    
}
