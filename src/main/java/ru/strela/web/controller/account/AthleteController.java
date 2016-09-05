package ru.strela.web.controller.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.strela.model.Athlete;
import ru.strela.model.auth.Person;
import ru.strela.model.filter.AthleteFilter;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.model.filter.payment.AthleteTariffFilter;
import ru.strela.model.payment.AthleteTariff;
import ru.strela.service.AthleteService;
import ru.strela.util.ajax.JsonData;
import ru.strela.util.ajax.JsonResponse;
import ru.strela.util.validate.JsonResponseValidateAdapter;
import ru.strela.web.controller.core.WebController;
import ru.strela.web.controller.dto.AthleteDTO;
import ru.strela.web.controller.dto.AthleteTariffDTO;
import ru.strela.web.controller.dto.PersonDTO;

import java.util.List;

@Controller
@RequestMapping("/account/athlete")
public class AthleteController extends WebController {

	@Autowired
	private AthleteService athleteService;

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
		AthleteDTO athleteDTO = null;
		if (athlete != null) {
			athleteDTO = new AthleteDTO(athlete);
		} else {
			athleteDTO = new AthleteDTO();
			athleteDTO.setPerson(new PersonDTO());
			athleteDTO.setSex(Athlete.Sex.male.name());
		}
		data.put("athlete", athleteDTO);

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

		if (athlete.getId() == 0) {
			athleteService.initNew(athlete);
		}
    	if (athleteService.validate(athlete, new JsonResponseValidateAdapter(response))) {
			Athlete savedAthlete = athleteService.save(athlete);
			response.addData("id", savedAthlete.getId());
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
