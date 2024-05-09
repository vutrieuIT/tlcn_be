package vn.id.vuductrieu.tlcn_be.controller;

import ch.qos.logback.core.model.INamedModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.id.vuductrieu.tlcn_be.dto.CartDto;
import vn.id.vuductrieu.tlcn_be.entity.CartEntity;
import vn.id.vuductrieu.tlcn_be.service.CartService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CartController {

    private final CartService cartService;

    @PostMapping("/add-to-cart")
    public ResponseEntity<?> addToCart(@RequestBody CartEntity cartEntity) {
        try {
            cartService.addToCart(cartEntity);
            return ResponseEntity.ok("Added to cart successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/carts")
    public ResponseEntity<?> getCartsByUserId(@RequestBody CartEntity cartEntity){
        try {
            List<CartDto> carts = cartService.getCartsByUserId(cartEntity.getUser_id());
            return ResponseEntity.ok(carts);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/carts/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable("id") Integer id){
        try {
            cartService.deleteCart(id);
            return ResponseEntity.ok("Deleted cart successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/carts/{id}")
    public ResponseEntity<?> updateCart(@RequestBody CartEntity cartEntity){
        try {
            cartService.updateCart(cartEntity);
            return ResponseEntity.ok("Updated cart successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
