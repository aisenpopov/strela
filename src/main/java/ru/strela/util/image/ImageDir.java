package ru.strela.util.image;

public enum ImageDir {
	
	ARTICLE_PREVIEW("article_preview"),
	ARTICLE_CONTENT("article_content"),
	
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
