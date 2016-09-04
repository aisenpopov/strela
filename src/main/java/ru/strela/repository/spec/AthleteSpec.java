package ru.strela.repository.spec;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import ru.strela.model.Athlete;
import ru.strela.model.filter.AthleteFilter;
import ru.strela.model.filter.PermissionFilter;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class AthleteSpec extends Spec {
    public static Specification<Athlete> filter(final AthleteFilter filter) {
        return new Specification<Athlete>() {

			public Predicate toPredicate(Root<Athlete> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = new ArrayList<Predicate>();

                if(StringUtils.isNotBlank(filter.getQuery())) {
                    Expression<String> firstName = root.get("firstName").as(String.class);
                    Expression<String> lastName = root.get("lastName").as(String.class);
                    Expression<String> middleName = root.get("middleName").as(String.class);

                    fillDisplayNamePredicates(builder, predicates, filter, firstName, lastName, middleName);
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
