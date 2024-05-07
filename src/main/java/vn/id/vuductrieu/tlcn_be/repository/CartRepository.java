package vn.id.vuductrieu.tlcn_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.id.vuductrieu.tlcn_be.entity.CartEntity;

public interface CartRepository extends JpaRepository<CartEntity, Integer> {

    @Query(value = "delete from CartEntity c where c.user_id = ?1")
    void deleteByUser_id(Integer userId);

}
