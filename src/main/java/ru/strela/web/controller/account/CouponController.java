package ru.strela.web.controller.account;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.model.filter.payment.CouponFilter;
import ru.strela.model.payment.Coupon;
import ru.strela.util.DateUtils;
import ru.strela.util.ajax.JsonData;
import ru.strela.util.ajax.JsonResponse;
import ru.strela.web.controller.core.WebController;

@Controller
@RequestMapping("/account/coupon")
public class CouponController extends WebController {

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResponse list(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "size", required = false, defaultValue = "50") int pageSize,
                             @RequestParam(value = "query", required = false) String query) {
        JsonResponse response = new JsonResponse();
        JsonData data = response.createJsonData();

        CouponFilter filter = new CouponFilter();
        filter.setQuery(query);
        filter.addOrder(new Order("name", OrderDirection.Asc));
        Page<Coupon> page = paymentService.findCoupons(filter, pageNumber - 1, pageSize);
        for (Coupon c : page) {
            JsonData jsonData = data.createCollection("coupons");

            jsonData.put("id", c.getId());
            jsonData.put("name", c.getName());
            jsonData.put("discountPercent", c.getDiscountPercent());
            jsonData.put("expiration", DateUtils.formatDayMonthYear(c.getExpiration()));
        }

        JsonData pageDate = data.addJsonData("page");
        pageDate.put("number", page.getNumber());
        pageDate.put("totalPages", page.getTotalPages());
        
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public JsonResponse get(@RequestParam(value = "id", required = true, defaultValue = "0") Integer id) {
        JsonResponse response = new JsonResponse();
        JsonData data = response.createJsonData();

        Coupon coupon = paymentService.findById(new Coupon(id));
        if (coupon != null) {
            JsonData c = data.addJsonData("coupon");

            c.put("id", coupon.getId());
            c.put("name", coupon.getName());
            c.put("discountPercent", coupon.getDiscountPercent());
            c.put("expiration", DateUtils.formatDDMMYYYY(coupon.getExpiration()));
        }

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResponse save(Coupon coupon) {
        JsonResponse response = new JsonResponse();

    	if(validate(response, coupon)) {
            if(coupon.getId() != 0) {
                Coupon saved = paymentService.findById(new Coupon(coupon.getId()));
            	
    			saved.setName(coupon.getName());
                saved.setExpiration(coupon.getExpiration());
                saved.setDiscountPercent(coupon.getDiscountPercent());

        		coupon = saved;
            }         
            
            paymentService.save(coupon);
        }

        return response;
    }
    
    @ResponseBody
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    public JsonResponse remove(@PathVariable("id") int id) {
        JsonResponse response = new JsonResponse();
        try {
            paymentService.remove(new Coupon(id));
        } catch(Exception e) {
            response.setStatus("error");
        }
        return response;
    }
    
    private boolean validate(JsonResponse response, Coupon coupon) {
    	if (StringUtils.isBlank(coupon.getName())) {
            response.addFieldMessage("name", FIELD_REQUIRED);
    	}
        if (coupon.getDiscountPercent() == null) {
            response.addFieldMessage("discountPercent", FIELD_REQUIRED);
        } else if (coupon.getDiscountPercent() < 0.0d) {
            response.addFieldMessage("discountPercent", "Значение должно быть больше нуля");
        } else if (coupon.getDiscountPercent() >= 100.0d) {
            response.addFieldMessage("discountPercent", "Значение должно быть меньше 100%");
        }

        return !response.isStatusError();
    }
}
