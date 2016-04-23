package ru.strela.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "article_video", indexes = {
		@Index(name = "article_video_article",  columnList="article_id")
})
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class ArticleVideo extends BaseEntity {
	
	private String link;
	private Article article;
	
	public ArticleVideo() {}

    public ArticleVideo(int id) {
        this.id = id;
    }
	
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
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
