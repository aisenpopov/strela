package ru.strela.editor.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.editor.controller.core.EditorController;
import ru.strela.model.Article;
import ru.strela.model.Gym;
import ru.strela.model.filter.GymFilter;
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
@RequestMapping("/editor/gym")
public class EditorGymController extends EditorController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "size", required = false, defaultValue = "50") int pageSize,
                             @ModelAttribute("filter") GymFilter filter,
                             @PathVariable Map<String, String> pathVariables) {
    	ModelBuilder model = new ModelBuilder("editor/gyms");
        if(filter == null) {        	
        	filter = new GymFilter();
        }
        filter.addOrder(new Order("name", OrderDirection.Asc));
        Page<Gym> page = applicationService.findGyms(filter, pageNumber - 1, pageSize);
        model.put("page", page);
        
        return model;
    }

    @RequestMapping(value = {"/edit/{id}", "/edit"}, method = RequestMethod.GET)
    public ModelAndView get(@PathVariable Map<String, String> pathVariables) {
    	ModelAndView modelAndView = getModel(TextUtils.getIntValue(pathVariables.get("id")));
        
        return modelAndView;
    }
    
    @RequestMapping(value = {"/edit", "/edit/{id}"}, method = RequestMethod.POST)
    public ModelAndView save(Gym gym, BindingResult result, @PathVariable Map<String, String> pathVariables) {
    	if(validate(result, gym)) {
            Article article = gym.getArticle();
            article.setName(gym.getName());
            if (article.getId() != 0) {
                Article existArticle = applicationService.findById(new Article(article.getId()));
                existArticle.setText(article.getText());
                existArticle.setMetaDescription(article.getMetaDescription());
                existArticle.setMetaKeywords(article.getMetaKeywords());
                existArticle.setHtmlTitle(article.getHtmlTitle());

                article = existArticle;
            } else {
                article.setType(Article.Type.inner);
            }
            if (gym.getId() != 0) {
                Gym existGym = applicationService.findById(new Gym(gym.getId()));

    			existGym.setName(gym.getName());
    			existGym.setCity(gym.getCity());
                existGym.setTeam(gym.getTeam());
                existGym.setAddress(gym.getAddress());
                existGym.setInstructors(gym.getInstructors());

        		gym = existGym;
            }         

            article = applicationService.save(article);
            gym.setArticle(article);
            gym = applicationService.save(gym);
            
            return new Redirect("/editor/gym/edit/" + gym.getId() + "/");
        }

        return new ModelAndView("editor/editGym");
    }

    @RequestMapping(value="/edit/{id}/ajax/", method=RequestMethod.POST)
    public ModelAndView onAjax(HttpServletRequest req,
                               HttpServletResponse res,
                               @PathVariable Map<String, String> pathVariables) {
        String action = req.getParameter("action");

        int id = TextUtils.getIntValue(pathVariables.get("id"));
        if("refresh-image".equals(action) && id != 0) {
            ajaxUpdate(req, res, "image-list");
        } else if ("refresh-crop-image".equals(action) && id != 0) {
            ajaxUpdate(req, res, "cropImagePanel");
        }

        return getModel(id);
    }
    
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse remove(@PathVariable("id") int id) {
        JsonResponse response = new JsonResponse();
        try {
            applicationService.remove(new Gym(id));
        } catch(Exception e) {
            response.setStatus("error");
        }
        return response;
    }
    
    private ModelAndView getModel(int id) {
        ModelBuilder model = new ModelBuilder("editor/editGym");
        Gym gym;
        
        if (id == 0) {
        	gym = new Gym();
        } else {
        	gym = applicationService.findById(new Gym(id));
            model.put("gymImage", FileDataSource.getImage(projectConfiguration, gym, ImageFormat.GYM_PREVIEW));
        }
        if (gym.getArticle() == null) {
            gym.setArticle(new Article());
        }
        model.put("gym", gym);
             
		return model;
    }
    
    private boolean validate(BindingResult result, Gym gym) {
    	if(StringUtils.isBlank(gym.getName())) {
    		result.rejectValue("name", "field.required", FIELD_REQUIRED);
    	}
        if(StringUtils.isBlank(gym.getAddress())) {
            result.rejectValue("address", "field.required", FIELD_REQUIRED);
        }
    	if(gym.getCity() == null) {
    		result.rejectValue("city", "field.required", FIELD_REQUIRED);
    	}
    	if(gym.getTeam() == null) {
    		result.rejectValue("team", "field.required", FIELD_REQUIRED);
    	}
        
        return !result.hasErrors();
    }
}
