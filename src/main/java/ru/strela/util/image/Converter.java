package ru.strela.util.image;

import java.io.File;
import java.io.IOException;

public interface Converter {
	
	boolean convertHW(File in, File out, int widthVertical, int heightVertical,
			int widthHorizontal, int heightHorizontal, boolean isSquare, boolean hightQuality, boolean isGray);
	
	void scaleHW(File in, File out, int widthVertical, int heightVertical,
			int widthHorizontal, int heightHorizontal, boolean isSquare, boolean hightQuality, boolean isGray) throws IOException;
	
	void scaleHW(byte[] image, File out, int widthVertical, int heightVertical,
			int widthHorizontal, int heightHorizontal, boolean isSquare, boolean hightQuality, boolean isGray) throws IOException;
	
	byte[] scaleHW(byte[] in, int widthVertical, int heightVertical, 
					int widthHorizontal, int heightHorizontal, boolean isSquare, boolean hightQuality, boolean isGray) throws IOException;
	
	byte[] resizeImage(byte[] source, int width, int height);
	
	byte[] cropImage(byte[] source, int x, int y, int width, int height);
	
	byte[] overlayWatermark(byte[] source);
	
	ImageInfo getImageInfo(byte[] image);
	
	ImageCropInfo getImageCropInfo(ImageInfo imageInfo, int scaleWidth, int scaleHeight);
	
	byte[] increaseImage(byte[] image, int width, int height, boolean stretch);

	byte[] wrapInSquare(byte[] image, int size);
	
}
