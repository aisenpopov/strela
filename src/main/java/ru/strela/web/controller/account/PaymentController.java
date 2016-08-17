package ru.strela.web.controller.account;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.strela.model.Athlete;
import ru.strela.model.Gym;
import ru.strela.model.auth.Person;
import ru.strela.model.filter.GymFilter;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.model.filter.payment.PaymentFilter;
import ru.strela.model.filter.payment.TariffFilter;
import ru.strela.model.payment.AthleteTariff;
import ru.strela.model.payment.Payment;
import ru.strela.model.payment.Tariff;
import ru.strela.util.DateUtils;
import ru.strela.util.ajax.JsonData;
import ru.strela.util.ajax.JsonResponse;
import ru.strela.web.controller.core.WebController;

import java.util.*;

@Controller
@RequestMapping("/account/payment")
public class PaymentController extends WebController {

    @ResponseBody
    @RequestMapping(value = "/list/", method = RequestMethod.GET)
    public JsonResponse list(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "size", required = false, defaultValue = "50") int pageSize,
                             @RequestParam(value = "query", required = false) String query) {
        JsonResponse response = new JsonResponse();
        PaymentFilter filter = new PaymentFilter();
        filter.setQuery(query);
        filter.addOrder(new Order("id", OrderDirection.Desc));
        Page<Payment> page = paymentService.findPayments(filter, pageNumber - 1, pageSize);
        List<Map<String, Object>> list = new ArrayList<>();
        response.addData("payments", list);
        for (Payment payment : page) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", payment.getId());
            item.put("athlete", payment.getAthleteTariff().getAthlete().getDisplayName());
            item.put("tariff", payment.getAthleteTariff().getTariff().getName());
            item.put("coupon", payment.getAthleteTariff().getCoupon() != null ? payment.getAthleteTariff().getCoupon().getName() : "");
            item.put("amount", payment.getAmount());
            Athlete operatorAthlete = personService.findByPerson(payment.getOperator());
            item.put("operator", payment.getOperator().getLogin() + (operatorAthlete != null ? (", " + operatorAthlete.getDisplayName()) : ""));
            item.put("date", DateUtils.formatDayMonthYear(payment.getDate()));
            list.add(item);
        }

        Map<String, Object> pageItem = new HashMap<String, Object>();
        response.addData("page", pageItem);
        pageItem.put("number", page.getNumber());
        pageItem.put("totalPages", page.getTotalPages());

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    public JsonResponse remove(@PathVariable("id") int id) {
        JsonResponse response = new JsonResponse();
        try {
            paymentService.remove(new Payment(id));
        } catch(Exception e) {
            response.setStatus("error");
        }
        return response;
    }

    @ResponseBody
    @RequestMapping(value = {"/info"}, method = RequestMethod.POST)
    public JsonResponse getPayment(@RequestParam(value = "id", required = true, defaultValue = "0") Integer id) {
        JsonResponse response = new JsonResponse();

        Payment payment = paymentService.findById(new Payment(id));
        if (payment != null) {
            Map<String, Object> paymentItem = new HashMap<>();
            paymentItem.put("amount", payment.getAmount());

            AthleteTariff athleteTariff = payment.getAthleteTariff();
            Athlete athlete = athleteTariff.getAthlete();
            paymentItem.put("athleteId", athlete.getId());
            paymentItem.put("athleteDisplayName", athlete.getDisplayName());

            Gym gym = athleteTariff.getTariff().getGym();
            paymentItem.put("gymId", gym.getId());
            paymentItem.put("gymName", gym.getName());

            response.setData(paymentItem);
        }

        return response;
    }

    @ResponseBody
    @RequestMapping(value="/checkSingleGym", method=RequestMethod.POST)
    public JsonResponse checkSingleGym() {
        JsonResponse response = new JsonResponse();
        JsonData data = response.createJsonData();

        Person currentPerson = personServer.getCurrentPerson();
        if (currentPerson != null && currentPerson.isInstructor() && !currentPerson.isAdmin()) {
            Athlete athlete = personService.findByPerson(currentPerson);
            if (athlete != null) {
                if (athlete.getTeam() != null) {
                    GymFilter filter = new GymFilter();
                    filter.setTeam(athlete.getTeam());
                    List<Gym> gyms = applicationService.findGyms(filter, true);

                    if (gyms != null && !gyms.isEmpty() && gyms.size() == 1) {
                        Gym gym = gyms.get(0);
                        JsonData jsonData = data.addJsonData("gym");
                        jsonData.put("id", gym.getId());
                        jsonData.put("name", gym.getName());
                    }
                }
            }
        }

        return response;
    }

    @ResponseBody
    @RequestMapping(value = {"/save"}, method = RequestMethod.POST)
    public JsonResponse save(Payment payment) {
        JsonResponse response = new JsonResponse();
        if(validate(response, payment)) {
            AthleteTariff athleteTariff = paymentService.getOrCreateAthleteTariff(payment.getAthleteTariff().getAthlete(), payment.getAthleteTariff().getTariff().getGym());
            payment.setAthleteTariff(athleteTariff);
            payment.setDate(new Date());
            payment.setOperator(personServer.getCurrentPerson());
            if(payment.getId() != 0) {
                Payment saved = paymentService.findById(new Payment(payment.getId()));

                saved.setAmount(payment.getAmount());
                saved.setOperator(payment.getOperator());
                saved.setDate(payment.getDate());

                payment = saved;
            }

            paymentService.save(payment);
        }

        return response;
    }

    private boolean validate(JsonResponse response, Payment payment) {
        if (payment.getAmount() == null) {
            response.addFieldMessage("amount", FIELD_REQUIRED);
        } else if (payment.getAmount() <= 0.0d) {
            response.addFieldMessage("amount", "Введите значение большее нуля");
        }
        Gym gym = payment.getAthleteTariff().getTariff().getGym();
        if (gym == null) {
            response.addFieldMessage("gym", FIELD_REQUIRED);
        } else {
            TariffFilter tariffFilter = new TariffFilter();
            tariffFilter.setGym(gym);
            List<Tariff> tariffs = paymentService.findTariffs(tariffFilter, false);
            if (tariffs.isEmpty()) {
                response.addFieldMessage("gym", "Для данного зала нету тарифов. Создайте тариф.");
            }
        }

        if (payment.getAthleteTariff().getAthlete() == null) {
            response.addFieldMessage("athlete", FIELD_REQUIRED);
        }

        return !response.isStatusError();
    }

}
