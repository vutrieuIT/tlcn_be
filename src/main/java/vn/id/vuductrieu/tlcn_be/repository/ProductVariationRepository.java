package vn.id.vuductrieu.tlcn_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.id.vuductrieu.tlcn_be.entity.ProductVariationEntity;

import java.util.List;

@Repository
public interface ProductVariationRepository extends JpaRepository<ProductVariationEntity, Integer> {

    List<ProductVariationEntity> findByProductId(Integer productId);
}
