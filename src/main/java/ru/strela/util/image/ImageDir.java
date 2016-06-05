package ru.strela.util.image;

public enum ImageDir {
	
	NEWS_PREVIEW("news_preview"),
	NEWS_CONTENT("news_content"),

	STATIC_PAGE_CONTENT("static_page_content"),

	BANNER_IMAGE("banner_image"),
	
	ATHLETE_MIDDLE("athlete_middle");
	
	private String dir;
	
	private ImageDir(String dir) {
		this.dir = dir;
	}
	
	public String getDir() {
		return dir;
	}
	
	public static ImageDir getDir(String name) {
		for(ImageDir dir : values()) {
			if(dir.name().equalsIgnoreCase(name)) {
				return dir;
			}
		}
		return null;
	}
	
}
