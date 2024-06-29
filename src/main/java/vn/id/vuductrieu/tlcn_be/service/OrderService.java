package vn.id.vuductrieu.tlcn_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
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
            orderDtos.add(orderDto);
        }
        return orderDtos;
    }

    public List<OrderDto> getAllOrder() {
        List<OrderDto> orderDtos = new ArrayList<>();
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        List<OrderEntity> orders = orderRepository.findAll(sort);
        for (OrderEntity order : orders) {
            List<OrderItemEntity> items = orderItemService.findAllByOrder_id(order.getId());
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(order, orderDto);
            orderDto.setTotal(items.stream().mapToInt((i) -> i.getPrice() * i.getQuantity()).sum());
            orderDtos.add(orderDto);
        }
        return orderDtos;
    }

    public void updateOrder(int id, String status) {
        OrderEntity order = orderRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException("Order not found", 1));
        order.setStatus(status);
        orderRepository.save(order);
    }

    public OrderDto getOrderDetail(int id, Integer userId) {
        if (userId != null) {
            OrderEntity order = orderRepository.findByIdAndUser_id(id, userId).orElseThrow(() -> new EmptyResultDataAccessException("Order not found", 1));
            List<OrderItemEntity> items = orderItemService.findAllByOrder_id(order.getId());
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(order, orderDto);
            orderDto.setItems(items);
            return orderDto;
        } else {
            return getOrderDetail(id);
        }
    }

    public OrderDto getOrderDetail(int id) {
        OrderEntity order = orderRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException("Order not found", 1));
        List<OrderItemEntity> items = orderItemService.findAllByOrder_id(order.getId());
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(order, orderDto);
        orderDto.setItems(items);
        return orderDto;
    }
}
