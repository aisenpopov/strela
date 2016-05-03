package ru.strela.editor.controller.payment;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.editor.controller.core.EditorController;
import ru.strela.model.Athlete;
import ru.strela.model.Gym;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.model.filter.payment.PaymentStatusFilter;
import ru.strela.model.payment.PaymentStatus;
import ru.strela.util.ModelBuilder;
import ru.strela.util.Redirect;
import ru.strela.util.TextUtils;
import ru.strela.util.ajax.JsonResponse;

import java.util.List;
import java.util.Map;

/**
 * Created by Aisen on 30.04.2016.
 */

@Controller
@RequestMapping("/editor/payed_status")
public class EditorPaymentStatusController extends EditorController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "size", required = false, defaultValue = "50") int pageSize,
                             @ModelAttribute("filter") PaymentStatusFilter filter,
                             @PathVariable Map<String, String> pathVariables) {
        ModelBuilder model = new ModelBuilder("editor/paymentStatuses");
        if(filter == null) {
            filter = new PaymentStatusFilter();
        }
        filter.addOrder(new Order("payedTill", OrderDirection.Desc));
        Page<PaymentStatus> page = paymentService.findPaymentStatuses(filter, pageNumber - 1, pageSize);
        model.put("page", page);

        return model;
    }

    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse remove(@PathVariable("id") int id) {
        JsonResponse response = new JsonResponse();
        try {
            paymentService.remove(new PaymentStatus(id));
        } catch(Exception e) {
            response.setStatus("error");
        }
        return response;
    }

    @RequestMapping(value = {"/edit/{id}", "/edit"}, method = RequestMethod.GET)
    public ModelAndView get(@PathVariable Map<String, String> pathVariables) {
        ModelAndView modelAndView = getModel(TextUtils.getIntValue(pathVariables.get("id")));

        return modelAndView;
    }

    @RequestMapping(value = {"/edit", "/edit/{id}"}, method = RequestMethod.POST)
    public ModelAndView save(PaymentStatus paymentStatus, BindingResult result, @PathVariable Map<String, String> pathVariables) {
        if(validate(result, paymentStatus)) {
            if(paymentStatus.getId() != 0) {
                PaymentStatus saved = paymentService.findById(new PaymentStatus(paymentStatus.getId()));

                saved.setAthlete(paymentStatus.getAthlete());
                saved.setGym(paymentStatus.getGym());
                saved.setPayedTill(paymentStatus.getPayedTill());

                paymentStatus = saved;
            }

            paymentStatus = paymentService.save(paymentStatus);

            return new Redirect("/editor/payed_status/edit/" + paymentStatus.getId() + "/");
        }

        return new ModelAndView("editor/editPaymentStatus");
    }

    private ModelAndView getModel(int id) {
        ModelBuilder model = new ModelBuilder("editor/editPaymentStatus");
        PaymentStatus paymentStatus;

        if (id == 0) {
            paymentStatus = new PaymentStatus();
        } else {
            paymentStatus = paymentService.findById(new PaymentStatus(id));
        }
        model.put("paymentStatus", paymentStatus);

        return model;
    }

    private boolean validate(BindingResult result, PaymentStatus paymentStatus) {
        Gym gym = paymentStatus.getGym();
        if (gym == null) {
            result.rejectValue("gym", "field.required", FIELD_REQUIRED);
        }
        Athlete athlete = paymentStatus.getAthlete();
        if (athlete == null) {
            result.rejectValue("athlete", "field.required", FIELD_REQUIRED);
        }
        if (paymentStatus.getPayedTill() == null) {
            result.rejectValue("payedTill", "field.required", FIELD_REQUIRED);
        }
        if (paymentStatus != null && athlete != null) {
            PaymentStatusFilter filter = new PaymentStatusFilter();
            filter.setAthlete(athlete);
            filter.setGym(gym);
            List<PaymentStatus> paymentStatuses = paymentService.findPaymentStatuses(filter);
            if(!paymentStatuses.isEmpty() && paymentStatuses.get(0).getId() != paymentStatus.getId()) {
                result.rejectValue("athlete", "field.required", "Запись с таким атлетом и залом уже существует");
                result.rejectValue("gym", "field.required", "Запись с таким атлетом и залом уже существует");
            }
        }

        return !result.hasErrors();
    }

}
