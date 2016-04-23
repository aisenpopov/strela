package ru.strela.util.image;

public class ImageInfo {
	
	private int width;
	private int heigth;
	private int size;
	
	public ImageInfo(int width, int height, int size) {
		this.width = width;
		this.heigth = height;
		this.size = size;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getHeigth() {
		return heigth;
	}
	
}