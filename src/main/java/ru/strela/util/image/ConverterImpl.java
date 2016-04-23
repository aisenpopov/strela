package ru.strela.util.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.JPEGEncodeParam;
import com.sun.media.jai.codec.SeekableStream;

public class ConverterImpl implements Converter {
	
	private static final Logger logger = LoggerFactory.getLogger(Converter.class);

	private static final String WATERMARK_FILE_NAME = "/images/watermark.png";
	
	@Override
	public boolean convertHW(File in, File out, 
			int widthVertical, int heightVertical,
			int widthHorizontal, int heightHorizontal,
			boolean isSquare, boolean hightQuality, boolean isGray) {
		
		try {
			scaleHW(in, out, widthVertical, heightVertical, widthHorizontal, heightHorizontal, isSquare, hightQuality, isGray);
			return  true;
		} catch (IOException e) {
			logger.error("Problem while convert file " + in.getAbsolutePath() + " to " + out.getAbsolutePath(), e);
			return false;
		}
	}
	
	@Override
	public void scaleHW(File in, File out, int widthVertical, int heightVertical,
			int widthHorizontal, int heightHorizontal, boolean isSquare, boolean hightQuality, boolean isGray) throws IOException {
		scaleHW(IOUtils.toByteArray(new FileInputStream(in)), out, widthVertical, heightVertical, widthHorizontal, heightHorizontal, isSquare, hightQuality, isGray);
	}
	
	@Override
	public void scaleHW(byte[] in, File out, 
			int widthVertical, int heightVertical,
			int widthHorizontal, int heightHorizontal, 
			boolean isSquare, boolean hightQuality, boolean isGray) throws IOException {
		
		out.getParentFile().mkdirs();
		FileOutputStream os = new FileOutputStream(out);
		
		byte[] scaledImage = scaleHW(in, widthVertical, heightVertical, widthHorizontal, heightHorizontal, isSquare, hightQuality, isGray);
		os.write(scaledImage);
		os.close();
	}
	
