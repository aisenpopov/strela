package ru.strela.repository.spec;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import ru.strela.model.Gym;
import ru.strela.model.filter.GymFilter;
import ru.strela.model.filter.PermissionFilter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class GymSpec {
    public static Specification<Gym> filter(final GymFilter filter) {
        return new Specification<Gym>() {

			public Predicate toPredicate(Root<Gym> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
              	query.distinct(true);

                if (filter.getVisible() != null) {
                    predicates.add(builder.equal(root.get("article").get("visible"), filter.getVisible()));
                }

                if (StringUtils.isNotBlank(filter.getQuery())) {
                    predicates.add(builder.like(builder.lower(root.get("name").as(String.class)),
                            "%" + filter.getQuery().toLowerCase() + "%"));
                }

                if (filter.getTeam() != null) {
                    predicates.add(builder.equal(root.get("team").get("id"), filter.getTeam().getId()));
                }

                if (filter.getCity() != null) {
                    predicates.add(builder.equal(root.get("city").get("id"), filter.getCity().getId()));
                }

                PermissionFilter permissionFilter = filter.getPermissionFilter();
                if (permissionFilter != null && permissionFilter.getTeam() != null) {
                    predicates.add(builder.equal(root.get("team").get("id"), permissionFilter.getTeam().getId()));
                }
                
                return builder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
