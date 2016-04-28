package ru.strela.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.strela.model.Gym;

@Repository
public interface GymRepository extends JpaRepository<Gym, Integer>, JpaSpecificationExecutor<Gym> {
}
