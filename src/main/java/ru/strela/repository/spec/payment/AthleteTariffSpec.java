package ru.strela.repository.spec.payment;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import ru.strela.model.filter.payment.AthleteTariffFilter;
import ru.strela.model.payment.AthleteTariff;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class AthleteTariffSpec {
    public static Specification<AthleteTariff> filter(final AthleteTariffFilter filter) {
        return new Specification<AthleteTariff>() {

			public Predicate toPredicate(Root<AthleteTariff> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
              	query.distinct(true);
              	
                if(StringUtils.isNotBlank(filter.getQuery())) {
                    predicates.add(builder.like(builder.lower(root.get("name").as(String.class)),
                            "%" + filter.getQuery().toLowerCase() + "%"));
                }
                
                return builder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
