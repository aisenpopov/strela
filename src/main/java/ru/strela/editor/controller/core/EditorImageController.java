package ru.strela.editor.controller.core;

import com.sun.media.jai.codec.SeekableStream;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import ru.strela.config.ProjectConfiguration;
import ru.strela.model.*;
import ru.strela.service.ApplicationService;
import ru.strela.service.PersonService;
import ru.strela.util.ajax.JsonData;
import ru.strela.util.ajax.JsonResponse;
import ru.strela.util.image.*;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class EditorImageController implements InitializingBean {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EditorImageController.class);
	private static final String MODE_TMP_UPLOAD = "mode_tmp_upload";
	private static final String MODE_UPLOAD = "mode_upload";
	
	@Autowired
	private UploadImageHelper uploadImageHelper;
	
	@Autowired
	private Converter converter;
	
	@Autowired
	private ProjectConfiguration projectConfiguration;
	
	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private PersonService personService;
	
	private File uploadDir;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		uploadDir = new File(projectConfiguration.getUploadDir());
	}
	
	@RequestMapping(value="/tmpfile/{name}", method = RequestMethod.GET)
	public void getTmpImage(HttpServletResponse resp,			               
			                @PathVariable(value="name") String name) throws IOException {
		int idx = name.indexOf(".");
		if(idx > 0) {
			name = name.substring(0, idx);
		}

		InputStream isTmp = null;
		File input = new File(uploadDir, name);
		if (input.exists()) {
			try {
				isTmp = new FileInputStream(input);
			} catch (FileNotFoundException e) {}
		}
		
		if(isTmp != null) {
			resp.setHeader("Cache-Control", "public");
			final long expiration = System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 30L;
			resp.setDateHeader("Expires", expiration);
			resp.setDateHeader("Last-Modified", expiration);
			resp.setContentType("image/jpeg");
			IOUtils.copy(isTmp, resp.getOutputStream());
			return;
		}
		resp.sendError(HttpServletResponse.SC_NOT_FOUND);
	}
	
	@RequestMapping(value="/editor/upload_image", method = RequestMethod.POST)
	@ResponseBody
	public JsonResponse uploadImage(HttpServletRequest req, 
						      @RequestParam(value="mode", required=false) String mode,
							  @RequestParam(value="imageUrl", required=false) String imageUrl) throws IOException {
		JsonResponse response = new JsonResponse();
		List<byte[]> imagesList = new ArrayList<byte[]>();
		
		MultipartFile multiPart = null;
		if(req instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)req;
			multiPart = multiRequest.getFile("uploadImage");
		}
		
		if(multiPart != null) {
			byte[] image = IOUtils.toByteArray(multiPart.getInputStream());
			imagesList.add(image);
		}
		if(!StringUtils.isBlank(imageUrl)) {
			byte[] image = getImage(imageUrl);
			if(image != null && image.length > 0) {
				imagesList.add(image);
			}
		}
		if(MODE_TMP_UPLOAD.equals(mode)) {
			for(byte[] image : imagesList) {
				final File tmpFile = File.createTempFile(RandomStringUtils.randomAlphabetic(5), "", uploadDir);
				try {
					if("true".equals(req.getParameter("isEncrease"))) {
						ImageInfo imageInfo = converter.getImageInfo(image);
						int size = imageInfo.getWidth() > imageInfo.getHeigth() ? imageInfo.getWidth() : imageInfo.getHeigth();
						image = converter.increaseImage(image, size, size, false);
					}
					IOUtils.write(image, new FileOutputStream(tmpFile));
					
					JsonData data = response.createJsonData();
					data.put("path", "/tmpfile/" + tmpFile.getName() + "/");
					data.put("fileName", tmpFile.getName());
				} catch(Exception e) {
					tmpFile.delete();
				}
			}
		} else if(MODE_UPLOAD.equals(mode)) {
			ImageDir dir = ImageDir.getDir(req.getParameter("type"));
			if(dir == null) {
				return response;
			}
			for(byte[] image : imagesList) {
				if(dir == ImageDir.NEWS_CONTENT) {
					uploadArticleContentImage(req, image, dir);
				}
			}
		}
		response.setStatus("success");
		return response;
	}
	
	@RequestMapping(value="/editor/upload_image/save", method = RequestMethod.POST)
	@ResponseBody
	public JsonResponse saveImage(HttpServletRequest req, 
							  @RequestParam("type") String type,
							  @RequestParam("tmpFile") String tmpFile) throws IOException {
		JsonResponse response = new JsonResponse();
		
		List<byte[]> imagesList = new ArrayList<byte[]>();
		if(!StringUtils.isBlank(tmpFile)) {
			File file = new File(uploadDir, tmpFile);
			byte[] image = getBytes(file);
			if(image != null) {
				imagesList.add(image);
			}
		}
		
		ImageDir dir = ImageDir.getDir(req.getParameter("type"));
		if(dir == null) {
			return response;
		}
		for(byte[] image : imagesList) {
			if (dir == ImageDir.NEWS_PREVIEW) {
				uploadArticlePreviewImage(req, image, dir);
			} else if (dir == ImageDir.ATHLETE_MIDDLE) {
				uploadAthleteMiddleImage(req, image, dir);
			} else if (dir == ImageDir.BANNER_IMAGE) {
				uploadBannerImage(req, image, dir);
			}
		}
		response.setStatus("success");
		return response;
	}
	
	@RequestMapping(value="/editor/upload_image/remove", method = RequestMethod.POST)
	@ResponseBody
	public void removeImage(HttpServletRequest req, 
							  @RequestParam("type") String type,
							  @RequestParam("id") Integer id) throws IOException {
		ImageDir dir = ImageDir.getDir(req.getParameter("type"));
		if(dir == null) {
			return;
		}
		
		if(dir == ImageDir.NEWS_CONTENT) {
			removeArticleContentImage(id);
		} else if (dir == ImageDir.NEWS_PREVIEW) {
			removeArticlePreviewImage(id, dir);
		} else if (dir == ImageDir.ATHLETE_MIDDLE) {
			removeAthleteMiddleImage(id, dir);
		} else if (dir == ImageDir.BANNER_IMAGE) {
			removeBannerImage(id, dir);
		}
	}
	
	private <T extends HasImage> T uploadImage(T item, ImageDir dir, HttpServletRequest req, byte[] image) {
		int x = ServletRequestUtils.getIntParameter(req, "x", 0);
		int y = ServletRequestUtils.getIntParameter(req, "y", 0);
		int widthCrop = ServletRequestUtils.getIntParameter(req, "width", 0);
		int heightCrop = ServletRequestUtils.getIntParameter(req, "height", 0);
		
		if(item != null) {
			byte[] cropImage = converter.cropImage(image, x, y, widthCrop, heightCrop);
			
			int ts = uploadImageHelper.uploadImage(dir, ImageFormat.getImageFormats(dir, false), cropImage, item.getId());
			item.setImage(ts);
		}
		return item;
	}
	
	private void uploadArticleContentImage(HttpServletRequest req, byte[] image, ImageDir dir) {
		int id = ServletRequestUtils.getIntParameter(req, "id", 0);
		Article article = applicationService.findById(new Article(id));
		if(article != null) {
			ArticleImage ai = new ArticleImage();
			ai.setArticle(article);
			ArticleImage articleImage = applicationService.save(ai);
			uploadImageHelper.uploadImage(dir, ImageFormat.getImageFormats(dir), image, articleImage.getId());
		}
	}
	
	private void removeArticleContentImage(Integer id) {
		if(id != null) {
			ArticleImage articleImage = applicationService.findById(new ArticleImage(id));
			if(articleImage != null) {
				applicationService.remove(articleImage);
			}
		}
	}
	
	private void uploadArticlePreviewImage(HttpServletRequest req, byte[] image, ImageDir imageDir) {
		int id = ServletRequestUtils.getIntParameter(req, "id", 0);
		Article article = applicationService.findById(new Article(id));
		article = uploadImage(article, imageDir, req, image);
		applicationService.save(article);
	}
	
	private void removeArticlePreviewImage(Integer id, ImageDir imageDir) {
		if(id != null) {
			Article article = applicationService.findById(new Article(id));
			if(article != null) {
				uploadImageHelper.removeImage(imageDir, ImageFormat.getImageFormats(imageDir), article.getId());
				article.setImage(null);
				applicationService.save(article);
			}
		}
	}
	
	private void uploadAthleteMiddleImage(HttpServletRequest req, byte[] image, ImageDir imageDir) {
		int id = ServletRequestUtils.getIntParameter(req, "id", 0);
		Athlete athlete = personService.findById(new Athlete(id));
		athlete = uploadImage(athlete, imageDir, req, image);
		personService.save(athlete);
	}
	
	private void removeAthleteMiddleImage(Integer id, ImageDir imageDir) {
		if(id != null) {
			Athlete athlete = personService.findById(new Athlete(id));
			if(athlete != null) {
				uploadImageHelper.removeImage(imageDir, ImageFormat.getImageFormats(imageDir), athlete.getId());
				athlete.setImage(null);
				personService.save(athlete);
			}
		}
	}

	private void uploadBannerImage(HttpServletRequest req, byte[] image, ImageDir imageDir) {
		int id = ServletRequestUtils.getIntParameter(req, "id", 0);

		BannerImage bannerImage = applicationService.findById(new BannerImage(id));
		if(bannerImage != null) {
			bannerImage = uploadImage(bannerImage, imageDir, req, image);
			applicationService.save(bannerImage);
		}
	}

	private void removeBannerImage(Integer id, ImageDir imageDir) {
		if(id != null) {
			BannerImage bannerImage = applicationService.findById(new BannerImage(id));
			if(bannerImage != null) {
				bannerImage.setImage(null);
				uploadImageHelper.removeImage(imageDir, ImageFormat.getImageFormats(imageDir), bannerImage.getId());
				applicationService.save(bannerImage);
			}
		}
	}
	
