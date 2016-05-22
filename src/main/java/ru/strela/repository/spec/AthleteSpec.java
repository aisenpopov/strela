package ru.strela.repository.spec;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import ru.strela.model.Athlete;
import ru.strela.model.filter.AthleteFilter;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class AthleteSpec {
    public static Specification<Athlete> filter(final AthleteFilter filter) {
        return new Specification<Athlete>() {

			public Predicate toPredicate(Root<Athlete> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = new ArrayList<Predicate>();

                if(StringUtils.isNotBlank(filter.getQuery())) {
                    Expression<String> firstName = root.get("firstName").as(String.class);
                    Expression<String> lastName = root.get("lastName").as(String.class);
                    Expression<String> middleName = root.get("middleName").as(String.class);

                    Expression<String> displayNameFull = builder.concat(lastName, " ");
                    displayNameFull = builder.concat(displayNameFull, firstName);
                    displayNameFull = builder.concat(displayNameFull, " ");
                    displayNameFull = builder.concat(displayNameFull, middleName);

                    Expression<String> displayName = builder.concat(lastName, " ");
                    displayName = builder.concat(displayName, firstName);

                    predicates.add(builder.or(builder.and(builder.like(builder.lower(displayNameFull), "%" + filter.getQuery().toLowerCase() + "%"), builder.isNotNull(middleName)),
                                              builder.and(builder.like(builder.lower(displayName), "%" + filter.getQuery().toLowerCase() + "%"), builder.isNull(middleName))));
                }
                if (filter.getInstructor() != null) {
                    predicates.add(builder.equal(root.get("instructor"), filter.getInstructor()));
                }

                return builder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
