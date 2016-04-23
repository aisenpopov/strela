package ru.strela.model.filter;

import ru.strela.model.Article;
import ru.strela.model.Article.Type;


public class ArticleFilter extends BaseFilter {

	private String path;
	private Article currentArticle;
	private Boolean isNext;
	private Type type;
	
    public String getPath() {
		return path;
	}
    
    public void setPath(String path) {
		this.path = path;
	}

	public Article getCurrentArticle() {
		return currentArticle;
	}

	public void setCurrentArticle(Article currentArticle) {
		this.currentArticle = currentArticle;
	}

	public Boolean getIsNext() {
		return isNext;
	}

	public void setIsNext(Boolean isNext) {
		this.isNext = isNext;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
    
}