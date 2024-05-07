package vn.id.vuductrieu.tlcn_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.dto.OrderDto;
import vn.id.vuductrieu.tlcn_be.entity.OrderEntity;
import vn.id.vuductrieu.tlcn_be.entity.OrderItemEntity;
import vn.id.vuductrieu.tlcn_be.repository.OrderItemRepository;
import vn.id.vuductrieu.tlcn_be.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemService;

    public List<OrderDto> getAllOrder(String userId) {
        List<OrderDto> orderDtos = new ArrayList<>();
        List<OrderEntity> orders = orderRepository.findAllByUser_id(userId);
        for (OrderEntity order : orders) {
            List<OrderItemEntity> items = orderItemService.findAllByOrder_id(order.getId());
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(order, orderDto);
            orderDto.setItems(items);
        }
        return orderDtos;
    }
}
