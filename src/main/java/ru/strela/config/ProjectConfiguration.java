package ru.strela.config;

import org.springframework.beans.factory.annotation.Value;

public class ProjectConfiguration {

	@Value("${upload.dir}")
	private String uploadDir;
	
	@Value("${image.dir}")
	private String imageDir;
	
	@Value("${image.upload.dir}")
	private String imageUploadDir;
	
	@Value("${domain}")
	private String domain;
	
	public String getUploadDir() {
		return uploadDir;
	}

	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}

	public String getImageDir() {
		return imageDir;
	}
	
	public void setImageDir(String imageDir) {
		this.imageDir = imageDir;
	}
	
	public String getImageUploadDir() {
		return imageUploadDir;
	}
	
	public void setImageUploadDir(String imageUploadDir) {
		this.imageUploadDir = imageUploadDir;
	}
	
	public String getDomain() {
		return domain;
	}
	
	public void setDomain(String domain) {
		this.domain = domain;
	}
}
