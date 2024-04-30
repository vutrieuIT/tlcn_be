package vn.id.vuductrieu.tlcn_be.repository;

import org.hibernate.annotations.processing.HQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.id.vuductrieu.tlcn_be.dto.ProductDto;
import vn.id.vuductrieu.tlcn_be.entity.ProductEntity;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findAll();

    @Query("SELECT p FROM ProductEntity p")
    List<ProductEntity> find();

    @Query("SELECT p, sum(pv.quantitySold) as totalSold "
            + "FROM ProductEntity p left join"
            + " ProductVariationEntity pv "
            + "on p.id = pv.product.id "
            + "order by totalSold desc "
            + "limit ?1")
    List<ProductEntity> findPopularProduct(int limit);

}
