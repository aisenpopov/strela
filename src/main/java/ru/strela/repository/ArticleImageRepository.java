package ru.strela.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.strela.model.Article;
import ru.strela.model.ArticleImage;

@Repository
public interface ArticleImageRepository extends JpaRepository<ArticleImage, Integer> {
	
	List<ArticleImage> findByArticle(Article article);

}
