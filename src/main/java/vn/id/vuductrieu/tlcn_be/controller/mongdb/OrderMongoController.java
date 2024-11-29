package vn.id.vuductrieu.tlcn_be.controller.mongdb;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.id.vuductrieu.tlcn_be.constants.MyConstants;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.OrderCollection;
import vn.id.vuductrieu.tlcn_be.service.MongoService.OrderMongoService;
import vn.id.vuductrieu.tlcn_be.service.PermissionService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mongo")
@RequiredArgsConstructor
public class OrderMongoController {

    private final OrderMongoService orderMongoService;
    private final PermissionService permissionService;

    @PostMapping("/order")
    public ResponseEntity<?> getUserOrder(@RequestBody JsonNode order){
        try {
            List<OrderCollection> orderCollections = orderMongoService.getAllOrder(order.get("user_id").asText());
            return ResponseEntity.ok(Map.of("orders", orderCollections));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/order")
    public ResponseEntity<?> getAllOrder(){
        try {
            if (!permissionService.checkRole(MyConstants.Role.EMPLOYEE.getValue(), MyConstants.Role.ADMIN.getValue())) {
                return ResponseEntity.status(403).body(Map.of("message", "Bạn không có quyền thao tác"));
            }
            List<OrderCollection> orderCollections = orderMongoService.getAllOrder();
            return ResponseEntity.ok(Map.of("orders", orderCollections));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/order")
    public ResponseEntity<?> updateOrder(@RequestBody JsonNode order){
        try {
            if (!permissionService.checkRole(MyConstants.Role.EMPLOYEE.getValue(), MyConstants.Role.ADMIN.getValue())) {
                return ResponseEntity.status(403).body(Map.of("message", "Bạn không có quyền thao tác"));
            }
            orderMongoService.updateOrder(order.get("id").asText(), order.get("status").asText());
            return ResponseEntity.ok(Map.of("message", "Update order successfully"));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Order not found"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/order-detail/{id}")
    public ResponseEntity<?> getOrderDetail(@PathVariable String id){
        try {
            if (permissionService.checkRole(MyConstants.Role.EMPLOYEE.getValue(), MyConstants.Role.ADMIN.getValue())) {
                OrderCollection orderCollection = orderMongoService.getOrderDetail(id, null);
                return ResponseEntity.ok(Map.of("order", orderCollection));
            } else {
                String userId = permissionService.getUserId().toString();
                if (userId == null) {
                    return ResponseEntity.status(403).body(Map.of("message", "token invalid"));
                }
                OrderCollection orderCollection = orderMongoService.getOrderDetail(id, userId);
                return ResponseEntity.ok(Map.of("order", orderCollection));
            }

        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Order not found"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage()));
        }
    }
}
