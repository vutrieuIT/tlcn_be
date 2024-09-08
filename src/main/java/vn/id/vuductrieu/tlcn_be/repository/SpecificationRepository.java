package vn.id.vuductrieu.tlcn_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.id.vuductrieu.tlcn_be.entity.SpecificationEntity;

@Repository
public interface SpecificationRepository extends JpaRepository<SpecificationEntity, Integer> {
    void deleteByCellphoneId(Integer id);
}
