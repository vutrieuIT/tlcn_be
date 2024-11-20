package vn.id.vuductrieu.tlcn_be.controller.mongdb;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.DiscountCollection;
import vn.id.vuductrieu.tlcn_be.service.MongoService.DiscountMongoService;

import java.util.Map;

@RestController
@RequestMapping("/api/mongo/discount")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountMongoService discountMongoService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            // TODO admin permission
            return ResponseEntity.ok().body(discountMongoService.getAll());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DiscountCollection discount) {
        try {
            // TODO admin permission
            discountMongoService.create(discount);
            return ResponseEntity.ok().body(Map.of("message", "Create success"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody DiscountCollection discount) {
        try {
            // TODO admin permission
            discountMongoService.update(discount);
            return ResponseEntity.ok().body(Map.of("message", "Update success"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
