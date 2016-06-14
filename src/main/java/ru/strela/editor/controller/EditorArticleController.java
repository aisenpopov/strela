package ru.strela.editor.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.editor.controller.core.EditorController;
import ru.strela.model.Article;
import ru.strela.model.Article.Type;
import ru.strela.model.ArticleImage;
import ru.strela.model.ArticleVideo;
import ru.strela.model.filter.ArticleFilter;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.util.ModelBuilder;
import ru.strela.util.Redirect;
import ru.strela.util.TextUtils;
import ru.strela.util.TranslitHelper;
import ru.strela.util.ajax.JsonResponse;
import ru.strela.util.image.FileDataSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/editor/article/{typeName}")
public class EditorArticleController extends EditorController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "size", required = false, defaultValue = "50") int pageSize,
                             @ModelAttribute("filter") ArticleFilter filter,
                             @PathVariable Map<String, String> pathVariables) {
    	ModelBuilder model = new ModelBuilder("editor/articles");
        if(filter == null) {        	
        	filter = new ArticleFilter();
        }
        filter.addOrder(new Order("publish", OrderDirection.Desc));
        Type type = Type.valueOf(pathVariables.get("typeName"));
        filter.setType(type);
        Page<Article> page = applicationService.findArticles(filter, pageNumber - 1, pageSize);
        model.put("page", page);
        if(type == Type.news) {
        	model.put("title", "Новости");
        	model.addTableColumn("Дата", "printPublish");
        } else {
        	model.put("title", "Статические страницы");
        }
		model.addTableColumn("Путь", "path");
		model.addTableColumn("Показывать", "printVisible");
        
        return model;
    }

    @RequestMapping(value = {"/edit/{id}", "/edit"}, method = RequestMethod.GET)
    public ModelAndView get(@PathVariable Map<String, String> pathVariables) {
    	ModelAndView modelAndView = getModel(TextUtils.getIntValue(pathVariables.get("id")),
    										Type.valueOf(pathVariables.get("typeName")));
        
        return modelAndView;
    }
    
    @RequestMapping(value = {"/edit", "/edit/{id}"}, method = RequestMethod.POST)
    public ModelAndView save(Article article, BindingResult result, @PathVariable Map<String, String> pathVariables) {
    	Type type = Type.valueOf(pathVariables.get("typeName"));
    	
    	if(validate(result, article)) {
            if(article.getId() != 0) {
            	Article saved = applicationService.findById(new Article(article.getId()));
            	
            	saved.setPublish(article.getPublish());
    			saved.setName(article.getName());
    			saved.setPath(article.getPath());
            	saved.setVisible(article.isVisible());           	
            	saved.setShortText(article.getShortText());
            	saved.setText(article.getText());
            	saved.setHtmlTitle(article.getHtmlTitle());
            	saved.setMetaKeywords(article.getMetaKeywords());
            	saved.setMetaDescription(article.getMetaDescription());         

        		article = saved;
            }         
            
            String path = article.getPath();
            if(StringUtils.isBlank(path)) {            	
            	path = TranslitHelper.translit(article.getName());
            }
        	ArticleFilter filter = new ArticleFilter();
    		filter.setPath(path);
    		Article exists = applicationService.findOneArticle(filter);
    		if(exists != null) {
    			if(exists.getPath().equals(path) && article.getId() != exists.getId()) {
            		article = applicationService.save(article);
            		path = path + "_" + article.getId();
            	}
    			article.setPath(path);
    		}
    		          
            article = applicationService.save(article);          
            
            return new Redirect("/editor/article/" + type.name() + "/edit/" + article.getId() + "/");
        }

        return new ModelAndView("editor/editArticle");
    }
    
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse remove(@PathVariable("id") int id) {
        JsonResponse response = new JsonResponse();
        try {
            applicationService.remove(new Article(id));
        } catch(Exception e) {
            response.setStatus("error");
        }
        return response;
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
			ajaxUpdate(req, res, "cropImagePanel" + req.getParameter("type"));
		} else if("save-videos".equals(action) && id != 0) {
			Article article = applicationService.findById(new Article(id));
			List<ArticleVideo> videos = applicationService.getArticleVideos(article);
			int position = 0;
			while(req.getParameter("videoId" + position) != null) {
	    		int videoId = Integer.parseInt(req.getParameter("videoId" + position)); 
	    		ArticleVideo articleVideo = null;
	    		if(videoId != 0) {
	    			articleVideo = applicationService.findById(new ArticleVideo(videoId));
	    			videos.remove(articleVideo);
	    		} else {
	    			articleVideo = new ArticleVideo();
	    			articleVideo.setArticle(article);
	    		}
	    		
	    		articleVideo.setLink(req.getParameter("link" + position));
	    		
	    		articleVideo = applicationService.save(articleVideo);
	    		
	    		position++;
	    	}
	    	for(ArticleVideo articleVideo : videos) {
	    		applicationService.remove(articleVideo);
	    	}
	    	ajaxUpdate(req, res, "video-panel");
		}
		
		return getModel(id, null);
	}
    
    private ModelAndView getModel(int id, Type type) {
        ModelBuilder model = new ModelBuilder("editor/editArticle");
        Article article;
        
        if(id == 0) {
        	article = new Article();
        	article.setType(type);
        } else {
        	article = applicationService.findById(new Article(id));        	
        	
    		for(ArticleImage articleImage : applicationService.getArticleImages(article)) {
    			ModelBuilder image = model.createCollection("images");
    			image.put("id", articleImage.getId());
    			image.put("image", FileDataSource.getImage(projectConfiguration, articleImage, article.getType().getImageFormat(false)));
    		}	
    		model.put("articleImage", FileDataSource.getImage(projectConfiguration, article, article.getType().getImageFormat(true)));
    	    model.put("videos", applicationService.getArticleVideos(article));
        }
        model.put("article", article);
             
		return model;
    }
    
    private boolean validate(BindingResult result, Article article) {
    	if(StringUtils.isBlank(article.getName())) {
    		result.rejectValue("name", "field.required", FIELD_REQUIRED);
    	}
    	if(article.getType() == Type.news) {
    		if(article.getPublish() == null) {
    			result.rejectValue("publish", "field.required", FIELD_REQUIRED);
    		}
    	}
        
        return !result.hasErrors();
    }
}
