package vn.id.vuductrieu.tlcn_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.id.vuductrieu.tlcn_be.entity.CategoriesProductEntity;

public interface CategoryProductRepository extends JpaRepository<CategoriesProductEntity, Integer> {

    @Query("SELECT MAX(c.position) FROM CategoriesProductEntity c")
    Integer getPositionMax();
}
