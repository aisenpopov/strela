package ru.strela.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.StringUtils;

import ru.strela.util.TranslitHelper;

@MappedSuperclass
public abstract class BaseEntitySeo extends BaseEntityNamed {

	private String path;
    private String htmlTitle;
	private String metaDescription;
	private String metaKeywords;
		
	@Column(nullable = false)
	public String getPath() {
		if(StringUtils.isBlank(path)) {
			path = TranslitHelper.translit(name);
		}
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}

	public String getHtmlTitle() {
		return htmlTitle;
	}
	
	public void setHtmlTitle(String htmlTitle) {
		this.htmlTitle = htmlTitle;
	}
	
	@Column(columnDefinition="TEXT")
	public String getMetaKeywords() {
		return metaKeywords;
	}
	
	public void setMetaKeywords(String metaKeywords) {
		this.metaKeywords = metaKeywords;
	}
	
	@Column(columnDefinition="TEXT")
	public String getMetaDescription() {
		return metaDescription;
	}
	
	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}
	
}
