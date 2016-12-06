package ru.strela.web.controller.account;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.strela.model.Athlete;
import ru.strela.model.Gym;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.model.filter.payment.PaymentStatusFilter;
import ru.strela.model.payment.PaymentStatus;
import ru.strela.util.DateUtils;
import ru.strela.util.ajax.JsonData;
import ru.strela.util.ajax.JsonResponse;
import ru.strela.web.controller.core.WebController;

import java.util.List;

@Controller
@RequestMapping("/account/payment_status")
public class PaymentStatusController extends WebController {

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResponse list(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "size", required = false, defaultValue = "50") int pageSize,
                             @RequestParam(value = "query", required = false) String query) {
        JsonResponse response = new JsonResponse();
        JsonData data = response.createJsonData();

        PaymentStatusFilter filter = new PaymentStatusFilter();
        filter.setQuery(query);
        filter.addOrder(new Order("payedTill", OrderDirection.Desc));
        Page<PaymentStatus> page = paymentService.findPaymentStatuses(filter, pageNumber - 1, pageSize);
        for (PaymentStatus paymentStatus : page) {
            JsonData jsonData = data.createCollection("paymentStatuses");
            jsonData.put("id", paymentStatus.getId());
            jsonData.put("athlete", paymentStatus.getAthlete().getDisplayName());
            jsonData.put("gym", paymentStatus.getGym().getName());
            jsonData.put("payedTill", DateUtils.formatDayMonthYear(paymentStatus.getPayedTill()));
        }

        JsonData pageDate = data.addJsonData("page");
        pageDate.put("number", page.getNumber());
        pageDate.put("totalPages", page.getTotalPages());

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    public JsonResponse remove(@PathVariable("id") int id) {
        JsonResponse response = new JsonResponse();
        try {
            paymentService.remove(new PaymentStatus(id));
        } catch(Exception e) {
            response.setStatus("error");
        }
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public JsonResponse get(@RequestParam(value = "id", required = true, defaultValue = "0") Integer id) {
        JsonResponse response = new JsonResponse();
        JsonData data = response.createJsonData();

        PaymentStatus paymentStatus = paymentService.findById(new PaymentStatus(id));
        if (paymentStatus != null) {
            JsonData jsonData = data.addJsonData("paymentStatus");

            Athlete athlete = paymentStatus.getAthlete();
            jsonData.put("athleteId", athlete.getId());
            jsonData.put("athleteDisplayName", athlete.getDisplayName());

            Gym gym = paymentStatus.getGym();
            jsonData.put("gymId", gym.getId());
            jsonData.put("gymName", gym.getName());

            jsonData.put("payedTill", DateUtils.formatDDMMYYYY(paymentStatus.getPayedTill()));
        }

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResponse save(PaymentStatus paymentStatus) {
        JsonResponse response = new JsonResponse();
        if(validate(response, paymentStatus)) {
            if(paymentStatus.getId() != 0) {
                PaymentStatus saved = paymentService.findById(new PaymentStatus(paymentStatus.getId()));

                saved.setAthlete(paymentStatus.getAthlete());
                saved.setGym(paymentStatus.getGym());
                saved.setPayedTill(paymentStatus.getPayedTill());

                paymentStatus = saved;
            }

            paymentService.save(paymentStatus);
        }

        return response;
    }

    private boolean validate(JsonResponse response, PaymentStatus paymentStatus) {
        Gym gym = paymentStatus.getGym();
        if (gym == null) {
            response.addFieldMessage("gym", FIELD_REQUIRED);
        }
        Athlete athlete = paymentStatus.getAthlete();
        if (athlete == null) {
            response.addFieldMessage("athlete", FIELD_REQUIRED);
        }
        if (paymentStatus.getPayedTill() == null) {
            response.addFieldMessage("payedTill", FIELD_REQUIRED);
        }
        if (gym != null && athlete != null) {
            PaymentStatusFilter filter = new PaymentStatusFilter();
            filter.setAthlete(athlete);
            filter.setGym(gym);
            List<PaymentStatus> paymentStatuses = paymentService.findPaymentStatuses(filter, true);
            if(!paymentStatuses.isEmpty() && paymentStatuses.get(0).getId() != paymentStatus.getId()) {
                response.addFieldMessage("athlete", "Запись с таким атлетом и залом уже существует");
                response.addFieldMessage("gym", "Запись с таким атлетом и залом уже существует");
            }
        }

        return !response.isStatusError();
    }

}
