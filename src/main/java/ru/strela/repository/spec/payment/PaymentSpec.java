package ru.strela.repository.spec.payment;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import ru.strela.model.filter.payment.PaymentFilter;
import ru.strela.model.payment.Payment;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentSpec {
    public static Specification<Payment> filter(final PaymentFilter filter) {
        return new Specification<Payment>() {

			public Predicate toPredicate(Root<Payment> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
              	query.distinct(true);
              	
                if (StringUtils.isNotBlank(filter.getQuery())) {
                    Expression<String> displayName = builder.concat(root.get("athleteTariff").get("athlete").get("lastName").as(String.class), " ");
                    displayName = builder.concat(displayName, root.get("athleteTariff").get("athlete").get("firstName").as(String.class));
                    displayName = builder.concat(displayName, " ");
                    displayName = builder.concat(displayName, root.get("athleteTariff").get("athlete").get("middleName").as(String.class));
                    predicates.add(builder.like(builder.lower(displayName),
                            "%" + filter.getQuery().toLowerCase() + "%"));
                }

                if (filter.getAthleteTariff() != null) {
                    predicates.add(builder.equal(root.get("athleteTariff").get("id"), filter.getAthleteTariff().getId()));
                }
                
                return builder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
