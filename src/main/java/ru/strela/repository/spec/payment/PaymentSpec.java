package ru.strela.repository.spec.payment;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import ru.strela.model.filter.PermissionFilter;
import ru.strela.model.filter.payment.PaymentFilter;
import ru.strela.model.payment.Payment;
import ru.strela.repository.spec.Spec;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentSpec extends Spec {
    public static Specification<Payment> filter(final PaymentFilter filter) {
        return new Specification<Payment>() {

			public Predicate toPredicate(Root<Payment> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
              	query.distinct(true);
              	
                if (StringUtils.isNotBlank(filter.getQuery())) {
                    Expression<String> firstName = root.get("athleteTariff").get("athlete").get("firstName").as(String.class);
                    Expression<String> lastName = root.get("athleteTariff").get("athlete").get("lastName").as(String.class);
                    Expression<String> middleName = root.get("athleteTariff").get("athlete").get("middleName").as(String.class);

                    fillDisplayNamePredicates(builder, predicates, filter, firstName, lastName, middleName);
                }

                if (filter.getAthleteTariff() != null) {
                    predicates.add(builder.equal(root.get("athleteTariff").get("id"), filter.getAthleteTariff().getId()));
                }

                PermissionFilter permissionFilter = filter.getPermissionFilter();
                if (permissionFilter != null && permissionFilter.getTeam() != null) {
                    predicates.add(builder.equal(root.get("athleteTariff").get("athlete").get("team").get("id"), permissionFilter.getTeam().getId()));
                }
                
                return builder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
