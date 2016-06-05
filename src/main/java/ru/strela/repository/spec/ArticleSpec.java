package ru.strela.repository.spec;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import ru.strela.model.Article;
import ru.strela.model.filter.ArticleFilter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticleSpec {
    public static Specification<Article> filter(final ArticleFilter filter) {
        return new Specification<Article>() {

			public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
              	query.distinct(true);
              	
              	if (filter.getVisible() != null) {
                    predicates.add(builder.equal(root.get("visible"), filter.getVisible()));
                }

                if (filter.getType() != null) {
              		predicates.add(builder.equal(root.get("type"), filter.getType()));
              	}
              	
                if (filter.getPath() != null) {
                	predicates.add(builder.equal(root.get("path"), filter.getPath()));
                }
                
                if (StringUtils.isNotBlank(filter.getQuery())) {
                    predicates.add(builder.like(builder.lower(root.get("name").as(String.class)),
                            "%" + filter.getQuery().toLowerCase() + "%"));
                }
                
                if (filter.getCurrentArticle() != null && filter.getIsNext() != null) {
                	predicates.add(root.get("id").in(filter.getCurrentArticle().getId()).not());
                	if(filter.getIsNext()) {
                		predicates.add(builder.greaterThan(root.<Date>get("publish"), filter.getCurrentArticle().getPublish()));
                	} else {
                		predicates.add(builder.lessThan(root.<Date>get("publish"), filter.getCurrentArticle().getPublish()));
                	}
                }  
                
                return builder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
