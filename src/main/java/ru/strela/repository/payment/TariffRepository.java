package ru.strela.repository.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.strela.model.payment.Tariff;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Integer>, JpaSpecificationExecutor<Tariff> {
}
