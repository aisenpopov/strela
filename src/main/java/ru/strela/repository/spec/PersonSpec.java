package ru.strela.repository.spec;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import ru.strela.model.auth.Person;
import ru.strela.model.filter.PersonFilter;

public class PersonSpec {
    public static Specification<Person> filter(final PersonFilter filter) {
        return new Specification<Person>() {

			public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = new ArrayList<Predicate>();

                if(StringUtils.isNotBlank(filter.getLogin())) {
                    predicates.add(builder.equal(root.get("login"), filter.getLogin()));
                }
                if(StringUtils.isNotBlank(filter.getQuery())) {
                    predicates.add(builder.like(builder.lower(root.get("login").as(String.class)),
                            "%" + filter.getQuery().toLowerCase() + "%"));
                }
                
                return builder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
