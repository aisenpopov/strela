package ru.strela.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "article_image", indexes = {
		@Index(name = "article_image_article",  columnList="article_id")
})
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class ArticleImage extends BaseEntity {
	
	private Article article;
	
	public ArticleImage() {}

    public ArticleImage(int id) {
    	this.id = id;
    }
	
	@ManyToOne(targetEntity=Article.class, fetch=FetchType.LAZY)
	@JoinColumn(name="article_id", nullable=false)
	public Article getArticle() {
		return article;
	}
	
	public void setArticle(Article article) {
		this.article = article;
	}
	
}
