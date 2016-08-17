package ru.strela.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.strela.model.Article;
import ru.strela.model.filter.ArticleFilter;
import ru.strela.util.ajax.JsonData;
import ru.strela.util.ajax.JsonResponse;
import ru.strela.util.processor.ArticlePrepareProcessor;
import ru.strela.web.controller.core.WebController;

@Controller
@RequestMapping(value = "/static")
public class StaticPageController extends WebController {

    @ResponseBody
    @RequestMapping(value = "/{path}", method = RequestMethod.POST)
    public JsonResponse getStaticPage(@PathVariable String path) {
        JsonResponse response = new JsonResponse();
        JsonData data = response.createJsonData();

        ArticleFilter articleFilter = new ArticleFilter();
        articleFilter.setType(Article.Type.static_page);
        articleFilter.setVisible(true);
        articleFilter.setPath(path);
        Article staticPage = applicationService.findOneArticle(articleFilter);
        if (staticPage != null) {
            data.put("name", staticPage.getName());

            ArticlePrepareProcessor prepareProcessor = new ArticlePrepareProcessor(applicationService, projectConfiguration, staticPage);
            String text = prepareProcessor.process(staticPage.getText());
            data.put("text", text);
        } else {
            response.setRedirect("/");
        }

        return response;
    }

}
