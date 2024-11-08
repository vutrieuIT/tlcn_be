package vn.id.vuductrieu.tlcn_be.controller.mongdb;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.document.ItemDocument;
import vn.id.vuductrieu.tlcn_be.service.MongoService.CartMongoService;

import java.util.List;

@RestController
@RequestMapping("/api/mongo")
@RequiredArgsConstructor
public class CartMongoController {

    private final CartMongoService cartMongoService;


    @GetMapping("/cart")
    public ResponseEntity<?> getCart() {
        try {
            return ResponseEntity.ok(cartMongoService.getCart());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/add-to-cart")
    public ResponseEntity<?> addToCart(@RequestBody ItemDocument itemDocument) {
        try {
            cartMongoService.addToCart(itemDocument);
            return ResponseEntity.ok("Added to cart successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/cart")
    public ResponseEntity<?> updateCart(@RequestBody List<ItemDocument> itemDocument) {
        try {
            cartMongoService.deleteCart(itemDocument);
            return ResponseEntity.ok("Deleted cart successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
