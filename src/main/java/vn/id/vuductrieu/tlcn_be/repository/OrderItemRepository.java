package vn.id.vuductrieu.tlcn_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.id.vuductrieu.tlcn_be.entity.OrderItemEntity;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Integer> {

    @Query(value = "select o from OrderItemEntity o where o.order_id = ?1")
    List<OrderItemEntity> findAllByOrder_id(Integer id);
}
