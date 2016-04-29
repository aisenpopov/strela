package ru.strela.service.impl;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.strela.config.ProjectConfiguration;
import ru.strela.model.*;
import ru.strela.model.filter.*;
import ru.strela.repository.*;
import ru.strela.repository.spec.*;
import ru.strela.service.ApplicationService;
import ru.strela.util.PageRequestBuilder;
import ru.strela.util.image.ImageDir;
import ru.strela.util.image.ImageFormat;
import ru.strela.util.image.UploadImageHelper;

import java.util.List;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService, InitializingBean{
	
	@Autowired
    private ArticleRepository articleRepository;
	
	@Autowired
	private ArticleImageRepository articleImageRepository;
	
	@Autowired
	private ArticleVideoRepository articleVideoRepository;
	
	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private SettingsRepository settingsRepository;
	
	@Autowired
	private RegistrationRegionRepository registrationRegionRepository;
	
	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private GymRepository gymRepository;

	@Autowired
	private TariffRepository tariffRepository;
	
//	@Autowired
//	private GalleryImageRepository galleryImageRepository;
//	
//	@Autowired
//	private MainImageRepository mainImageRepository;
//	

	@Autowired
	private UploadImageHelper uploadImageHelper;

	@Autowired
	private ProjectConfiguration projectConfiguration;
	
	@Override
    public Article save(Article article) {
        return articleRepository.save(article);
    }

    @Override
    public void remove(Article article) {
    	for(ArticleImage articleImage : getArticleImages(article)) {
    		remove(articleImage);
    	}

    	uploadImageHelper.removeImage(ImageDir.ARTICLE_PREVIEW, ImageFormat.getImageFormats(ImageDir.ARTICLE_PREVIEW), article.getId());
        articleRepository.delete(article);
    }

    @Override
    public Article findById(Article article) {
        return articleRepository.findOne(article.getId());
    }
    
    @Override
    public Article findOneArticle(ArticleFilter filter) {
        return articleRepository.findOne(ArticleSpec.filter(filter));
    }

    @Override
    public Page<Article> findArticles(ArticleFilter filter, int pageNumber, int pageSize) {
        return articleRepository.findAll(ArticleSpec.filter(filter), PageRequestBuilder.build(filter, pageNumber, pageSize));
    }
    
    @Override
    public List<Article> findArticles(ArticleFilter filter) {
        return articleRepository.findAll(ArticleSpec.filter(filter), PageRequestBuilder.getSort(filter));
    }
    
    
    @Override
	public ArticleImage findById(ArticleImage articleImage) {
		return articleImageRepository.findOne(articleImage.getId());
	}

	@Override
	public void remove(ArticleImage articleImage) {
		uploadImageHelper.removeImage(ImageDir.ARTICLE_CONTENT, ImageFormat.getImageFormats(ImageDir.ARTICLE_CONTENT), articleImage.getId());
		articleImageRepository.delete(articleImage);
	}

	@Override
	public ArticleImage save(ArticleImage articleImage) {
		return articleImageRepository.save(articleImage);
	}

	@Override
	public List<ArticleImage> getArticleImages(Article article) {
		return articleImageRepository.findByArticle(article);
	}
	
	
	@Override
	public ArticleVideo findById(ArticleVideo articleVideo) {
		return articleVideoRepository.findOne(articleVideo.getId());
	}

	@Override
	public void remove(ArticleVideo articleVideo) {
		articleVideoRepository.delete(articleVideo);
	}

	@Override
	public ArticleVideo save(ArticleVideo articleVideo) {
		return articleVideoRepository.save(articleVideo);
	}

	@Override
	public List<ArticleVideo> getArticleVideos(Article article) {
		return articleVideoRepository.findByArticle(article);
	}
	
	
	@Override
	public City save(City city) {
		return cityRepository.save(city);
	}

	@Override
	public void remove(City city) {
		cityRepository.delete(city);
	}

	@Override
	public City findById(City city) {
		return cityRepository.findOne(city.getId());
	}

	@Override
	public City findOneCity(CityFilter filter) {
		return cityRepository.findOne(CitySpec.filter(filter));
	}

	@Override
	public Page<City> findCities(CityFilter filter, int pageNumber, int pageSize) {
		return cityRepository.findAll(CitySpec.filter(filter), PageRequestBuilder.build(filter, pageNumber, pageSize));
	}

	@Override
	public List<City> findCities(CityFilter filter) {
		return cityRepository.findAll(CitySpec.filter(filter), PageRequestBuilder.getSort(filter));
	}

	@Override
	public Country save(Country country) {
		return countryRepository.save(country);
	}

	
	@Override
	public void remove(Country country) {
		countryRepository.delete(country);
	}

	@Override
	public Country findById(Country country) {
		return countryRepository.findOne(country.getId());
	}

	@Override
	public Country findOneCountry(CountryFilter filter) {
		return countryRepository.findOne(CountrySpec.filter(filter));
	}

	@Override
	public Page<Country> findCountries(CountryFilter filter, int pageNumber, int pageSize) {
		return countryRepository.findAll(CountrySpec.filter(filter), PageRequestBuilder.build(filter, pageNumber, pageSize));
	}

	@Override
	public List<Country> findCountries(CountryFilter filter) {
		return countryRepository.findAll(CountrySpec.filter(filter), PageRequestBuilder.getSort(filter));
	}

	
	@Override
	public RegistrationRegion save(RegistrationRegion registrationRegion) {
		return registrationRegionRepository.save(registrationRegion);
	}

	@Override
	public void remove(RegistrationRegion registrationRegion) {
		registrationRegionRepository.delete(registrationRegion);
	}

	@Override
	public RegistrationRegion findById(RegistrationRegion registrationRegion) {
		return registrationRegionRepository.findOne(registrationRegion.getId());
	}

	@Override
	public RegistrationRegion findOneRegistrationRegion(BaseFilter filter) {
		return registrationRegionRepository.findOne(RegistrationRegionSpec.filter(filter));
	}

	@Override
	public Page<RegistrationRegion> findRegistrationRegions(BaseFilter filter, int pageNumber, int pageSize) {
		return registrationRegionRepository.findAll(RegistrationRegionSpec.filter(filter), PageRequestBuilder.build(filter, pageNumber, pageSize));
	}

	@Override
	public List<RegistrationRegion> findRegistrationRegions(BaseFilter filter) {
		return registrationRegionRepository.findAll(RegistrationRegionSpec.filter(filter), PageRequestBuilder.getSort(filter));
	}

	
	@Override
	public Team save(Team team) {
		return teamRepository.save(team);
	}

	@Override
	public void remove(Team team) {
		teamRepository.delete(team);
	}

	@Override
	public Team findById(Team team) {
		return teamRepository.findOne(team.getId());
	}

	@Override
	public Page<Team> findTeams(TeamFilter filter, int pageNumber, int pageSize) {
		return teamRepository.findAll(TeamSpec.filter(filter), PageRequestBuilder.build(filter, pageNumber, pageSize));
	}

	@Override
	public List<Team> findTeams(TeamFilter filter) {
		return teamRepository.findAll(TeamSpec.filter(filter), PageRequestBuilder.getSort(filter));
	}


	@Override
	public Gym save(Gym gym) {
		return gymRepository.save(gym);
	}

	@Override
	public void remove(Gym gym) {
		gymRepository.delete(gym);
	}

	@Override
	public Gym findById(Gym gym) {
		return gymRepository.findOne(gym.getId());
	}

	@Override
	public Page<Gym> findGyms(GymFilter filter, int pageNumber, int pageSize) {
		return gymRepository.findAll(GymSpec.filter(filter), PageRequestBuilder.build(filter, pageNumber, pageSize));
	}

	@Override
	public List<Gym> findGyms(GymFilter filter) {
		return gymRepository.findAll(GymSpec.filter(filter), PageRequestBuilder.getSort(filter));
	}

	@Override
	public Tariff save(Tariff tariff) {
		return tariffRepository.save(tariff);
	}

	@Override
	public void remove(Tariff tariff) {
		tariffRepository.delete(tariff);
	}

	@Override
	public Tariff findById(Tariff tariff) {
		return tariffRepository.findOne(tariff.getId());
	}

	@Override
	public Page<Tariff> findTariffs(TariffFilter filter, int pageNumber, int pageSize) {
		return tariffRepository.findAll(TariffSpec.filter(filter), PageRequestBuilder.build(filter, pageNumber, pageSize));
	}

	@Override
	public List<Tariff> findTariffs(TariffFilter filter) {
		return tariffRepository.findAll(TariffSpec.filter(filter), PageRequestBuilder.getSort(filter));
	}

	@Override
    public Settings save(Settings settings) {
        return settingsRepository.save(settings);
    }
	
	@Override
    public Settings getSettings() {
		List<Settings> settings = settingsRepository.findAll();
        if(!settings.isEmpty()) {
        	return settings.get(0);
        }

        return null;
    }
	 
	@Override
	public void afterPropertiesSet() throws Exception {
		List<Settings> list = settingsRepository.findAll();
        if(list.isEmpty()) {
        	Settings settings = new Settings();
        	save(settings);
        }        
        
	}
}
