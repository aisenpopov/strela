package ru.strela.repository.spec;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import ru.strela.model.Athlete;
import ru.strela.model.filter.AthleteFilter;
import ru.strela.model.filter.PermissionFilter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class AthleteSpec extends Spec {
    public static Specification<Athlete> filter(final AthleteFilter filter) {
        return new Specification<Athlete>() {

			public Predicate toPredicate(Root<Athlete> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = new ArrayList<Predicate>();

                if(StringUtils.isNotBlank(filter.getQuery())) {
                    fillAthleteDisplayNamePredicates(builder, predicates, filter, root);
                }
                if (filter.getInstructor() != null) {
                    predicates.add(builder.equal(root.get("instructor"), filter.getInstructor()));
                }

                PermissionFilter permissionFilter = filter.getPermissionFilter();
                if (permissionFilter != null && permissionFilter.getTeam() != null) {
                    predicates.add(builder.or(builder.equal(root.get("team").get("id"), permissionFilter.getTeam().getId()), builder.isNull(root.get("team"))));
                }

                return builder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
