package ru.strela.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import ru.strela.util.DateUtils;


@Entity
@Table(name = "article")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class Article extends BaseEntitySeo implements HasImage {

	public enum Type {
		news("Новость"),
		static_page("Статическая страница");
					
		private String title;

		private Type(String title) {
			this.title = title;
		}
			
		public String getTitle() {
			return title;
		}
	}
	
	private Type type;
    private Date publish;
	private String shortText;
    private String text;
    private Integer image;    

    public Article() {}

    public Article(int id) {
       this.id = id;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Date getPublish() {
		return publish;
	}
	
	public void setPublish(Date publish) {
		this.publish = publish;
	}

	@Column(columnDefinition="TEXT")
    public String getShortText() {
		return shortText;
	}

	public void setShortText(String shortText) {
		this.shortText = shortText;
	}

	@Column(columnDefinition="TEXT")
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getImage() {
		return image;
	}

    @Override
	public void setImage(Integer image) {
		this.image = image;
	}
	
	@Transient
	public String getPrintPublish() {
		return DateUtils.format(this.publish);
	}
	
}
