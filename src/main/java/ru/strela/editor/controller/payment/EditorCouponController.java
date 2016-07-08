package ru.strela.editor.controller.payment;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.editor.controller.core.EditorController;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.model.filter.payment.CouponFilter;
import ru.strela.model.payment.Coupon;
import ru.strela.util.ModelBuilder;
import ru.strela.util.Redirect;
import ru.strela.util.TextUtils;
import ru.strela.util.ajax.JsonResponse;

import java.util.Map;

@Controller
@RequestMapping("/editor/coupon")
public class EditorCouponController extends EditorController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "size", required = false, defaultValue = "50") int pageSize,
                             @ModelAttribute("filter") CouponFilter filter) {
    	ModelBuilder model = new ModelBuilder("editor/coupons");
        if (filter == null) {
        	filter = new CouponFilter();
        }
        filter.addOrder(new Order("name", OrderDirection.Asc));
        Page<Coupon> page = paymentService.findCoupons(filter, pageNumber - 1, pageSize);
        model.put("page", page);
        
        return model;
    }

    @RequestMapping(value = {"/edit/{id}", "/edit"}, method = RequestMethod.GET)
    public ModelAndView get(@PathVariable Map<String, String> pathVariables) {
    	ModelAndView modelAndView = getModel(TextUtils.getIntValue(pathVariables.get("id")));
        
        return modelAndView;
    }
    
    @RequestMapping(value = {"/edit", "/edit/{id}"}, method = RequestMethod.POST)
    public ModelAndView save(Coupon coupon, BindingResult result, @PathVariable Map<String, String> pathVariables) {
    	if(validate(result, coupon)) {
            if(coupon.getId() != 0) {
                Coupon saved = paymentService.findById(new Coupon(coupon.getId()));
            	
    			saved.setName(coupon.getName());
                saved.setExpiration(coupon.getExpiration());
                saved.setDiscountPercent(coupon.getDiscountPercent());

        		coupon = saved;
            }         
            
            coupon = paymentService.save(coupon);
            
            return new Redirect("/editor/coupon/edit/" + coupon.getId() + "/");
        }

        return new ModelAndView("editor/editCoupon");
    }
    
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse remove(@PathVariable("id") int id) {
        JsonResponse response = new JsonResponse();
        try {
            paymentService.remove(new Coupon(id));
        } catch(Exception e) {
            response.setStatus("error");
        }
        return response;
    }
    
    private ModelAndView getModel(int id) {
        ModelBuilder model = new ModelBuilder("editor/editCoupon");
        Coupon coupon;
        
        if (id == 0) {
        	coupon = new Coupon();
        } else {
        	coupon = paymentService.findById(new Coupon(id));
        }
        model.put("coupon", coupon);
             
		return model;
    }
    
    private boolean validate(BindingResult result, Coupon coupon) {
    	if (StringUtils.isBlank(coupon.getName())) {
    		result.rejectValue("name", "field.required", FIELD_REQUIRED);
    	}
        if (coupon.getDiscountPercent() == null) {
            result.rejectValue("discountPercent", "field.required", FIELD_REQUIRED);
        } else if (coupon.getDiscountPercent() < 0.0d) {
            result.rejectValue("discountPercent", "field.required", "Значение должно быть больше нуля");
        } else if (coupon.getDiscountPercent() >= 100.0d) {
            result.rejectValue("discountPercent", "field.required", "Значение должно быть меньше 100%");
        }

        return !result.hasErrors();
    }
}
