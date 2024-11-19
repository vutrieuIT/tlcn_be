package vn.id.vuductrieu.tlcn_be.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.id.vuductrieu.tlcn_be.dto.OrderDetailDto;
import vn.id.vuductrieu.tlcn_be.dto.OrderDto;
import vn.id.vuductrieu.tlcn_be.service.OrderService;
import vn.id.vuductrieu.tlcn_be.service.PermissionService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderController {

    private final OrderService orderService;

    private final PermissionService permissionService;
    @PostMapping("/order")
    public ResponseEntity<?> getAllOrder(@RequestBody JsonNode order){
        try {
            List<OrderDto> orderDtos = orderService.getAllOrder(order.get("user_id").asText());
            return ResponseEntity.ok(Map.of("orders", orderDtos));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/order")
    public ResponseEntity<?> getAllOrder(){
        try {
            if (!permissionService.isAdmin()) {
                return ResponseEntity.status(403).body(Map.of("message", "Permission denied"));
            }
            List<OrderDto> orderDtos = orderService.getAllOrder();
            return ResponseEntity.ok(Map.of("orders", orderDtos));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/order")
    public ResponseEntity<?> updateOrder(@RequestBody JsonNode order){
        try {
            if (!permissionService.isAdmin()) {
                return ResponseEntity.status(403).body(Map.of("message", "Permission denied"));
            }
            orderService.updateOrder(order.get("id").asInt(), order.get("status").asText());
            return ResponseEntity.ok(Map.of("message", "Update order successfully"));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Order not found"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/order-detail/{id}")
    public ResponseEntity<?> getOrderDetail(@PathVariable Integer id){
        try {
            if (permissionService.isAdmin()) {
                OrderDetailDto orderDetailDto = orderService.getOrderDetail(id, null);
                return ResponseEntity.ok(Map.of("order", orderDetailDto));
            } else {
                String userId = permissionService.getUserId();
                if (userId == null) {
                    return ResponseEntity.status(403).body(Map.of("message", "token invalid"));
                }
                OrderDetailDto orderDetailDto = orderService.getOrderDetail(id, Integer.parseInt(userId));
                if (!orderDetailDto.getUser_id().equals(userId)) {
                    return ResponseEntity.status(403).body(Map.of("message", "Permission denied"));
                }
                return ResponseEntity.ok(Map.of("order", orderDetailDto));
            }

        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Order not found"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage()));
        }
    }
}
