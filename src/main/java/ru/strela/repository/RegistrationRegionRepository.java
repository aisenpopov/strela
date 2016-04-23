package ru.strela.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import ru.strela.model.RegistrationRegion;

@Repository
public interface RegistrationRegionRepository extends JpaRepository<RegistrationRegion, Integer>, JpaSpecificationExecutor<RegistrationRegion> {
}
