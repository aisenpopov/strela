package ru.strela.repository.spec;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import ru.strela.model.BannerImage;
import ru.strela.model.filter.BannerImageFilter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class BannerImageSpec {
    public static Specification<BannerImage> filter(final BannerImageFilter filter) {
        return new Specification<BannerImage>() {

			public Predicate toPredicate(Root<BannerImage> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                query.distinct(true);
                
                if (filter.getType() != null) {
                	predicates.add(builder.equal(root.get("type"), filter.getType()));
                }
                
                if(filter.getVisible() != null) {
                    predicates.add(builder.equal(root.get("visible"), filter.getVisible()));
                }

                if(StringUtils.isNotBlank(filter.getQuery())) {
                    predicates.add(builder.like(builder.lower(root.get("name").as(String.class)),
                            "%" + filter.getQuery().toLowerCase() + "%"));
                }
                
                return builder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
