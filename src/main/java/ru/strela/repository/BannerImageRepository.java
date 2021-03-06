package ru.strela.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.strela.model.BannerImage;

@Repository
public interface BannerImageRepository extends JpaRepository<BannerImage, Integer>, JpaSpecificationExecutor<BannerImage> {
}
