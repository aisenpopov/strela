package ru.strela.util.image;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.strela.config.ProjectConfiguration;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UploadImageHelperImpl implements UploadImageHelper {
	
	private static final Logger LOG = Logger.getLogger(UploadImageHelperImpl.class);
	
	@Autowired
	private Converter converter;
	
	@Autowired
	private ProjectConfiguration projectConfiguration;
	
	@Override
	public File uploadTmpFile(String prefix, String suffix, byte[] data) throws IOException {
		File newFile = File.createTempFile(prefix, suffix);
		FileOutputStream out = new FileOutputStream(newFile);
		out.write(data);
		out.close();
		return newFile;
	}
	
	@Override
	public File uploadImage(ImageDir dir, String nameFile, String suffix, File file) {
		return uploadImage(dir.getDir(), nameFile, suffix, file);
	}
	
	@Override
	public File uploadImage(String dir, String nameFile, String suffix, File file) {
		File baseDir = new File(projectConfiguration.getImageUploadDir() + "/" + dir);
		baseDir.mkdirs();
		try {
			File newFile = new File(baseDir, nameFile + "." + suffix);
			(new File(newFile.getParent())).mkdirs();
			FileInputStream in = new FileInputStream(file);
			FileOutputStream out = new FileOutputStream(newFile);
			IOUtils.copy(in, out);
			in.close();
			out.close();
			return newFile;
		} catch (IOException e) {
			LOG.error(e);
		}
		return null;
	}
	
	@Override
	public void removeImage(ImageDir dir, String objectId, String nameFile, String suffix) {
		File baseDir = new File(projectConfiguration.getImageUploadDir() + "/" + dir.getDir());
		if (!baseDir.exists()) return;

		File f = new File(baseDir, "/" + objectId + "/" + nameFile + "." + suffix);
		f.delete();
		File objectDirectory = new File(baseDir, "/" + objectId); 
		if(objectDirectory.isDirectory() && objectDirectory.list().length == 0) {
			objectDirectory.delete();
		}
	}
	
	@Override
	public int uploadImage( ImageDir dir, ImageFormat[] formats,  File file, int objectId) {
		try {
			FileInputStream fis = new FileInputStream(file);
			byte[] data = IOUtils.toByteArray(fis);
			return uploadImage(dir, formats, data, objectId);
		} catch (IOException e) {
			return -1;
		}
	}
	
	@Override
	public int uploadImage( ImageDir dir, ImageFormat[] formats,  byte[] data, int objectId) {
		return uploadImage(dir, formats, data, String.valueOf(objectId));
	}
	
	@Override
	public int uploadImage( ImageDir dir, ImageFormat[] formats,  byte[] data, String objectId) {
		int ts = -1;
		try {
			boolean isConverte = true;
			List<File> files = new ArrayList<File>();
			for(ImageFormat format : formats) {
				File inFile = uploadTmpFile(format.name().toLowerCase() + "_in" ,".jpg", data);
				File tmpFile = File.createTempFile("tmp" + format.getPrefix().replace("/", "_") + objectId, ".jpg");
				files.add(tmpFile);
				if(!converter.convertHW(inFile, tmpFile, 
						format.getWidthVertical(), format.getHeightVertical(),
						format.getWidthHorizontal(), format.getHeightHorizontal(), format.isSquare(), format.isHightQuality(), format.isGray()) ){
					isConverte = false;
					break;
				}
			}
			if(isConverte){
				ts = RandomUtils.nextInt(1000000);
				int i = 0;
				for(ImageFormat type : formats) {
					File tmpFile = files.get(i++);
					uploadImage(dir.getDir(), objectId + "/" + type.getPrefix(), "jpg", tmpFile);
				}
			}
		} catch (IOException e) {
			LOG.error("Error file upload files", e);
		}
		return ts;
	}
	
	@Override
	public void copyImage(ImageDir dir, int objectIdFrom, int objectIdTo) {
		copyImage(dir, String.valueOf(objectIdFrom), String.valueOf(objectIdTo));
	}
	
	@Override
	public void copyImage(ImageDir dir, String objectIdFrom, String objectIdTo) {
		for(ImageFormat format : ImageFormat.getImageFormats(dir)) {
			copyImageFile(dir, format.getPrefix() + objectIdFrom + ".jpg", format.getPrefix() + objectIdTo  + ".jpg");
		}
	}
	
	private void copyImageFile(ImageDir dir, String nameFileFrom, String nameFileTo) {
		File baseDir = new File(projectConfiguration.getImageUploadDir() + "/" + dir.getDir());
		if (!baseDir.exists()) return;

		File fileForm = new File(baseDir, nameFileFrom);
		if(fileForm.exists()) {
			File fileTo = new File(baseDir, nameFileTo);
			try {
				FileUtils.copyFile(fileForm, fileTo);
			} catch(IOException e) {}
		}
	}
	
	@Override
	public void removeImage(ImageDir dir, ImageFormat[] formats, Integer objectId) {
		if (objectId != null) {
			removeImage(dir, formats, String.valueOf(objectId));
		}
	}
	
	@Override
	public void removeImage(ImageDir dir, ImageFormat[] formats, String objectId) {
		for(ImageFormat format : formats) {
			removeImage(dir, objectId, format.getPrefix(), "jpg");
		}
	}
	
	@Override
	public ImageInfo getImageInfo(ImageDir dir, ImageFormat format, int objectId) {
		File file = new File(projectConfiguration.getImageUploadDir() + "/" + dir.getDir() + "/" + format.getPrefix() + objectId + ".jpg");
		if(file.exists()) {
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Image img = toolkit.createImage(file.getAbsolutePath());
	        ImageIcon icon = new ImageIcon(img);
	        
	        return new ImageInfo(icon.getIconWidth(), icon.getIconHeight(), (int)(file.length() / 1024));
	    }
		return null;
	}
	
	@Override
	public byte[] getImage(ImageDir dir, ImageFormat format, int objectId) {
		return getImage(dir, format, String.valueOf(objectId));
	}
	
	@Override
	public byte[] getImage(ImageDir dir, ImageFormat format, String objectId) {
		File file = new File(projectConfiguration.getImageUploadDir() + "/" + dir.getDir() + "/" + format.getPrefix() + objectId + ".jpg");
		if(file.exists()) {
			try {
				return IOUtils.toByteArray(new FileInputStream(file));
			} catch (IOException e) {
			}
		}
		return null;
	}
	
}
