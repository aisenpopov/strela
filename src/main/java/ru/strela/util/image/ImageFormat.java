package ru.strela.util.image;

import org.apache.commons.lang.ArrayUtils;


public enum ImageFormat {
	ORIGINAL("o", 0, 0, 0, 0, false, true, false),
	
	NEWS_PREVIEW("np", 260, 171, 260, 171, false, false, false),
	NEWS_CONTENT("nc", 1170, 0, 1170, 0, false, false, false),

	STATIC_PAGE_CONTENT("spc", 1170, 0, 1170, 0, false, false, false),

	BANNER_IMAGE("bi", 940, 479, 940, 479, false, false, false),

	ATHLETE_MIDDLE("ap", 300, 300, 300, 300, true, false, false);
	
	private String prefix;
	private int widthVertical;
	private int heightVertical;
	private int widthHorizontal;
	private int heightHorizontal;
	private boolean isSquare;
	private boolean hightQuality;
	private boolean isGray;
	
	private ImageFormat(String prefix, 
			int widthVertical, int heightVertical, 
			int widthHorizontal, int heightHorizontal,
			boolean isSquare, boolean hightQuality, boolean isGray) {
		this.prefix = prefix;
		this.widthVertical = widthVertical;
		this.heightVertical = heightVertical;
		this.widthHorizontal = widthHorizontal;
		this.heightHorizontal = heightHorizontal;
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
	
	public int getHeightHorizontal() {
		return heightHorizontal;
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
		if (dir == ImageDir.ATHLETE_MIDDLE) {
			formats = new ImageFormat[]{ATHLETE_MIDDLE};
		} else if (dir == ImageDir.NEWS_PREVIEW){
			formats = new ImageFormat[]{NEWS_PREVIEW};
		} else if (dir == ImageDir.NEWS_CONTENT){
			formats = new ImageFormat[]{NEWS_CONTENT};
		} else if (dir == ImageDir.STATIC_PAGE_CONTENT){
			formats = new ImageFormat[]{STATIC_PAGE_CONTENT};
		} else if (dir == ImageDir.BANNER_IMAGE) {
			formats = new ImageFormat[]{BANNER_IMAGE};
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
		return "Horizontal: " + getWidthHorizontal() + "x" + getHeightHorizontal() +
			   " Vertical: " + getWidthVertical() + "x" + getHeightVertical();
	}
	
}