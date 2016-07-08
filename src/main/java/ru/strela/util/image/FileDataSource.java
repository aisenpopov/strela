package ru.strela.util.image;

import org.apache.commons.lang.StringUtils;
import ru.strela.config.ProjectConfiguration;
import ru.strela.model.*;

public class FileDataSource {

//	public static String getImage(ProjectConfiguration configuration, UserInfo userInfo, ImageFormat formatDayMonth) {
//		if (userInfo != null) {
//			return getImage(configuration, userInfo.getId(), ImageDir.SOCIAL_AVATAR, formatDayMonth, null);
//		} 
//		return StringUtils.EMPTY;
//	}
	
//	public static String getImage(ProjectConfiguration configuration, PageImage pageImage, ImageFormat formatDayMonth) {
//		if (pageImage != null) {
//			return getImage(configuration, pageImage.getId(), ImageDir.PAGE_IMAGE, formatDayMonth, null);
//		} 
//		return StringUtils.EMPTY;
//	}
//	
//	public static String getImage(ProjectConfiguration configuration, Settings settings, ImageFormat formatDayMonth) {
//		if (settings != null && settings.getImage() != null) {
//			return getImage(configuration, settings.getId(), ImageDir.SETTINGS_MENU, formatDayMonth, settings.getImage());
//		} 
//		return StringUtils.EMPTY;
//	}
	
	public static String getImage(ProjectConfiguration configuration, BannerImage bannerImage, ImageFormat format) {
		if (bannerImage != null && bannerImage.getImage() != null) {
			return getImage(configuration, bannerImage.getId(), ImageDir.BANNER_IMAGE, format, bannerImage.getImage());
		}
		return StringUtils.EMPTY;
	}
	
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
			if(ImageFormat.NEWS_PREVIEW == format && article.getImage() != null) {
				return getImage(configuration, article.getId(), ImageDir.NEWS_PREVIEW, format, article.getImage());
			}
		} 
		return StringUtils.EMPTY;
	}
	
	public static String getImage(ProjectConfiguration configuration, ArticleImage articleImage, ImageFormat format) {
		if (articleImage != null) {
			if (ImageFormat.NEWS_CONTENT == format) {
				return getImage(configuration, articleImage.getId(), ImageDir.NEWS_CONTENT, format, null);
			} else if (ImageFormat.GYM_CONTENT == format) {
				return getImage(configuration, articleImage.getId(), ImageDir.GYM_CONTENT, format, null);
			}

		}
		return StringUtils.EMPTY;
	}

	public static String getImage(ProjectConfiguration projectConfiguration, Gym gym, ImageFormat format) {
		if (gym != null && gym.getArticle() != null && gym.getArticle().getImage() != null) {
			return getImage(projectConfiguration, gym.getArticle().getId(), ImageDir.GYM_PREVIEW, format, gym.getArticle().getImage());
		}
		return StringUtils.EMPTY;
	}

	private static String getImage(ProjectConfiguration configuration, int id, ImageDir imageDir, ImageFormat format, Integer image) {
		String dir = configuration.getImageDir() + "/" + imageDir.getDir() + "/";
		return dir + id + "/" + format.getPrefix() + ".jpg" + (image != null ? "?" + image : "");
	}
	
}
