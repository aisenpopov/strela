package ru.strela.model;

import javax.persistence.*;

@Entity
@Table(name = "banner_image")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class BannerImage extends BaseEntitySortable implements HasImage {
	
	public enum Type {
		
		photo("Фотография"),
		advert("Объявление");

		private String title;

		private Type(String title) {
			this.title = title;
		}
		
		public String getTitle() {
			return title;
		}
		
		public static Type getByName(String name) {
			for (Type type : Type.values()) {
				if (type.name().equals(name)) {
					return type;
				}
			}
			return null;
		}
		
    }

	/**
	 * Тип баннера.
	 */
	private Type type;

	/**
	 * Абсолютная ссылка баннера.
	 */
	private String link;

	/**
	 * Текст объявления, которое выводится поверх картинки.
	 */
	private String text;

    /**
	 * Если картинка загружена, то содержит случайное число.
	 * Используется для сброса кэша браузера при обновлении картинки.
	 */
	private Integer image;

	/**
	 * Показывается?
	 */
	private boolean visible;

    public BannerImage() {}

    public BannerImage(int id) {
       this.id = id;
    }
	
    public Integer getImage() {
		return image;
	}

    @Override
	public void setImage(Integer image) {
		this.image = image;
	}
    
	@Enumerated(EnumType.STRING)
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Transient
	public String getPrintTypeTitle() {
		return type != null ? type.getTitle() : null;
	}

	@Transient
	public String getPrintVisible() {
		return visible ? "Да" : "Нет";
	}

}
