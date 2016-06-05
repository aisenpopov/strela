package ru.strela.editor.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.editor.controller.core.EditorController;
import ru.strela.model.BannerImage;
import ru.strela.model.filter.BannerImageFilter;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.util.ModelBuilder;
import ru.strela.util.Redirect;
import ru.strela.util.TextUtils;
import ru.strela.util.ajax.JsonResponse;
import ru.strela.util.image.FileDataSource;
import ru.strela.util.image.ImageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/editor/banner_image/{typeName}")
public class EditorBannerImageController extends EditorController {

    @RequestMapping(value = {"/", "/ajax"}, method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView list(HttpServletRequest req, HttpServletResponse res, @PathVariable Map<String, String> pathVariables,
                             @RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "size", required = false, defaultValue = "50") int pageSize,
                             @RequestParam(value = "id", required = false) Integer id,
                             @ModelAttribute("filter") BannerImageFilter filter) {
    	BannerImage.Type type = BannerImage.Type.valueOf(pathVariables.get("typeName"));
        String action = req.getParameter("action");
        
        if (id != null) {
        	if("sort-down".equals(action)) {
    			BannerImage bannerImage = applicationService.findById(new BannerImage(id));
    			applicationService.sortBannerImage(type, bannerImage, false);
    		} else if("sort-up".equals(action)) {
                BannerImage bannerImage = applicationService.findById(new BannerImage(id));
    			applicationService.sortBannerImage(type, bannerImage, true);
    		}
        	ajaxUpdate(req, res, "list");
        }

        ModelAndView modelAndView = new ModelAndView("editor/bannerImages");
        if(filter == null) {        	
        	filter = new BannerImageFilter();
        }
        filter.setType(type);
        filter.addOrder(new Order("position", OrderDirection.Asc));
        
        Page<BannerImage> page = applicationService.findBannerImages(filter, pageNumber - 1, pageSize);
        modelAndView.addObject("page", page);

        return modelAndView;
    }

    @RequestMapping(value = {"/edit/{id}", "/edit"}, method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable Map<String, String> pathVariables) {
    	ModelAndView modelAndView = getModel(TextUtils.getIntValue(pathVariables.get("id")),
    											BannerImage.Type.getByName(pathVariables.get("typeName")));

        return modelAndView;
    }

    @RequestMapping(value = {"/edit", "/edit/{id}"}, method = RequestMethod.POST)
    public ModelAndView save(BannerImage bannerImage, BindingResult result, @PathVariable Map<String, String> pathVariables) {
        if (validate(result, bannerImage)) {
            if(bannerImage.getId() != 0) {
                BannerImage saved = applicationService.findById(new BannerImage(bannerImage.getId()));
            	saved.setType(bannerImage.getType());
                saved.setName(bannerImage.getName());
                saved.setLink(bannerImage.getLink());
                saved.setVisible(bannerImage.isVisible());

            	bannerImage = saved;
            }
            bannerImage = applicationService.save(bannerImage);

            return new Redirect("/editor/banner_image/" + pathVariables.get("typeName") + "/edit/" + bannerImage.getId() + "/");
        }

        return new ModelAndView("editor/editBannerImage");
    }

    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse remove(@PathVariable("id") int id) {
        JsonResponse response = new JsonResponse();
        try {
            BannerImage bannerImage = applicationService.findById(new BannerImage(id));
            applicationService.remove(bannerImage);
        } catch(Exception e) {
            response.setStatus("error");
        }
        return response;
    }

    @RequestMapping(value="/edit/{id}/ajax", method=RequestMethod.POST)
	public ModelAndView onAjax(HttpServletRequest req,
								HttpServletResponse res,
								@PathVariable Map<String, String> pathVariables) {
		String action = req.getParameter("action");

		int id = TextUtils.getIntValue(pathVariables.get("id"));
		if (id != 0) {
			if ("refresh-crop-image".equals(action)) {
				ajaxUpdate(req, res, "cropImagePanel");
			}
		}

		return getModel(id, BannerImage.Type.getByName(pathVariables.get("typeName")));
	}

    private ModelAndView getModel(int id, BannerImage.Type type) {
        ModelBuilder model = new ModelBuilder("editor/editBannerImage");
        BannerImage bannerImage;

        if (id == 0) {
        	bannerImage = new BannerImage();
        	bannerImage.setType(type);
        } else {
        	bannerImage = applicationService.findById(new BannerImage(id));
        	if (type == BannerImage.Type.slider) {
        		model.put("image", FileDataSource.getImage(projectConfiguration, bannerImage, ImageFormat.BANNER_IMAGE));
        	}
        }
        model.put("bannerImage", bannerImage);

		return model;
    }

    private boolean validate(BindingResult result, BannerImage bannerImage) {
        if (StringUtils.isBlank(bannerImage.getName())) {
            result.rejectValue("name", "field.required", FIELD_REQUIRED);
        }

        return !result.hasErrors();
    }

}
