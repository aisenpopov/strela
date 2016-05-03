package ru.strela.repository.spec.payment;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import ru.strela.model.filter.payment.PaymentStatusFilter;
import ru.strela.model.payment.PaymentStatus;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentStatusSpec {
    public static Specification<PaymentStatus> filter(final PaymentStatusFilter filter) {
        return new Specification<PaymentStatus>() {

			public Predicate toPredicate(Root<PaymentStatus> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
              	query.distinct(true);
              	
                if (StringUtils.isNotBlank(filter.getQuery())) {
                    Expression<String> displayName = builder.concat(root.get("athlete").get("lastName").as(String.class), " ");
                    displayName = builder.concat(displayName, root.get("athlete").get("firstName").as(String.class));
                    displayName = builder.concat(displayName, " ");
                    displayName = builder.concat(displayName, root.get("athlete").get("middleName").as(String.class));
                    predicates.add(builder.like(builder.lower(displayName),
                            "%" + filter.getQuery().toLowerCase() + "%"));
                }
                if (filter.getAthlete() != null) {
                    predicates.add(builder.equal(root.get("athlete").get("id"), filter.getAthlete().getId()));
                }
                if (filter.getGym() != null) {
                    predicates.add(builder.equal(root.get("gym").get("id"), filter.getGym().getId()));
                }
                
                return builder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
