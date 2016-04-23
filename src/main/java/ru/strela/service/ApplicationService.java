package ru.strela.service;

import java.util.List;

import org.springframework.data.domain.Page;

import ru.strela.model.Article;
import ru.strela.model.ArticleImage;
import ru.strela.model.ArticleVideo;
import ru.strela.model.City;
import ru.strela.model.Country;
import ru.strela.model.RegistrationRegion;
import ru.strela.model.Settings;
import ru.strela.model.Team;
import ru.strela.model.filter.ArticleFilter;
import ru.strela.model.filter.BaseFilter;
import ru.strela.model.filter.CityFilter;
import ru.strela.model.filter.CountryFilter;
import ru.strela.model.filter.TeamFilter;

public interface ApplicationService {
	
	Article save(Article article);

    void remove(Article article);

    Article findById(Article article);
    
    Article findOneArticle(ArticleFilter filter);

    Page<Article> findArticles(ArticleFilter filter, int pageNumber, int pageSize);
    
    List<Article> findArticles(ArticleFilter filter);
    
    
    void remove(ArticleImage articleImage);

	ArticleImage save(ArticleImage articleImage);

	List<ArticleImage> getArticleImages(Article article);
	
	ArticleImage findById(ArticleImage articleImage);
	
	
	ArticleVideo findById(ArticleVideo articleVideo);

	void remove(ArticleVideo articleVideo);

	ArticleVideo save(ArticleVideo articleVideo);

	List<ArticleVideo> getArticleVideos(Article article);
	
	
	City save(City city);

    void remove(City city);

    City findById(City city);
    
    City findOneCity(CityFilter filter);

    Page<City> findCities(CityFilter filter, int pageNumber, int pageSize);
    
    List<City> findCities(CityFilter filter);
    
    
    Country save(Country country);

    void remove(Country country);

    Country findById(Country country);
    
    Country findOneCountry(CountryFilter filter);

    Page<Country> findCountries(CountryFilter filter, int pageNumber, int pageSize);
    
    List<Country> findCountries(CountryFilter filter);
    
    
    RegistrationRegion save(RegistrationRegion registrationRegion);

    void remove(RegistrationRegion registrationRegion);

    RegistrationRegion findById(RegistrationRegion registrationRegion);
    
    RegistrationRegion findOneRegistrationRegion(BaseFilter filter);

    Page<RegistrationRegion> findRegistrationRegions(BaseFilter filter, int pageNumber, int pageSize);
    
    List<RegistrationRegion> findRegistrationRegions(BaseFilter filter);
    
    
    Team save(Team team);

    void remove(Team team);

    Team findById(Team team);
    
    Page<Team> findTeams(TeamFilter filter, int pageNumber, int pageSize);
    
    List<Team> findTeams(TeamFilter filter);
	
	
//	MainImage save(MainImage mainImage);
//
//	void remove(MainImage mainImage);
//
//	MainImage findById(MainImage mainImage);
//
//	MainImage findOneMainImage(MainImageFilter filter);
//
//	Page<MainImage> findMainImages(MainImageFilter filter, int pageNumber, int pageSize);
//
//	List<MainImage> findMainImages(MainImageFilter filter);
//
//	void sortMainImage(Type type, final MainImage mainImage, final boolean isUp);
	
	Settings save(Settings settings);
	
	Settings getSettings();

}
