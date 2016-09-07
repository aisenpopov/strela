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
import ru.strela.model.payment.Payment;
import ru.strela.model.payment.Tariff;
import ru.strela.util.DateUtils;
import ru.strela.util.ajax.JsonData;
import ru.strela.util.ajax.JsonResponse;
import ru.strela.util.validate.JsonResponseValidateAdapter;
import ru.strela.web.controller.core.WebController;
import ru.strela.web.controller.dto.GymDTO;
import ru.strela.web.controller.dto.PaymentDTO;
import ru.strela.web.controller.dto.TariffDTO;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/account/payment")
public class PaymentController extends WebController {

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResponse list(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "size", required = false, defaultValue = "50") int pageSize,
                             @RequestParam(value = "query", required = false) String query) {
        JsonResponse response = new JsonResponse();
        JsonData data = response.createJsonData();

        PaymentFilter filter = new PaymentFilter();
        filter.setQuery(query);
        filter.addOrder(new Order("id", OrderDirection.Desc));
        Page<Payment> page = paymentService.findPayments(filter, pageNumber - 1, pageSize);
        for (Payment payment : page) {
            PaymentDTO paymentDTO = new PaymentDTO(payment);
            paymentDTO.setDate(DateUtils.formatDayMonthYear(payment.getDate()));
            Athlete operatorAthlete = personService.findByPerson(payment.getOperator());
            paymentDTO.getOperator().setLogin(payment.getOperator().getLogin() + (operatorAthlete != null ? (", " + operatorAthlete.getDisplayName()) : ""));
            data.addCollectionItem("payments", paymentDTO);
        }

        fillPage(data, page);

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
    @RequestMapping(value = "/getPayment", method = RequestMethod.POST)
    public JsonResponse getPayment(@RequestParam(value = "id", required = true, defaultValue = "0") Integer id) {
        JsonResponse response = new JsonResponse();
        JsonData data = response.createJsonData();

        Payment payment = paymentService.findById(new Payment(id));
        if (payment != null) {
            data.put("payment", new PaymentDTO(payment));
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
                        data.put("gym", new GymDTO(gym));
                    }
                }
            }
        }

        return response;
    }

    @ResponseBody
    @RequestMapping(value="/getTariff", method=RequestMethod.POST)
    public JsonResponse getTariff(@RequestParam(value = "id", required = true, defaultValue = "0") Integer id) {
        JsonResponse response = new JsonResponse();
        JsonData data = response.createJsonData();

        Gym gym = applicationService.findById(new Gym(id));
        TariffFilter tariffFilter = new TariffFilter();
        tariffFilter.setGym(gym);
        List<Tariff> tariffs = paymentService.findTariffs(tariffFilter, false);
        if (!tariffs.isEmpty()) {
            Tariff tariff = tariffs.get(0);
            data.put("tariff", new TariffDTO(tariff));
        }

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResponse save(Payment payment) {
        JsonResponse response = new JsonResponse();

        payment.setDate(new Date());
        payment.setOperator(personServer.getCurrentPerson());
        if(paymentService.validate(payment, new JsonResponseValidateAdapter(response))) {

            paymentService.savePayment(payment);

        }

        return response;
    }

}
