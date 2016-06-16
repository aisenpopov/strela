package ru.strela.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.model.Article;
import ru.strela.model.filter.ArticleFilter;
import ru.strela.util.ModelBuilder;
import ru.strela.util.ResourceNotFoundException;
import ru.strela.util.processor.ArticlePrepareProcessor;
import ru.strela.web.controller.core.BaseController;

/**
 * Created by Aisen on 27.04.2016.
 */
@Controller
@RequestMapping(value = {"/{path}"})
public class StaticPageController extends BaseController {

    @RequestMapping(value = "/", method = {RequestMethod.GET})
    public ModelAndView getStaticPage(@PathVariable String path) {
        ModelBuilder model = new ModelBuilder("staticPage");

        ArticleFilter articleFilter = new ArticleFilter();
        articleFilter.setType(Article.Type.static_page);
        articleFilter.setVisible(true);
        articleFilter.setPath(path);
        Article staticPage = applicationService.findOneArticle(articleFilter);
        if (staticPage != null) {
            model.addObject("staticPage", staticPage);

            ArticlePrepareProcessor prepareProcessor = new ArticlePrepareProcessor(applicationService, projectConfiguration, staticPage);
            String text = prepareProcessor.process(staticPage.getText());
            model.addObject("text", text);
        } else {
            throw new ResourceNotFoundException();
        }

        return model;
    }

}
