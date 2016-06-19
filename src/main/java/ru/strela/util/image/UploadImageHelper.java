package ru.strela.util.image;

import java.io.File;
import java.io.IOException;


public interface UploadImageHelper {
	
	File uploadImage(ImageDir dir, String nameFile, String suffix, File file);
	
	void removeImage(ImageDir dir, ImageFormat[] formats, Integer objectId);
	
	void removeImage(ImageDir dir, ImageFormat[] formats, String objectId);
	
	void removeImage(ImageDir dir, String objectId, String nameFile, String suffix);

	File uploadTmpFile(String prefix, String suffix, byte[] data) throws IOException;
	
	File uploadImage(String dir, String nameFile, String suffix, File file);
	
	int uploadImage( ImageDir dir, ImageFormat[] formats,  byte[] data, int objectId);
	
	int uploadImage( ImageDir dir, ImageFormat[] formats,  File file, int objectId);
	
	int uploadImage( ImageDir dir, ImageFormat[] formats,  byte[] data, String objectId);
	
	ImageInfo getImageInfo(ImageDir dir, ImageFormat format, int objectId);
	
	byte[] getImage(ImageDir dir, ImageFormat format, int objectId);
	
	byte[] getImage(ImageDir dir, ImageFormat format, String objectId);
	
	void copyImage(ImageDir dir, String objectIdFrom, String objectIdTo);
	
	void copyImage(ImageDir dir, int objectIdFrom, int objectIdTo);
	
}
