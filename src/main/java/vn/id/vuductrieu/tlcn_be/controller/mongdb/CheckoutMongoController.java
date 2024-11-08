package vn.id.vuductrieu.tlcn_be.controller.mongdb;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mongo")
@RequiredArgsConstructor
public class CheckoutMongoController {

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout() {
        return ResponseEntity.ok("Checkout success");
    }
}
