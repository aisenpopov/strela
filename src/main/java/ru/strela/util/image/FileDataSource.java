package ru.strela.util.image;

import org.apache.commons.lang.StringUtils;

import ru.strela.config.ProjectConfiguration;
import ru.strela.model.Article;
import ru.strela.model.ArticleImage;
import ru.strela.model.Athlete;

public class FileDataSource {

//	public static String getImage(ProjectConfiguration configuration, UserInfo userInfo, ImageFormat format) {
//		if (userInfo != null) {
//			return getImage(configuration, userInfo.getId(), ImageDir.SOCIAL_AVATAR, format, null);
//		} 
//		return StringUtils.EMPTY;
//	}
	
//	public static String getImage(ProjectConfiguration configuration, PageImage pageImage, ImageFormat format) {
//		if (pageImage != null) {
//			return getImage(configuration, pageImage.getId(), ImageDir.PAGE_IMAGE, format, null);
//		} 
//		return StringUtils.EMPTY;
//	}
//	
//	public static String getImage(ProjectConfiguration configuration, Settings settings, ImageFormat format) {
//		if (settings != null && settings.getImage() != null) {
//			return getImage(configuration, settings.getId(), ImageDir.SETTINGS_MENU, format, settings.getImage());
//		} 
//		return StringUtils.EMPTY;
//	}
	
//	public static String getImage(ProjectConfiguration configuration, MainImage mainImage, ImageFormat format) {
//		if (mainImage != null && mainImage.getImage() != null) {
//			return getImage(configuration, mainImage.getId(), ImageDir.MAIN_IMAGE_PHOTO, format, mainImage.getImage());
//		} 
//		return StringUtils.EMPTY;
//	}
	
	public static String getImage(ProjectConfiguration configuration, Athlete athlete, ImageFormat format) {
		if(athlete != null) {
			if(ImageFormat.ATHLETE_MIDDLE == format && athlete.getImage() != null) {
				return getImage(configuration, athlete.getId(), ImageDir.ATHLETE_MIDDLE, format, athlete.getImage());
			}
		}
		return StringUtils.EMPTY;
	}
	
	public static String getImage(ProjectConfiguration configuration, Article article, ImageFormat format) {
		if(article != null) {
			if(ImageFormat.ARTICLE_PREVIEW == format && article.getImage() != null) {
				return getImage(configuration, article.getId(), ImageDir.ARTICLE_PREVIEW, format, article.getImage());
			}
		} 
		return StringUtils.EMPTY;
	}
	
	public static String getImage(ProjectConfiguration configuration, ArticleImage articleImage, ImageFormat format) {
		if (articleImage != null) {
			return getImage(configuration, articleImage.getId(), ImageDir.ARTICLE_CONTENT, format, null);
		} 
		return StringUtils.EMPTY;
	}
	
	private static String getImage(ProjectConfiguration configuration, int id, ImageDir imageDir, ImageFormat format, Integer image) {
		String dir = configuration.getImageDir() + "/" + imageDir.getDir() + "/";
		return dir + id + "/" + format.getPrefix() + ".jpg" + (image != null ? "?" + image : "");
	}
	
}
