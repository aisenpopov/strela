package ru.strela.util.processor;

import ru.strela.config.ProjectConfiguration;
import ru.strela.model.Article;
import ru.strela.model.ArticleImage;
import ru.strela.service.ApplicationService;
import ru.strela.util.image.FileDataSource;

import java.util.List;

public class ArticlePrepareProcessor extends AbstractProcessor {

	private Article article;
	private ApplicationService applicationService;
	private ProjectConfiguration projectConfiguration;
	
	public ArticlePrepareProcessor(ApplicationService applicationService, ProjectConfiguration projectConfiguration, Article article) {
		this.applicationService = applicationService;
		this.projectConfiguration = projectConfiguration;
		this.article = article;
	}
	
	@Override
	public String process(String text) {
		List<ArticleImage> articleImages = applicationService.getArticleImages(article);
		for (ArticleImage articleImage : articleImages) {
			StringBuilder html = new StringBuilder();
			html.append("<div class='article-image'>")
					.append("<img src=\"").append(FileDataSource.getImage(projectConfiguration, articleImage, article.getType().getImageFormat(false))).append("\"/>")
				.append("</div>");
			text = text.replace("#[image=" + articleImage.getId() + "]", html.toString());
			
			html.delete(0, html.length());
		}
		
//		for(ArticleVideo articleVideo : applicationService.getArticleVideos(article)) {
//			StringBuffer htmlBuff = new StringBuffer();
//			htmlBuff.append("<iframe class=\"c-t-media\" title='YouTube video player' width='1100' height='620' src='")
//					.append(articleVideo.getLink() + "?showinfo=0")
//					.append("' frameborder='0' allowFullScreen></iframe>");
//			text = text.replace("#[video=" + articleVideo.getId() + "]", htmlBuff.toString());
//			text = text.replace("watch?v=", "embed/");
//		}
		
		return text;
	}
}