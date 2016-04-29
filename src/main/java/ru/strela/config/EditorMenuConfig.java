package ru.strela.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.strela.editor.menu.EditorMenuBar;
import ru.strela.editor.menu.MenuItem;

@Configuration
public class EditorMenuConfig {
	
	@Bean
	public EditorMenuBar editorMenuBar() {
		EditorMenuBar menuBar = new EditorMenuBar();
		
		MenuItem main = new MenuItem();
		main.setCaption("Главная");
		main.setIcon("fa-image");
		main.setHref("#");
		menuBar.addItem(main);
		
		MenuItem directory = new MenuItem();
		directory.setCaption("Справочники");
		directory.setIcon("fa-book");
		directory.setHref("#");
		menuBar.addItem(directory);
		
		{
			MenuItem athlete = new MenuItem();
			athlete.setCaption("Атлеты");
			athlete.setHref("athlete");
			directory.addItem(athlete);
			
			MenuItem team = new MenuItem();
			team.setCaption("Команды");
			team.setHref("team");
			directory.addItem(team);

			MenuItem gym = new MenuItem();
			gym.setCaption("Залы");
			gym.setHref("gym");
			directory.addItem(gym);
			
			MenuItem registrationRegion = new MenuItem();
			registrationRegion.setCaption("Регионы регистрации");
			registrationRegion.setHref("registration_region");
			directory.addItem(registrationRegion);
			
			MenuItem country = new MenuItem();
			country.setCaption("Страны");
			country.setHref("country");
			directory.addItem(country);
			
			MenuItem city = new MenuItem();
			city.setCaption("Города");
			city.setHref("city");
			directory.addItem(city);
		}

		MenuItem payment = new MenuItem();
		payment.setCaption("Платежи");
		payment.setIcon("fa-money");
		payment.setHref("#");
		menuBar.addItem(payment);

		{
			MenuItem tariff = new MenuItem();
			tariff.setCaption("Тарифы");
			tariff.setHref("tariff");
			payment.addItem(tariff);
		}
		
		MenuItem news = new MenuItem();
		news.setCaption("Новости");
		news.setIcon("fa-newspaper-o");
		news.setHref("article/news");
		menuBar.addItem(news);
		
		MenuItem staticPages = new MenuItem();
		staticPages.setCaption("Статические страницы");
		staticPages.setIcon("fa-file-text");
		staticPages.setHref("article/static_page");
		menuBar.addItem(staticPages);
		
		MenuItem persons = new MenuItem();
		persons.setCaption("Пользователи");
		persons.setIcon("fa-users");
		persons.setHref("person");
		menuBar.addItem(persons);
		
		MenuItem settings = new MenuItem();
		settings.setCaption("Настройки");
		settings.setIcon("fa-gear");
		settings.setHref("settings");
		menuBar.addItem(settings);
		
		return menuBar;
	}

}
