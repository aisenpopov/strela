package ru.strela.model.filter.payment;

import ru.strela.model.auth.Person;
import ru.strela.model.filter.BaseFilter;

/**
 * Created by Aisen on 08.07.2016.
 */
public class TransactionFilter extends BaseFilter {

    private Person person;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