	@Override
	public byte[] scaleHW(byte[] in, int widthVertical, int heightVertical,
							int widthHorizontal, int heightHorizontal, 
							boolean isSquare, boolean hightQuality, boolean isGray) throws IOException {
		
		RenderedOp image = JAI.create("stream", SeekableStream.wrapInputStream(new ByteArrayInputStream(in), true));
		
		int width, height;
		if(image.getWidth() < image.getHeight()) {
			width = widthVertical;
			height = heightVertical;
		} else {
			width = widthHorizontal;
			height = heightHorizontal;
		}
		
		//hightQuality = width == 0 && height == 0 ? true : hightQuality;
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		if ((width > 0 && width < image.getWidth() || height > 0 && height < image.getHeight())) {
			int scaleWidth = width;
			int scaleHeight = height;
			double ratio = image.getWidth() / (float)image.getHeight();
			if(isSquare && width == 0 && height == 0) {
				width = Math.min(image.getWidth(), image.getHeight());
				height = Math.min(image.getWidth(), image.getHeight());
				scaleWidth = image.getWidth();
				scaleHeight = image.getHeight();
			} else if(height == 0) {
				double widthRatio = image.getWidth() / (float) width;
				scaleHeight = (int)Math.round(image.getHeight() / widthRatio);
			} else if(width == 0) {
				double heightRatio = image.getHeight() / (float) height;
				scaleWidth = (int)Math.round(image.getWidth() / heightRatio);
			} else {
				double requiredRatio = width / (float)height;
				if(ratio < requiredRatio) {
					double widthRatio = image.getWidth() / (float) width;
					scaleHeight = (int)Math.round(image.getHeight() / widthRatio);
				} else {
					double heightRatio = image.getHeight() / (float) height;
					scaleWidth = (int)Math.round(image.getWidth() / heightRatio);
				}
			}
			
			BufferedImage scaledImage = scaleImage(image, scaleWidth, scaleHeight);
			if(width > 0 && height > 0 && (width < scaleWidth || height < scaleHeight)) {
            	int w = Math.min(width, scaleWidth);
            	int h = Math.min(height, scaleHeight);
            	int x = scaleWidth / 2 - w / 2;
            	int y = scaleHeight / 2 - h / 2;
            	
//            	ParameterBlock pb = new ParameterBlock();
//            	pb.addSource(resultImage);
//            	pb.add((float)x);
//            	pb.add((float)y);
//            	pb.add((float)w);
//            	pb.add((float)h);
//            	resultImage = JAI.create("crop",pb);
            	scaledImage = scaledImage.getSubimage(x, y, w, h);
            }
			
			//ParameterBlock pb2 = new ParameterBlock();
            //pb2.add(scaledImage);
            // RenderedOp resultImage = JAI.create("AWTImage", pb2);
			
//			if(imageType == BufferedImage.TYPE_INT_ARGB || imageType == BufferedImage.TYPE_4BYTE_ABGR) {
//				ParameterBlock pb2 = new ParameterBlock();
//	            pb2.add(scaledImage);
//				RenderedOp resultImage = JAI.create("AWTImage", pb2);
//				
//				PlanarImage planarImage = resultImage.getRendering();
//				PNGEncodeParam.RGB pngEncodeParam = new PNGEncodeParam.RGB();
//		        ImageEncoder encoder = ImageCodec.createImageEncoder("PNG", os, pngEncodeParam);
//			    encoder.encode(planarImage.getAsBufferedImage());
//			    //encoder.encode(scaledImage);
//			} else {
//				JPEGEncodeParam encodeParam = new JPEGEncodeParam();
//			    encodeParam.setQuality(0.93f);
//			    ImageEncoder encoder = ImageCodec.createImageEncoder("JPEG", os, encodeParam);
//			    encoder.encode(scaledImage);
//			}
			String[] codecs = ImageCodec.getDecoderNames(SeekableStream.wrapInputStream(new ByteArrayInputStream(in), true));
			if(!ArrayUtils.contains(codecs, "png")) { 
		        BufferedImage newBufferedImage = new BufferedImage(scaledImage.getWidth(),
	            		scaledImage.getHeight(),  (isGray) ? BufferedImage.TYPE_BYTE_GRAY : BufferedImage.TYPE_INT_RGB);
	        	newBufferedImage.createGraphics().drawImage(scaledImage, 0, 0, Color.WHITE, null);
	        	  
	        	JPEGEncodeParam encodeParam = new JPEGEncodeParam();
			    encodeParam.setQuality(hightQuality ? 1 : 0.95f);
			    ImageEncoder encoder = ImageCodec.createImageEncoder("JPEG", os, encodeParam);
			    encoder.encode(newBufferedImage);
			} else {
				ImageIO.write(scaledImage, codecs[0], os);
			}
		    
			//if(out != null) {
				
            	////ImageIO.write(resultImage, "png", out);
           // }
			
		} else {
			
//			int imageType = sourceImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : sourceImage.getType();
//			if(imageType != BufferedImage.TYPE_INT_ARGB && 
//					imageType != BufferedImage.TYPE_4BYTE_ABGR && 
//					imageType != BufferedImage.TYPE_INT_ARGB_PRE && 
//					imageType != BufferedImage.TYPE_4BYTE_ABGR_PRE) {
//				JPEGEncodeParam encodeParam = new JPEGEncodeParam();
//			    encodeParam.setQuality(1f);
//			    ImageEncoder encoder = ImageCodec.createImageEncoder("JPEG", os, encodeParam);
//			    encoder.encode(sourceImage);
//			} else {
				//IOUtils.copy(new FileInputStream(in), os);
//			}
			
			String[] codecs = ImageCodec.getDecoderNames(SeekableStream.wrapInputStream(new ByteArrayInputStream(in), true));
			if(!ArrayUtils.contains(codecs, "png")) {
				BufferedImage sourceImage = image.getAsBufferedImage();
				BufferedImage newBufferedImage = new BufferedImage(sourceImage.getWidth(),
						sourceImage.getHeight(),  (isGray) ? BufferedImage.TYPE_BYTE_GRAY : BufferedImage.TYPE_INT_RGB);
				newBufferedImage.createGraphics().drawImage(sourceImage, 0, 0, Color.WHITE, null);
	        	JPEGEncodeParam encodeParam = new JPEGEncodeParam();
				encodeParam.setQuality(hightQuality ? 1 : 0.95f);
				ImageEncoder encoder = ImageCodec.createImageEncoder("JPEG", os, encodeParam);
				encoder.encode(newBufferedImage);
			} else {
				IOUtils.copy(new ByteArrayInputStream(in), os);
			}
		}
		os.close();
		
		return os.toByteArray();
	}
	
	private BufferedImage scaleImage(RenderedOp image, int scaleWidth, int scaleHeight) {
		Map<RenderingHints.Key, Object> map = new HashMap<RenderingHints.Key, Object>();
		map.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		map.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		map.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		map.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		map.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		map.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		map.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		RenderingHints hints = new RenderingHints(map);
		
		BufferedImage sourceImage = image.getAsBufferedImage();
		int imageType = sourceImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : sourceImage.getType();
		BufferedImage scaledImage = new BufferedImage(scaleWidth, scaleHeight, imageType);
		Graphics2D graphics = scaledImage.createGraphics();
		graphics.setRenderingHints(hints);
		Image scaled;
		if(imageType == BufferedImage.TYPE_BYTE_GRAY) {
			scaled = image.getAsBufferedImage();
			graphics.scale((double)scaleWidth / image.getWidth(), (double)scaleHeight / image.getHeight());
		} else {
			scaled = image.getAsBufferedImage().getScaledInstance(scaleWidth, scaleHeight, Image.SCALE_SMOOTH);
        }
		graphics.drawImage(scaled, 0, 0, null);
        graphics.dispose();
        
        return scaledImage;
	}
	
