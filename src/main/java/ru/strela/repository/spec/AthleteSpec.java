package ru.strela.repository.spec;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import ru.strela.model.Athlete;
import ru.strela.model.filter.AthleteFilter;

public class AthleteSpec {
    public static Specification<Athlete> filter(final AthleteFilter filter) {
        return new Specification<Athlete>() {

			public Predicate toPredicate(Root<Athlete> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = new ArrayList<Predicate>();

                if(StringUtils.isNotBlank(filter.getQuery())) {
                    Expression<String> displayName = builder.concat(root.get("lastName").as(String.class), " ");
                    displayName = builder.concat(displayName, root.get("firstName").as(String.class));
                    displayName = builder.concat(displayName, " ");
                    displayName = builder.concat(displayName, root.get("middleName").as(String.class));
                    predicates.add(builder.like(builder.lower(displayName),
                            "%" + filter.getQuery().toLowerCase() + "%"));
                }
                return builder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
