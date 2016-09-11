package ru.strela.web.controller.account;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.model.filter.payment.TariffFilter;
import ru.strela.model.payment.Tariff;
import ru.strela.util.DateUtils;
import ru.strela.util.ajax.JsonData;
import ru.strela.util.ajax.JsonResponse;
import ru.strela.web.controller.core.WebController;

import java.util.List;

@Controller
@RequestMapping("/account/tariff")
public class TariffController extends WebController {

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResponse list(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "size", required = false, defaultValue = "50") int pageSize,
                             @RequestParam(value = "query", required = false) String query) {
        JsonResponse response = new JsonResponse();
        JsonData data = response.createJsonData();

        TariffFilter filter = new TariffFilter();
        filter.setQuery(query);
        filter.addOrder(new Order("name", OrderDirection.Asc));
        Page<Tariff> page = paymentService.findTariffs(filter, pageNumber - 1, pageSize);
        for (Tariff t : page) {
            JsonData jsonData = data.createCollection("tariffs");

            jsonData.put("id", t.getId());
            jsonData.put("name", t.getName());
            jsonData.put("gym", t.getGym().getName());
            jsonData.put("priceMonth", t.getPriceMonth());
            jsonData.put("priceQuarter", t.getPriceQuarter());
            jsonData.put("priceHalfYear", t.getPriceHalfYear());
            jsonData.put("priceYear", t.getPriceYear());
            jsonData.put("expiration", DateUtils.formatDayMonthYear(t.getExpiration()));
        }

        fillPage(data, page);

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public JsonResponse get(@RequestParam(value = "id", required = true, defaultValue = "0") Integer id) {
        JsonResponse response = new JsonResponse();
        JsonData data = response.createJsonData();

        Tariff tariff = paymentService.findById(new Tariff(id));
        if (tariff != null) {
            JsonData jsonData = data.addJsonData("tariff");

            jsonData.put("id", tariff.getId());
            jsonData.put("name", tariff.getName());
            jsonData.put("gymId", tariff.getGym().getId());
            jsonData.put("gymName", tariff.getGym().getName());
            jsonData.put("expiration", DateUtils.formatDDMMYYYY(tariff.getExpiration()));
            jsonData.put("priceMonth", tariff.getPriceMonth());
            jsonData.put("priceQuarter", tariff.getPriceQuarter());
            jsonData.put("priceHalfYear", tariff.getPriceHalfYear());
            jsonData.put("priceYear", tariff.getPriceYear());
        }
        
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResponse save(Tariff tariff) {
        JsonResponse response = new JsonResponse();

    	if (validate(response, tariff)) {
            if (tariff.getId() != 0) {
                Tariff saved = paymentService.findById(new Tariff(tariff.getId()));
            	
    			saved.setName(tariff.getName());
                saved.setGym(tariff.getGym());
                saved.setExpiration(tariff.getExpiration());
                saved.setPriceOnce(tariff.getPriceOnce());
                saved.setPriceMonth(tariff.getPriceMonth());
                saved.setPriceQuarter(tariff.getPriceQuarter());
                saved.setPriceHalfYear(tariff.getPriceHalfYear());
                saved.setPriceYear(tariff.getPriceYear());

        		tariff = saved;
            }         
            
            paymentService.save(tariff);
        }

        return response;
    }
    
    @ResponseBody
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    public JsonResponse remove(@PathVariable("id") int id) {
        JsonResponse response = new JsonResponse();
        try {
            paymentService.remove(new Tariff(id));
        } catch(Exception e) {
            response.setStatus("error");
        }
        return response;
    }
    
    private boolean validate(JsonResponse response, Tariff tariff) {
    	if (StringUtils.isBlank(tariff.getName())) {
            response.addFieldMessage("name", FIELD_REQUIRED);
    	}
        if (tariff.getGym() == null) {
            response.addFieldMessage("gym", FIELD_REQUIRED);
        } else {
            TariffFilter filter = new TariffFilter();
            filter.setGym(tariff.getGym());
            List<Tariff> tariffs = paymentService.findTariffs(filter, true);
            if (!tariffs.isEmpty() && tariffs.get(0).getId() != tariff.getId()) {
                response.addFieldMessage("gym", "Для данного зала уже имеется тариф");
            }
        }
        Double priceYear = tariff.getPriceYear();
        if (priceYear != null && priceYear <= 0.0d) {
            response.addFieldMessage("priceYear", POSITIVE_NUMBER);
        }
        Double priceHalfYear = tariff.getPriceHalfYear();
        if (priceHalfYear != null && priceHalfYear <= 0.0d) {
            response.addFieldMessage("priceHalfYear", POSITIVE_NUMBER);
        }
        Double priceQuarter = tariff.getPriceQuarter();
        if (priceQuarter != null && priceQuarter <= 0.0d) {
            response.addFieldMessage("priceQuarter", POSITIVE_NUMBER);
        }
        Double priceMonth = tariff.getPriceMonth();
        if (priceMonth != null && priceMonth <= 0.0d) {
            response.addFieldMessage("priceMonth", POSITIVE_NUMBER);
        }
        if (priceYear == null && tariff.getPriceHalfYear() == null
                && tariff.getPriceQuarter() == null && tariff.getPriceMonth() == null) {
            String message = "Заполните хотя бы одно поле";
            response.addFieldMessage("priceYear", message);
            response.addFieldMessage("priceHalfYear", message);
            response.addFieldMessage("priceQuarter", message);
            response.addFieldMessage("priceMonth", message);
        }

        return !response.isStatusError();
    }
}
