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
@RequestMapping(value = {"/news"})
public class NewsController extends BaseController {

    @RequestMapping(value = "/{path}/", method = {RequestMethod.GET})
    public ModelAndView getNews(@PathVariable String path) {
        ModelBuilder model = new ModelBuilder("news");

        ArticleFilter articleFilter = new ArticleFilter();
        articleFilter.setType(Article.Type.news);
        articleFilter.setVisible(true);
        articleFilter.setPath(path);
        Article news = applicationService.findOneArticle(articleFilter);
        if (news != null) {
            model.put("news", news);

            ArticlePrepareProcessor prepareProcessor = new ArticlePrepareProcessor(applicationService, projectConfiguration, news);
            String text = prepareProcessor.process(news.getText());
            model.put("text", text);
        } else {
            throw new ResourceNotFoundException();
        }

        return model;
    }

}
