package vn.id.vuductrieu.tlcn_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.id.vuductrieu.tlcn_be.entity.CartEntity;

import java.util.List;
import java.util.Optional;

public interface CheckoutRepository extends JpaRepository<CartEntity, Integer> {

    @Query(value = "SELECT c FROM CartEntity c WHERE c.user_id = ?1")
    List<CartEntity> selectByUserId(Integer user_id);
}
