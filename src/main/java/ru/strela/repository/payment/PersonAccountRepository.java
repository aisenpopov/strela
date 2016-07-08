package ru.strela.repository.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.strela.model.auth.Person;
import ru.strela.model.payment.PersonAccount;

/**
 * Created by Aisen on 08.07.2016.
 */
@Repository
public interface PersonAccountRepository extends JpaRepository<PersonAccount, Integer>, JpaSpecificationExecutor<PersonAccount> {

    PersonAccount findByPerson(Person person);

}
