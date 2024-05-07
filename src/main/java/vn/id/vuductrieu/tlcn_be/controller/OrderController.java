package vn.id.vuductrieu.tlcn_be.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.id.vuductrieu.tlcn_be.dto.OrderDto;
import vn.id.vuductrieu.tlcn_be.service.OrderService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderController {

    private final OrderService orderService;
    @PostMapping("/order")
    public ResponseEntity<?> getAllOrder(@RequestBody JsonNode order){
        try {
            List<OrderDto> orderDtos = orderService.getAllOrder(order.get("user_id").asText());
            return ResponseEntity.ok(Map.of("orders", orderDtos));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage()));
        }
    }
}
