package ru.strela.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import ru.strela.model.Settings;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Integer>, JpaSpecificationExecutor<Settings> {
}
