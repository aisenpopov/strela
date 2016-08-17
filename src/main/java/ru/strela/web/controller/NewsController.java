package ru.strela.web.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.strela.model.Article;
import ru.strela.model.filter.ArticleFilter;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.util.DateUtils;
import ru.strela.util.ajax.JsonData;
import ru.strela.util.ajax.JsonResponse;
import ru.strela.util.image.FileDataSource;
import ru.strela.util.image.ImageFormat;
import ru.strela.util.processor.ArticlePrepareProcessor;
import ru.strela.web.controller.core.WebController;

@Controller
@RequestMapping(value = {"/news"})
public class NewsController extends WebController {

    @ResponseBody
    @RequestMapping(value = "/{path}", method = RequestMethod.POST)
    public JsonResponse getNews(@PathVariable String path) {
        JsonResponse response = new JsonResponse();
        JsonData data = response.createJsonData();

        ArticleFilter articleFilter = new ArticleFilter();
        articleFilter.setType(Article.Type.news);
        articleFilter.setVisible(true);
        articleFilter.setPath(path);
        Article news = applicationService.findOneArticle(articleFilter);
        if (news != null) {
            data.put("name", news.getName());
            data.put("publish", DateUtils.formatDayMonthYear(news.getPublish()));

            ArticlePrepareProcessor prepareProcessor = new ArticlePrepareProcessor(applicationService, projectConfiguration, news);
            String text = prepareProcessor.process(news.getText());
            data.put("text", text);
        } else {
            response.setRedirect("/");
        }

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public JsonResponse getNews(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                                @RequestParam(value = "size", required = false, defaultValue = "6") int pageSize) {
        JsonResponse response = new JsonResponse();
        JsonData data = response.createJsonData();

        ArticleFilter articleFilter = new ArticleFilter();
        articleFilter.setType(Article.Type.news);
        articleFilter.setVisible(true);
        articleFilter.addOrder(new Order("publish", OrderDirection.Desc));
        Page<Article> page = applicationService.findArticles(articleFilter, pageNumber - 1, pageSize);
        for (Article article : page) {
            JsonData jsonData = data.createCollection("newsList");
            jsonData.put("path", article.getPath());
            jsonData.put("name", article.getName());
            jsonData.put("shortText", article.getShortText());
            jsonData.put("publish", DateUtils.formatDayMonthYear(article.getPublish()));
            jsonData.put("image", FileDataSource.getImage(projectConfiguration, article, ImageFormat.NEWS_PREVIEW));
        }

        JsonData pageData = data.addJsonData("page");
        pageData.put("number", page.getNumber());
        pageData.put("totalPages", page.getTotalPages());

        return response;
    }

}
