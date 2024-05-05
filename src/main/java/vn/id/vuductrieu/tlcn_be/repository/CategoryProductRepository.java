package vn.id.vuductrieu.tlcn_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.id.vuductrieu.tlcn_be.entity.CategoriesProductEntity;

public interface CategoryProductRepository extends JpaRepository<CategoriesProductEntity, Integer> {
}
