package ru.strela.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.strela.model.Article;
import ru.strela.model.ArticleVideo;

@Repository
public interface ArticleVideoRepository extends JpaRepository<ArticleVideo, Integer> {
	
	List<ArticleVideo> findByArticle(Article article);

}
