package ru.strela.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import ru.strela.model.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer>, JpaSpecificationExecutor<Team> {
}
