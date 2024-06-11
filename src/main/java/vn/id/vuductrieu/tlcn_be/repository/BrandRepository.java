package vn.id.vuductrieu.tlcn_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.id.vuductrieu.tlcn_be.entity.BrandEntity;

public interface BrandRepository extends JpaRepository<BrandEntity, Integer> {

    @Query(value = "select max(position) from brands", nativeQuery = true)
    Integer getMaxPosition();
}