//	private void uploadSettingsMenuImage(HttpServletRequest req, byte[] image, ImageDir imageDir) {
//		Settings settings = applicationService.getSettings();
//		settings = uploadImage(settings, imageDir, req, image);
//		applicationService.save(settings);
//	}
//	
//	private void removeSettingsMenuImage(Integer id, ImageDir imageDir) {
//		Settings settings = applicationService.getSettings();
//		if(settings != null) {
//			uploadImageHelper.removeImage(imageDir, ImageFormat.getImageFormats(imageDir), settings.getId());
//			settings.setImage(null);
//			applicationService.save(settings);
//		}
//	}
	
	protected byte[] getImage(String url) {
		try {
			if(url.indexOf("http") < 0) {
				url += "http://" + url;
			}
			HttpClient httpClient = new HttpClient();
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(10000);
			
			GetMethod get = new GetMethod(url);
			get.addRequestHeader("Accept-Language", "ru-ru,ru;q=0.8,en-us;q=0.5,en;q=0.3");
			get.addRequestHeader("Accept-Encoding", "gzip, deflate");
			get.addRequestHeader("Connection", "keep-alive");
			get.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:16.0) Gecko/20100101 Firefox/16.0");
			httpClient.executeMethod(get);
			
			InputStream is = get.getResponseBodyAsStream();
			RenderedOp image = JAI.create("stream", SeekableStream.wrapInputStream(is, true));
			if(image.getWidth() > 0 && image.getHeight() > 0) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
        		ImageIO.write(image.getAsBufferedImage(), "jpg", baos);
        		return baos.toByteArray();
			}
		} catch (Exception e) {
			LOGGER.error("Dont upload image by url: " + url, e);
		}
		return null;
	}
	
	protected byte[] getBytes(File output) throws IOException {
		if (output.length() > 0) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(output);
				ByteArrayOutputStream fos = new ByteArrayOutputStream();
				IOUtils.copy(fis, fos);
				return fos.toByteArray();
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						LOGGER.error("IO", e);
					}
				}
			}
		}
		return null;
	}
	
}
