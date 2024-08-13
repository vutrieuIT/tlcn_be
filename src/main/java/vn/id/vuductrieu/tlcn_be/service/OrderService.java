package vn.id.vuductrieu.tlcn_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import vn.id.vuductrieu.tlcn_be.dto.OrderDetailDto;
import vn.id.vuductrieu.tlcn_be.dto.OrderDto;
import vn.id.vuductrieu.tlcn_be.dto.OrderItemDto;
import vn.id.vuductrieu.tlcn_be.dto.ProductDto;
import vn.id.vuductrieu.tlcn_be.entity.OrderEntity;
import vn.id.vuductrieu.tlcn_be.entity.OrderItemEntity;
import vn.id.vuductrieu.tlcn_be.entity.ProductEntity;
import vn.id.vuductrieu.tlcn_be.entity.ProductVariationEntity;
import vn.id.vuductrieu.tlcn_be.repository.OrderItemRepository;
import vn.id.vuductrieu.tlcn_be.repository.OrderRepository;
import vn.id.vuductrieu.tlcn_be.repository.ProductRepository;
import vn.id.vuductrieu.tlcn_be.repository.ProductVariationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final OrderItemRepository orderItemRepository;

    private final ProductVariationRepository productVariationRepository;

    public List<OrderDto> getAllOrder(String userId) {
        List<OrderDto> orderDtos = new ArrayList<>();
        List<OrderEntity> orders = orderRepository.findAllByUser_id(userId);
        for (OrderEntity order : orders) {
            List<OrderItemEntity> items = orderItemRepository.findAllByOrder_id(order.getId());
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
            List<OrderItemEntity> items = orderItemRepository.findAllByOrder_id(order.getId());
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

    public OrderDetailDto getOrderDetail(int id, Integer userId) {
        if (userId != null) {
            OrderEntity order = orderRepository.findByIdAndUser_id(id, userId).orElseThrow(() -> new EmptyResultDataAccessException("Order not found", 1));
            List<OrderItemEntity> items = orderItemRepository.findAllByOrder_id(order.getId());
            List<OrderItemDto> orderItemDtoList = new ArrayList<>();
            for (OrderItemEntity item : items) {
                OrderItemDto orderItemDto = new OrderItemDto();
                BeanUtils.copyProperties(item, orderItemDto);
                Optional<ProductEntity> product = productRepository.findById(item.getProduct_id());
                orderItemDto.setProduct_name(product.map(ProductEntity::getName).orElse(null));
                ProductVariationEntity productVariation = productVariationRepository.findById(item.getVariant_id()).orElse(null);
                orderItemDto.setImage_url(productVariation != null ? productVariation.getImage_url() : null);
                orderItemDtoList.add(orderItemDto);
            }
            OrderDetailDto orderDetailDto = new OrderDetailDto();
            BeanUtils.copyProperties(order, orderDetailDto);
            orderDetailDto.setItems(orderItemDtoList);
            return orderDetailDto;
        } else {
            return getOrderDetail(id);
        }
    }

    public OrderDetailDto getOrderDetail(int id) {
        OrderEntity order = orderRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException("Order not found", 1));
        List<OrderItemEntity> items = orderItemRepository.findAllByOrder_id(order.getId());
        List<OrderItemDto> orderItemDtoList = new ArrayList<>();
        for (OrderItemEntity item : items) {
            OrderItemDto orderItemDto = new OrderItemDto();
            BeanUtils.copyProperties(item, orderItemDto);
            Optional<ProductEntity> product = productRepository.findById(item.getProduct_id());
            orderItemDto.setProduct_name(product.map(ProductEntity::getName).orElse(null));
            ProductVariationEntity productVariation = productVariationRepository.findById(item.getVariant_id()).orElse(null);
            orderItemDto.setImage_url(productVariation != null ? productVariation.getImage_url() : null);
            orderItemDtoList.add(orderItemDto);
        }
        OrderDetailDto orderDto = new OrderDetailDto();
        BeanUtils.copyProperties(order, orderDto);
        orderDto.setItems(orderItemDtoList);
        return orderDto;
    }
}
