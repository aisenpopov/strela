package ru.strela.repository.spec.payment;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import ru.strela.model.filter.payment.AthleteTariffFilter;
import ru.strela.model.payment.AthleteTariff;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class AthleteTariffSpec {
    public static Specification<AthleteTariff> filter(final AthleteTariffFilter filter) {
        return new Specification<AthleteTariff>() {

			public Predicate toPredicate(Root<AthleteTariff> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
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
                
                return builder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
