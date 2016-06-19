package ru.strela.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.strela.model.Gym;
import ru.strela.model.GymImage;

import java.util.List;

@Repository
public interface GymImageRepository extends JpaRepository<GymImage, Integer> {
	
	List<GymImage> findByGym(Gym gym);

}
