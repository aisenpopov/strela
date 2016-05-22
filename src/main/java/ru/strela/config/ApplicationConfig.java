package ru.strela.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.flywaydb.core.Flyway;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import ru.strela.editor.menu.EditorMenuBuilder;
import ru.strela.util.image.Converter;
import ru.strela.util.image.ConverterImpl;
import ru.strela.util.image.UploadImageHelper;
import ru.strela.util.image.UploadImageHelperImpl;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

@Configuration
@ComponentScan(value={"ru.strela.service.impl"})
@EnableJpaRepositories(value={"ru.strela.repository"})
@EnableTransactionManagement
public class ApplicationConfig extends WebMvcConfigurerAdapter {

    private final static String[] MODEL_PACKAGES = new String[] {"ru.strela.model"};
    
    private static final String MIGRATIONS_FOLDER = "migrations";

    @Value("${db.driver}")
    private String driver;

    @Value("${db.url}")
    private String url;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    @Value("${hibernate.dialect}")
    private String dialect;
    
    @Bean(initMethod = "migrate")
    public Flyway flyway() throws PropertyVetoException {
    	Flyway flyway = new Flyway();
    	flyway.setBaselineOnMigrate(true);
    	flyway.setBaselineVersionAsString("0");
    	flyway.setLocations("classpath:" + MIGRATIONS_FOLDER);
    	flyway.setValidateOnMigrate(false);
    	flyway.setDataSource(dataSource());
    	return flyway;
    }

    @Bean
    public DataSource dataSource() throws PropertyVetoException {
        final ComboPooledDataSource ds = new ComboPooledDataSource();
        ds.setDriverClass(driver);
        ds.setJdbcUrl(url);
        ds.setUser(username);
        ds.setPassword(password);
        ds.setPreferredTestQuery("select 1");
        ds.setMinPoolSize(3);
        ds.setMaxPoolSize(20);
        ds.setAcquireIncrement(5);
        ds.setTestConnectionOnCheckin(true);
        ds.setIdleConnectionTestPeriod(300);
        ds.setMaxIdleTimeExcessConnections(240);
        return ds;
    }

    @Bean
    public JpaTransactionManager transactionManager() throws Exception {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws Exception {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPackagesToScan(MODEL_PACKAGES);
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", dialect);
        jpaProperties.put("hibernate.format_sql", false);
        jpaProperties.put("hibernate.ejb.naming_strategy", org.hibernate.cfg.ImprovedNamingStrategy.class);
        jpaProperties.put("hibernate.show_sql", false);
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        jpaProperties.put("hibernate.generate_statistics", "false");
        jpaProperties.put("hibernate.max_fetch_depth", "2");
        jpaProperties.put("hibernate.jdbc.batch_size", "50");
        jpaProperties.put("hibernate.cache.use_second_level_cache", "true");
        jpaProperties.put("hibernate.cache.use_query_cache", "false");
        jpaProperties.put("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        jpaProperties.put("hibernate.cache.provider_configuration_file_resource_path", "ehcache.xml");
        jpaProperties.put("hibernate.connection.release_mode", "auto");
        jpaProperties.put("hibernate.enable_lazy_load_no_trans", true);

        entityManagerFactoryBean.setJpaProperties(jpaProperties);

        return entityManagerFactoryBean;
    }
    
    @Bean
	public UploadImageHelper uploadImageHelper() {
		return new UploadImageHelperImpl();
	}
    
    @Bean
   	public ProjectConfiguration projectConfiguration() {
   		return new ProjectConfiguration();
   	}
       
    @Bean
   	public Converter converter() {
   		return new ConverterImpl();
   	}

    @Bean
    public EditorMenuBuilder editorMenuBuilder() {
        return new EditorMenuBuilder();
    }
    
}

