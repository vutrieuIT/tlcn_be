package vn.id.vuductrieu.tlcn_be.repository;

import org.hibernate.annotations.processing.HQL;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.id.vuductrieu.tlcn_be.dto.ProductDto;
import vn.id.vuductrieu.tlcn_be.entity.ProductEntity;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

    List<ProductEntity> findAll();

    @Query("SELECT p FROM ProductEntity p")
    List<ProductEntity> find();

    @Query("SELECT p FROM ProductEntity p")
    List<ProductEntity> findPopularProduct(Pageable pageable);

}
