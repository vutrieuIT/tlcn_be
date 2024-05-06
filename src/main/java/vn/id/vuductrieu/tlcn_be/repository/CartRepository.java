package vn.id.vuductrieu.tlcn_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.id.vuductrieu.tlcn_be.entity.CartEntity;

public interface CartRepository extends JpaRepository<CartEntity, Integer> {

    void deleteByUser_id(Integer userId);

}
