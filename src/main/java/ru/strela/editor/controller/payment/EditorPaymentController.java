package ru.strela.editor.controller.payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.editor.controller.core.EditorController;
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
import ru.strela.util.ModelBuilder;
import ru.strela.util.Redirect;
import ru.strela.util.TextUtils;
import ru.strela.util.ajax.JsonResponse;
import ru.strela.util.validate.BindingResultValidateAdapter;

import java.util.*;

@Controller
@RequestMapping("/editor/payment")
public class EditorPaymentController extends EditorController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "size", required = false, defaultValue = "50") int pageSize,
                             @ModelAttribute("filter") PaymentFilter filter) {
        ModelBuilder model = new ModelBuilder("editor/payments");
        if (filter == null) {
            filter = new PaymentFilter();
        }
        filter.addOrder(new Order("id", OrderDirection.Desc));
        Page<Payment> page = paymentService.findPayments(filter, pageNumber - 1, pageSize);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Payment payment : page) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("payment", payment);
            item.put("athlete", personService.findByPerson(payment.getOperator()));
            list.add(item);
        }
        model.put("page", new PageImpl<Map<String, Object>>(list));

        return model;
    }

    @RequestMapping(value = {"/edit/{id}", "/edit"}, method = RequestMethod.GET)
    public ModelAndView get(@PathVariable Map<String, String> pathVariables) {
        ModelAndView modelAndView = getModel(TextUtils.getIntValue(pathVariables.get("id")), null);

        return modelAndView;
    }

    @RequestMapping(value = {"/edit", "/edit/{id}"}, method = RequestMethod.POST)
    public ModelAndView save(Payment payment, BindingResult result) {
        if(paymentService.validate(payment, new BindingResultValidateAdapter(result))) {

            Payment savedPayment = paymentService.savePayment(payment);

            return new Redirect("/editor/payment/edit/" + savedPayment.getId() + "/");
        }

        return getModel(null, null);
    }

    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse remove(@PathVariable("id") int id) {
        JsonResponse response = new JsonResponse();
        try {
            paymentService.remove(new Payment(id));
        } catch(Exception e) {
            response.setStatus("error");
        }
        return response;
    }

    private ModelAndView getModel(Integer id, AthleteTariff athleteTariff) {
        ModelBuilder model = new ModelBuilder("editor/editPayment");
        Payment payment;

        if (id != null) {
            if (id == 0) {
                payment = new Payment();
                payment.setDate(new Date());
                Person currentPerson = personServer.getCurrentPerson();
                payment.setOperator(currentPerson);

                if (currentPerson != null && currentPerson.isInstructor() && !currentPerson.isAdmin()) {
                    Athlete athlete = personService.findByPerson(currentPerson);
                    if (athlete != null) {
                        if (athlete.getTeam() != null) {
                            GymFilter filter = new GymFilter();
                            filter.setTeam(athlete.getTeam());
                            List<Gym> gyms = applicationService.findGyms(filter, true);

                            if (gyms != null && !gyms.isEmpty() && gyms.size() == 1) {
                                Tariff newTariff = new Tariff();
                                newTariff.setGym(gyms.get(0));

                                AthleteTariff newAthleteTariff = new AthleteTariff();
                                newAthleteTariff.setTariff(newTariff);

                                payment.setAthleteTariff(newAthleteTariff);
                            }
                        }
                    }
                }
            } else {
                payment = paymentService.findById(new Payment(id));
            }
            model.put("payment", payment);
        }
        if (athleteTariff == null) {
            athleteTariff = new AthleteTariff();
        }
        model.put("athleteTariff", athleteTariff);

        return model;
    }

    private boolean validate(BindingResult result, Payment payment) {
        if (payment.getAmount() == null) {
            result.rejectValue("amount", "field.required", FIELD_REQUIRED);
        } else if (payment.getAmount() <= 0.0d) {
            result.rejectValue("amount", "field.required", "Введите значение большее нуля");
        }
        Gym gym = payment.getAthleteTariff().getTariff().getGym();
        if (gym == null) {
            result.rejectValue("athleteTariff.tariff.gym", "field.required", FIELD_REQUIRED);
        } else {
            TariffFilter tariffFilter = new TariffFilter();
            tariffFilter.setGym(gym);
            List<Tariff> tariffs = paymentService.findTariffs(tariffFilter, false);
            if (tariffs.isEmpty()) {
                result.rejectValue("athleteTariff.tariff.gym", "field.required", "Для данного зала нету тарифов. Создайте тариф.");
            }
        }

        if (payment.getAthleteTariff().getAthlete() == null) {
            result.rejectValue("athleteTariff.athlete", "field.required", FIELD_REQUIRED);
        }

        if (payment.getDate() == null) {
            result.rejectValue("date", "field.required", FIELD_REQUIRED);
        }
        if (payment.getOperator() == null) {
            result.rejectValue("operator", "field.required", FIELD_REQUIRED);
        }

        return !result.hasErrors();
    }

}