	@Override
	public byte[] overlayWatermark(byte[] source) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			BufferedImage waterMark = ImageIO.read(getClass().getResourceAsStream(WATERMARK_FILE_NAME));
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(source));
			Graphics2D g = image.createGraphics();
			int offset = Math.min(image.getWidth(), image.getHeight()) / 25;
			int newWidthLogo = Math.round(image.getWidth() / 4);
			int newHieghtLogo = Math.round(((float)newWidthLogo / waterMark.getWidth()) * waterMark.getHeight());
			try {
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));// 50% transp
				g.drawImage(waterMark, offset, image.getHeight() - newHieghtLogo - offset, newWidthLogo, newHieghtLogo, null);// draw in lower right corner
			} finally {
				g.dispose();
			}
			
			ImageIO.write(image, "jpg", os);
		} catch (IOException e) {
			logger.error("Error overlay watermark", e);
		}
		return os.toByteArray();
	}
	
	@Override
	public byte[] cropImage(byte[] source, int x, int y, int width, int height) {
		try {
			BufferedImage src = ImageIO.read(new ByteArrayInputStream(source));
			BufferedImage cropped = src.getSubimage(x, y, width, height);
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			BufferedImage newBufferedImage = new BufferedImage(cropped.getWidth(), cropped.getHeight(), BufferedImage.TYPE_INT_RGB);
			newBufferedImage.createGraphics().drawImage(cropped, 0, 0, Color.WHITE, null);
        	
			JPEGEncodeParam encodeParam = new JPEGEncodeParam();
        	encodeParam.setQuality(0.98f);
        	ImageEncoder encoder = ImageCodec.createImageEncoder("JPEG", os, encodeParam);
			encoder.encode(newBufferedImage);
			return os.toByteArray();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public byte[] resizeImage(byte[] source, int width, int height) {
		try {
			RenderedOp image = JAI.create("stream", SeekableStream.wrapInputStream(new ByteArrayInputStream(source), true));
			width = width == 0 ? image.getWidth() : width;
			height = height == 0 ? image.getHeight() : height;
			BufferedImage scaledImage = scaleImage(image, width, height);
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
        	ImageIO.write(scaledImage, "jpg", baos);
        	return baos.toByteArray();
		} catch(Exception e) {}
		return null;
	}
	
	@Override
	public ImageInfo getImageInfo(byte[] image) {
		RenderedOp icon = JAI.create("stream", SeekableStream.wrapInputStream(new ByteArrayInputStream(image), true));
		//ImageIcon icon = new ImageIcon(image);
		return new ImageInfo(icon.getWidth(), icon.getHeight(), image.length);
	}
	
	@Override
	public ImageCropInfo getImageCropInfo(ImageInfo imageInfo, int scaleWidth, int scaleHeight) {
		double ratioWidth = imageInfo.getWidth() / (double)scaleWidth;
		double ratioHeight = imageInfo.getHeigth() / (double)scaleHeight;
		double ratio = Math.min(ratioWidth, ratioHeight);
		int widthCrop = (int)Math.round(ratio * scaleWidth);
		int heightCrop = (int)Math.round(ratio * scaleHeight);
		int x = (imageInfo.getWidth() - widthCrop) / 2;
		int y = (imageInfo.getHeigth() - heightCrop) / 2;
		return new ImageCropInfo(x, y, widthCrop, heightCrop);
	}
	
	@Override
	public byte[] increaseImage(byte[] image, int width, int height, boolean stretch) {
		RenderedOp renderOp = JAI.create("stream", SeekableStream.wrapInputStream(new ByteArrayInputStream(image), true));
		BufferedImage sourceImage = renderOp.getAsBufferedImage();
		
		Map<RenderingHints.Key, Object> map = new HashMap<RenderingHints.Key, Object>();
		map.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		map.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		map.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		map.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		map.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		map.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		RenderingHints hints = new RenderingHints(map);
		
		int imageType = sourceImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : sourceImage.getType();
		BufferedImage increasedImage = new BufferedImage(width, height, imageType);
		Graphics2D graphics = increasedImage.createGraphics();
		graphics.setRenderingHints(hints);
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, width, height);
		
		if(stretch) {
			graphics.drawImage(sourceImage, 0, 0, width, height, null);
		} else {
			int x = (width - sourceImage.getWidth()) / 2;
			int y = (height - sourceImage.getHeight()) / 2;
			graphics.drawImage(sourceImage, x, y, null);
		}
        graphics.dispose();
        
        try {
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    	ImageIO.write(increasedImage, "jpg", baos);
	    	return baos.toByteArray();
        } catch (IOException e) {}
        
        return null;
    }
	
	@Override
	public byte[] wrapInSquare(byte[] image, int size) {
		ImageInfo imageInfo = getImageInfo(image);
		double koef = ((double)imageInfo.getWidth()) / imageInfo.getHeigth();
		byte[] preview = null;
		try {
			if (koef > 1) {
				preview = scaleHW(image, size, 
						(int)(size / koef), 
						size, 
						(int)(size / koef), 
						false, false, false);
			} else {
				preview = scaleHW(image, (int)(size * koef), 
						size, 
						(int)(size * koef), 
						size, 
						false, false, false);
			}
		} catch (IOException e) {}
		return preview;
	}
}
