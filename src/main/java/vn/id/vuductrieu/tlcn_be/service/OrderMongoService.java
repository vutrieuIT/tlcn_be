package vn.id.vuductrieu.tlcn_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.entity.OrderCollection;
import vn.id.vuductrieu.tlcn_be.repository.OrderRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderMongoService {

    private final OrderRepo orderRepo;
    public List<OrderCollection> getAllOrder(String userId) {
        return orderRepo.findAllByUserId(userId);
    }

    public List<OrderCollection> getAllOrder() {
        return orderRepo.findAll();
    }

    public void updateOrder(String id, String status) {
        OrderCollection orderCollection = orderRepo.findById(id).orElseThrow(() -> new EmptyResultDataAccessException("Order not found", 1));
        orderCollection.setStatus(status);
        orderRepo.save(orderCollection);
    }

    public OrderCollection getOrderDetail(String id, String userId) {
        if (userId == null) {
            return orderRepo.findById(id).orElse(new OrderCollection());
        } else {
            return orderRepo.findByIdAndUserId(id, userId);
        }
    }
}
