package ru.strela.repository.spec;

import org.apache.commons.lang.StringUtils;
import ru.strela.model.filter.BaseFilter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.List;

public class Spec {

    public static void fillAthleteDisplayNamePredicates(CriteriaBuilder builder, List<Predicate> predicates, BaseFilter filter, Path athlete) {
        Expression<String> firstName = athlete.get("firstName").as(String.class);
        Expression<String> lastName = athlete.get("lastName").as(String.class);
        Expression<String> middleName = athlete.get("middleName").as(String.class);

        Expression<String> displayNameFull = builder.concat(lastName, " ");
        displayNameFull = builder.concat(displayNameFull, builder.<String>coalesce(firstName, builder.literal(StringUtils.EMPTY)));
        displayNameFull = builder.concat(displayNameFull, " ");
        displayNameFull = builder.concat(displayNameFull, builder.<String>coalesce(middleName, builder.literal(StringUtils.EMPTY)));

        predicates.add(builder.like(builder.lower(displayNameFull), "%" + filter.getQuery().toLowerCase() + "%"));
    }

}
