package vn.id.vuductrieu.tlcn_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.id.vuductrieu.tlcn_be.entity.OrderEntity;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    @Query(value = "select o from OrderEntity o where o.user_id = ?1")
    List<OrderEntity> findAllByUser_id(String userId);

    @Query(value = "select o from OrderEntity o where o.id = ?1 and o.user_id = ?2")
    Optional<OrderEntity> findByIdAndUser_id(Integer id, Integer userId);
}
