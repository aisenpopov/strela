package ru.strela.repository.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.strela.model.payment.AthleteTariff;

@Repository
public interface AthleteTariffRepository extends JpaRepository<AthleteTariff, Integer>, JpaSpecificationExecutor<AthleteTariff> {
}
