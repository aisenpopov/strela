package ru.strela.repository.spec.payment;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import ru.strela.model.filter.PermissionFilter;
import ru.strela.model.filter.payment.PaymentStatusFilter;
import ru.strela.model.payment.PaymentStatus;
import ru.strela.repository.spec.Spec;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class PaymentStatusSpec extends Spec {
    public static Specification<PaymentStatus> filter(final PaymentStatusFilter filter) {
        return new Specification<PaymentStatus>() {

			public Predicate toPredicate(Root<PaymentStatus> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
              	query.distinct(true);
              	
                if (StringUtils.isNotBlank(filter.getQuery())) {
                    fillAthleteDisplayNamePredicates(builder, predicates, filter, root.get("athlete"));
                }
                if (filter.getAthlete() != null) {
                    predicates.add(builder.equal(root.get("athlete").get("id"), filter.getAthlete().getId()));
                }
                if (filter.getGym() != null) {
                    predicates.add(builder.equal(root.get("gym").get("id"), filter.getGym().getId()));
                }

                PermissionFilter permissionFilter = filter.getPermissionFilter();
                if (permissionFilter != null && permissionFilter.getTeam() != null) {
                    predicates.add(builder.equal(root.get("athlete").get("team").get("id"), permissionFilter.getTeam().getId()));
                    predicates.add(builder.equal(root.get("gym").get("team").get("id"), permissionFilter.getTeam().getId()));
                }
                
                return builder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
