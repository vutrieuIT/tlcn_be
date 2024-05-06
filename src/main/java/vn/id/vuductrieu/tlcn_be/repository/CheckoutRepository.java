package vn.id.vuductrieu.tlcn_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.id.vuductrieu.tlcn_be.entity.CartEntity;

import java.util.List;
import java.util.Optional;

public interface CheckoutRepository extends JpaRepository<CartEntity, Integer> {

    List<CartEntity> findByUser_id(Integer user_id);
}
