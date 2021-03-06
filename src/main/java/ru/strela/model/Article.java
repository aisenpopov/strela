package ru.strela.model;

import ru.strela.util.DateUtils;
import ru.strela.util.image.ImageDir;
import ru.strela.util.image.ImageFormat;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "article")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class Article extends BaseEntitySeo implements HasImage {

	public enum Type {
		news("Новость"),
		static_page("Статическая страница"),
		gym("Зал");

		private String title;

		private Type(String title) {
			this.title = title;
		}
			
		public String getTitle() {
			return title;
		}

		public ImageFormat getImageFormat(boolean isPreview) {
			ImageFormat imageFormat = null;
			if (this == Type.news) {
				imageFormat = isPreview ? ImageFormat.NEWS_PREVIEW : ImageFormat.NEWS_CONTENT;
			} else if (this == Type.static_page) {
				imageFormat = ImageFormat.STATIC_PAGE_CONTENT;
			} else if (this == Type.gym) {
				imageFormat = isPreview ? ImageFormat.GYM_PREVIEW : ImageFormat.GYM_CONTENT;
			}

			return imageFormat;
		}

		public ImageDir getImageDir(boolean isPreview) {
			ImageDir imageDir = null;
			if (this == Type.news) {
				imageDir = isPreview ? ImageDir.NEWS_PREVIEW : ImageDir.NEWS_CONTENT;
			} else if (this == Type.static_page) {
				imageDir = ImageDir.STATIC_PAGE_CONTENT;
			} else if (this == Type.gym) {
				imageDir = isPreview ? ImageDir.GYM_PREVIEW : ImageDir.GYM_CONTENT;
			}

			return imageDir;
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
		return DateUtils.formatDayMonthYear(this.publish);
	}

}
