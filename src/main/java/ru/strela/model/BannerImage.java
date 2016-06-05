package ru.strela.model;

import javax.persistence.*;

@Entity
@Table(name = "banner_image")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class BannerImage extends BaseEntitySortable implements HasImage {
	
	public enum Type {
		
		slider("Слайдер");

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
	
	private Type type;
	private String link;
    private Integer image;
	private boolean visible;

    public BannerImage() {
    }

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

	@Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
