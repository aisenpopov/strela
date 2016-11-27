package ru.strela.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.strela.model.City;
import ru.strela.model.auth.Person;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Integer>, JpaSpecificationExecutor<City> {

    @Query(nativeQuery=true, value= "SELECT DISTINCT c.*\n" +
                                    "FROM city c\n" +
                                    "INNER JOIN gym g ON c.id = g.city_id\n" +
                                    "INNER JOIN article a ON g.article_id = a.id\n" +
                                    "WHERE a.visible = TRUE\n" +
                                    "ORDER BY c.id")
    List<City> findHasGym();

}
