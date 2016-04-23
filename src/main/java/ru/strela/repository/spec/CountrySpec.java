package ru.strela.repository.spec;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import ru.strela.model.Country;
import ru.strela.model.filter.CountryFilter;

public class CountrySpec {
    public static Specification<Country> filter(final CountryFilter filter) {
        return new Specification<Country>() {

			public Predicate toPredicate(Root<Country> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
              	query.distinct(true);
              	
                if(StringUtils.isNotBlank(filter.getQuery())) {
                    predicates.add(builder.like(builder.lower(root.get("name").as(String.class)),
                            "%" + filter.getQuery().toLowerCase() + "%"));
                }
                
                return builder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
