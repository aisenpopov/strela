package ru.strela.util.image;

import org.apache.commons.lang.ArrayUtils;


public enum ImageFormat {
	ORIGINAL("o", 0, 0, 0, 0, false, true, false),
	
	ARTICLE_PREVIEW("ap", 510, 400, 510, 400, false, false, false),
	ARTICLE_CONTENT("ac", 1100, 0, 1100, 0, false, false, false),
	
	ATHLETE_MIDDLE("ap", 300, 300, 300, 300, true, false, false);
	
	private String prefix;
	private int widthVertical;
	private int heightVertical;
	private int widthHorizontal;
	private int heightHorizonal;
	private boolean isSquare;
	private boolean hightQuality;
	private boolean isGray;
	
	private ImageFormat(String prefix, 
			int widthVertical, int heightVertical, 
			int widthHorizontal, int heightHorizonal,
			boolean isSquare, boolean hightQuality, boolean isGray) {
		this.prefix = prefix;
		this.widthVertical = widthVertical;
		this.heightVertical = heightVertical;
		this.widthHorizontal = widthHorizontal;
		this.heightHorizonal = heightHorizonal;
		this.isSquare = isSquare;
		this.hightQuality = hightQuality;
		this.isGray = isGray;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public int getWidthHorizontal() {
		return widthHorizontal;
	}
	
	public int getHeightHorizonal() {
		return heightHorizonal;
	}
	
	public int getWidthVertical() {
		return widthVertical;
	}
	
	public int getHeightVertical() {
		return heightVertical;
	}
	
	public boolean isSquare() {
		return isSquare;
	}
	
	public boolean isHightQuality() {
		return hightQuality;
	}
	
	public boolean isGray() {
		return isGray;
	}
	
	public void setGray(boolean isGray) {
		this.isGray = isGray;
	}
	
	public static ImageFormat[] getImageFormats(ImageDir dir) {
		return getImageFormats(dir, false);
	}
	
	public static ImageFormat[] getImageFormats(ImageDir dir, boolean excludeOriginal) {
		ImageFormat[] formats = new ImageFormat[]{};
		if(dir == ImageDir.ARTICLE_PREVIEW) {
			formats = new ImageFormat[]{ARTICLE_PREVIEW};
		} else if(dir == ImageDir.ARTICLE_CONTENT) {
			formats = new ImageFormat[]{ARTICLE_CONTENT};
		} else if(dir == ImageDir.ATHLETE_MIDDLE) {
			formats = new ImageFormat[]{ATHLETE_MIDDLE};
		}
		
		if(!excludeOriginal) {
			formats = (ImageFormat[])ArrayUtils.add(formats, ImageFormat.ORIGINAL);	
		}
		return formats;
	}
	
	public static ImageFormat[] original() {
		return new ImageFormat[]{ORIGINAL};
	}
	
	public static ImageFormat getByName(String name) {
		for(ImageFormat format : ImageFormat.values()) {
			if(format.name().equalsIgnoreCase(name)) {
				return format;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "Horizontal: " + getWidthHorizontal() + "x" + getHeightHorizonal() +
			   " Vertical: " + getWidthVertical() + "x" + getHeightVertical();
	}
	
}