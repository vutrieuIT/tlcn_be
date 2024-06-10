package vn.id.vuductrieu.tlcn_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.id.vuductrieu.tlcn_be.entity.OrderItemEntity;

import java.time.LocalDate;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Integer> {

    @Query(value = "select o from OrderItemEntity o where o.order_id = ?1")
    List<OrderItemEntity> findAllByOrder_id(Integer id);

    @Query(value = "select sum(o.quantity) from OrderItemEntity o where o.created_at >= ?1 and o.created_at <= ?2")
    Long findForSumarySales(LocalDate startDate, LocalDate endDate);

    @Query(value = "select sum(o.quantity*o.price) from OrderItemEntity o where o.created_at >= ?1 and o.created_at <= ?2")
    Long findForSumaryRevenue(LocalDate startDate, LocalDate endDate);
}
