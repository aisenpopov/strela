package ru.strela.editor.controller.payment;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.editor.controller.core.EditorController;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.model.filter.payment.PaymentFilter;
import ru.strela.model.payment.AthleteTariff;
import ru.strela.model.payment.Payment;
import ru.strela.util.ModelBuilder;
import ru.strela.util.Redirect;
import ru.strela.util.TextUtils;
import ru.strela.util.ajax.JsonResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * Created by Aisen on 30.04.2016.
 */

@Controller
@RequestMapping("/editor/payment")
public class EditorPaymentController extends EditorController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "size", required = false, defaultValue = "50") int pageSize,
                             @ModelAttribute("filter") PaymentFilter filter,
                             @PathVariable Map<String, String> pathVariables) {
        ModelBuilder model = new ModelBuilder("editor/payments");
        if(filter == null) {
            filter = new PaymentFilter();
        }
        filter.addOrder(new Order("date", OrderDirection.Desc));
        Page<Payment> page = paymentService.findPayments(filter, pageNumber - 1, pageSize);
        model.put("page", page);

        return model;
    }

    @RequestMapping(value = {"/edit/{id}", "/edit"}, method = RequestMethod.GET)
    public ModelAndView get(@PathVariable Map<String, String> pathVariables) {
        ModelAndView modelAndView = getModel(TextUtils.getIntValue(pathVariables.get("id")), null);

        return modelAndView;
    }

    @RequestMapping(value = {"/edit", "/edit/{id}"}, method = RequestMethod.POST)
    public ModelAndView save(Payment payment, BindingResult result, @PathVariable Map<String, String> pathVariables) {
        if(validate(result, payment)) {
            if(payment.getId() != 0) {
                Payment saved = paymentService.findById(new Payment(payment.getId()));

                saved.setAthleteTariff(payment.getAthleteTariff());
                saved.setAmount(payment.getAmount());
                saved.setOperator(payment.getOperator());
                saved.setDate(payment.getDate());

                payment = saved;
            }

            payment = paymentService.save(payment);

            return new Redirect("/editor/payment/edit/" + payment.getId() + "/");
        }

        return getModel(null, null);
    }

    @RequestMapping(value={"/edit/ajax/save/", "/edit/{idd}/ajax/save/"}, method=RequestMethod.POST)
    public ModelAndView saveAthleteTariff(HttpServletRequest req,
                                          HttpServletResponse res,
                                          @ModelAttribute("athleteTariff") AthleteTariff athleteTariff,
                                          BindingResult result) {
        if (validate(result, athleteTariff)) {
            if (athleteTariff.getId() > 0) {
                AthleteTariff exist = paymentService.findById(new AthleteTariff(athleteTariff.getId()));

                exist.setTariff(athleteTariff.getTariff());
                exist.setCoupon(athleteTariff.getCoupon());

                athleteTariff = exist;
            }

            AthleteTariff saved = paymentService.save(athleteTariff);

            ajaxUpdate(req, res, "athleteTariffForm");

            return getModel(0, null);
        } else {
            ajaxUpdate(req, res, "athleteTariffForm");

            return getModel(0, athleteTariff);
        }
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
                payment.setOperator(personServer.getCurrentPerson());
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
        if (payment.getAthleteTariff() == null || payment.getAthleteTariff().getId() == 0) {
            result.rejectValue("athleteTariff", "field.required", FIELD_REQUIRED);
        }
        if (payment.getDate() == null) {
            result.rejectValue("date", "field.required", FIELD_REQUIRED);
        }
        if (payment.getOperator() == null) {
            result.rejectValue("operator", "field.required", FIELD_REQUIRED);
        }

        return !result.hasErrors();
    }

    private boolean validate(BindingResult result, AthleteTariff athleteTariff) {
        if (athleteTariff.getAthlete() == null) {
            result.rejectValue("athlete", "field.required", FIELD_REQUIRED);
        }
        if (athleteTariff.getTariff() == null) {
            result.rejectValue("tariff", "field.required", FIELD_REQUIRED);
        }

        return !result.hasErrors();
    }

}
