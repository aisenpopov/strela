package ru.strela.repository.spec;

import ru.strela.model.filter.BaseFilter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.List;

public class Spec {

    public static void fillDisplayNamePredicates(CriteriaBuilder builder, List<Predicate> predicates, BaseFilter filter,
                         Expression<String> firstName, Expression<String> lastName, Expression<String> middleName) {
        Expression<String> displayNameFull = builder.concat(lastName, " ");
        displayNameFull = builder.concat(displayNameFull, firstName);
        displayNameFull = builder.concat(displayNameFull, " ");
        displayNameFull = builder.concat(displayNameFull, middleName);

        Expression<String> displayName = builder.concat(lastName, " ");
        displayName = builder.concat(displayName, firstName);

        predicates.add(builder.or(builder.and(builder.like(builder.lower(displayNameFull), "%" + filter.getQuery().toLowerCase() + "%"), builder.isNotNull(middleName)),
                builder.and(builder.like(builder.lower(displayName), "%" + filter.getQuery().toLowerCase() + "%"), builder.isNull(middleName))));
    }

}
