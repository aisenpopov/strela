package ru.strela.repository.spec.payment;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import ru.strela.model.filter.PermissionFilter;
import ru.strela.model.payment.Tariff;
import ru.strela.model.filter.payment.TariffFilter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class TariffSpec {
    public static Specification<Tariff> filter(final TariffFilter filter) {
        return new Specification<Tariff>() {

			public Predicate toPredicate(Root<Tariff> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
              	query.distinct(true);
              	
                if (StringUtils.isNotBlank(filter.getQuery())) {
                    predicates.add(builder.like(builder.lower(root.get("name").as(String.class)),
                            "%" + filter.getQuery().toLowerCase() + "%"));
                }

                if (filter.getGym() != null) {
                    predicates.add(builder.equal(root.get("gym").get("id"), filter.getGym().getId()));
                }

                PermissionFilter permissionFilter = filter.getPermissionFilter();
                if (permissionFilter != null && permissionFilter.getTeam() != null) {
                    predicates.add(builder.equal(root.get("gym").get("team").get("id"), permissionFilter.getTeam().getId()));
                }
                
                return builder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
