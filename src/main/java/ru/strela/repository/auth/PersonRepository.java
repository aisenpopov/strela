package ru.strela.repository.auth;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.strela.model.auth.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer>, JpaSpecificationExecutor<Person> {
	
	Person findByLogin(String login);
	
	@Query(nativeQuery=true, value= "SELECT p.*\n" +
									"FROM person p\n" +
									"INNER JOIN athlete a ON p.id = a.person_id\n" +
									"WHERE p.login = :email OR a.email = :email")
	Person findByEmail(String email);
	
	@Query(nativeQuery=true, value= "SELECT p.*\n" +
									"FROM person p\n" +
									"LEFT OUTER JOIN athlete a ON p.id = a.person_id\n" +
									"WHERE a.id IS NULL AND p.login ILIKE '%'||(:query)||'%'")
	List<Person> findHasNotAthlete(@Param("query") String query);
	
}
