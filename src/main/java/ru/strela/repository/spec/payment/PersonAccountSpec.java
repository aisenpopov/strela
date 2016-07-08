package ru.strela.repository.spec.payment;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import ru.strela.model.filter.payment.PersonAccountFilter;
import ru.strela.model.payment.PersonAccount;
import ru.strela.repository.spec.Spec;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aisen on 08.07.2016.
 */
public class PersonAccountSpec extends Spec {
    public static Specification<PersonAccount> filter(final PersonAccountFilter filter) {
        return new Specification<PersonAccount>() {

            public Predicate toPredicate(Root<PersonAccount> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = new ArrayList<Predicate>();

                if (StringUtils.isNotBlank(filter.getQuery())) {
                    predicates.add(builder.like(builder.lower(root.get("person").get("login").as(String.class)),
                            "%" + filter.getQuery().toLowerCase() + "%"));
                }

                if (filter.getPerson() != null) {
                    predicates.add(builder.equal(root.get("person").get("id"), filter.getPerson().getId()));
                }

                return builder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
