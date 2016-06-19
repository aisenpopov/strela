package ru.strela.service;

import org.springframework.data.domain.Page;
import ru.strela.model.*;
import ru.strela.model.filter.*;

import java.util.List;

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

    List<City> findHasGym();
    
    
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


    Gym save(Gym gym);

    void remove(Gym gym);

    Gym findById(Gym gym);

    Page<Gym> findGyms(GymFilter filter, int pageNumber, int pageSize);

    List<Gym> findGyms(GymFilter filter);


    void remove(GymImage gymImage);

    GymImage save(GymImage gymImage);

    List<GymImage> getGymImages(Gym gym);

    GymImage findById(GymImage gymImage);


	BannerImage save(BannerImage bannerImage);

	void remove(BannerImage bannerImage);

	BannerImage findById(BannerImage bannerImage);

	BannerImage findOneBannerImage(BannerImageFilter filter);

	Page<BannerImage> findBannerImages(BannerImageFilter filter, int pageNumber, int pageSize);

	List<BannerImage> findBannerImages(BannerImageFilter filter);

	void sortBannerImage(final BannerImage bannerImage, final boolean isUp);
	
	Settings save(Settings settings);
	
	Settings getSettings();

}
