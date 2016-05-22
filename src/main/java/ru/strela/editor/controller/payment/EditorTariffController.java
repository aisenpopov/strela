package ru.strela.editor.controller.payment;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.editor.controller.core.EditorController;
import ru.strela.model.payment.Tariff;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.model.filter.payment.TariffFilter;
import ru.strela.util.ModelBuilder;
import ru.strela.util.Redirect;
import ru.strela.util.TextUtils;
import ru.strela.util.ajax.JsonResponse;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/editor/tariff")
public class EditorTariffController extends EditorController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "size", required = false, defaultValue = "50") int pageSize,
                             @ModelAttribute("filter") TariffFilter filter,
                             @PathVariable Map<String, String> pathVariables) {
    	ModelBuilder model = new ModelBuilder("editor/tariffs");
        if (filter == null) {
        	filter = new TariffFilter();
        }
        filter.addOrder(new Order("name", OrderDirection.Asc));
        Page<Tariff> page = paymentService.findTariffs(filter, pageNumber - 1, pageSize);
        model.put("page", page);
        
        return model;
    }

    @RequestMapping(value = {"/edit/{id}", "/edit"}, method = RequestMethod.GET)
    public ModelAndView get(@PathVariable Map<String, String> pathVariables) {
    	ModelAndView modelAndView = getModel(TextUtils.getIntValue(pathVariables.get("id")));
        
        return modelAndView;
    }
    
    @RequestMapping(value = {"/edit", "/edit/{id}"}, method = RequestMethod.POST)
    public ModelAndView save(Tariff tariff, BindingResult result, @PathVariable Map<String, String> pathVariables) {
    	if(validate(result, tariff)) {
            if(tariff.getId() != 0) {
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
            
            tariff = paymentService.save(tariff);
            
            return new Redirect("/editor/tariff/edit/" + tariff.getId() + "/");
        }

        return new ModelAndView("editor/editTariff");
    }
    
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse remove(@PathVariable("id") int id) {
        JsonResponse response = new JsonResponse();
        try {
            paymentService.remove(new Tariff(id));
        } catch(Exception e) {
            response.setStatus("error");
        }
        return response;
    }
    
    private ModelAndView getModel(int id) {
        ModelBuilder model = new ModelBuilder("editor/editTariff");
        Tariff tariff;
        
        if (id == 0) {
        	tariff = new Tariff();
        } else {
        	tariff = paymentService.findById(new Tariff(id));
        }
        model.put("tariff", tariff);
             
		return model;
    }
    
    private boolean validate(BindingResult result, Tariff tariff) {
    	if (StringUtils.isBlank(tariff.getName())) {
    		result.rejectValue("name", "field.required", FIELD_REQUIRED);
    	}
        if (tariff.getGym() == null) {
            result.rejectValue("gym", "field.required", FIELD_REQUIRED);
        } else {
            TariffFilter filter = new TariffFilter();
            filter.setGym(tariff.getGym());
            List<Tariff> tariffs = paymentService.findTariffs(filter, true);
            if (!tariffs.isEmpty() && tariffs.get(0).getId() != tariff.getId()) {
                result.rejectValue("gym", "field.required", "Для данного зала уже имеется тариф");
            }
        }
        Double priceYear = tariff.getPriceYear();
        if (priceYear != null && priceYear <= 0.0d) {
            result.rejectValue("priceYear", "field.required", POSITIVE_NUMBER);
        }
        Double priceHalfYear = tariff.getPriceHalfYear();
        if (priceHalfYear != null && priceHalfYear <= 0.0d) {
            result.rejectValue("priceHalfYear", "field.required", POSITIVE_NUMBER);
        }
        Double priceQuarter = tariff.getPriceQuarter();
        if (priceQuarter != null && priceQuarter <= 0.0d) {
            result.rejectValue("priceQuarter", "field.required", POSITIVE_NUMBER);
        }
        Double priceMonth = tariff.getPriceMonth();
        if (priceMonth != null && priceMonth <= 0.0d) {
            result.rejectValue("priceMonth", "field.required", POSITIVE_NUMBER);
        }
        if (priceYear == null && tariff.getPriceHalfYear() == null
                && tariff.getPriceQuarter() == null && tariff.getPriceMonth() == null) {
            String message = "Заполните хотя бы одно поле";
            result.rejectValue("priceYear", "field.required", message);
            result.rejectValue("priceHalfYear", "field.required", message);
            result.rejectValue("priceQuarter", "field.required", message);
            result.rejectValue("priceMonth", "field.required", message);
        }

        return !result.hasErrors();
    }
}
