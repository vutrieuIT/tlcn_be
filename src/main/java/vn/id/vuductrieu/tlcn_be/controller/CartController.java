package vn.id.vuductrieu.tlcn_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
            return ResponseEntity.internalServerError().body("An error occurred");
        }
    }

    @PostMapping()
    public ResponseEntity<?> getCartsByUserId(@RequestBody CartEntity cartEntity){
        try {
            List<CartDto> carts = cartService.getCartsByUserId(cartEntity.getUser_id());
            return ResponseEntity.ok(carts);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred");
        }
    }

    @DeleteMapping("/carts/{id}")
    public ResponseEntity<?> deleteCart(@RequestBody CartEntity cartEntity){
        try {
            cartService.deleteCart(cartEntity.getId());
            return ResponseEntity.ok("Deleted cart successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred");
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
            return ResponseEntity.internalServerError().body("An error occurred");
        }
    }
}
