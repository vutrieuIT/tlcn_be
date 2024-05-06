package vn.id.vuductrieu.tlcn_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.id.vuductrieu.tlcn_be.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
}
