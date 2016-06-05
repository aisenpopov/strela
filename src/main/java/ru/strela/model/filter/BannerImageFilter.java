package ru.strela.model.filter;


import ru.strela.model.BannerImage;

public class BannerImageFilter extends BaseFilter {

	private BannerImage.Type type;
	
    public BannerImage.Type getType() {
		return type;
	}

	public void setType(BannerImage.Type type) {
		this.type = type;
	}
    
}