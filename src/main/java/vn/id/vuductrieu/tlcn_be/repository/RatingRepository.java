package vn.id.vuductrieu.tlcn_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.id.vuductrieu.tlcn_be.composite_key.RatingKey;
import vn.id.vuductrieu.tlcn_be.entity.RatingEntity;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, RatingKey> {

    List<RatingEntity> findByCellphoneId(Integer id);
}
