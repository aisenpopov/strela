package ru.strela.repository.spec;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import ru.strela.model.Team;
import ru.strela.model.filter.PermissionFilter;
import ru.strela.model.filter.TeamFilter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class TeamSpec {
    public static Specification<Team> filter(final TeamFilter filter) {
        return new Specification<Team>() {

			public Predicate toPredicate(Root<Team> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
              	query.distinct(true);
              	
                if (StringUtils.isNotBlank(filter.getQuery())) {
                    predicates.add(builder.like(builder.lower(root.get("name").as(String.class)),
                            "%" + filter.getQuery().toLowerCase() + "%"));
                }

                if (filter.getChiefInstructor() != null) {
                    predicates.add(builder.equal(root.get("chiefInstructor").get("id"), filter.getChiefInstructor().getId()));
                }

                PermissionFilter permissionFilter = filter.getPermissionFilter();
                if (permissionFilter != null && permissionFilter.getTeam() != null) {
                    predicates.add(builder.equal(root.get("id"), permissionFilter.getTeam().getId()));
                }
                
                return builder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
