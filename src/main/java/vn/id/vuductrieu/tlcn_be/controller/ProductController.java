package vn.id.vuductrieu.tlcn_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.id.vuductrieu.tlcn_be.dto.ProductDto;
import vn.id.vuductrieu.tlcn_be.entity.ProductEntity;
import vn.id.vuductrieu.tlcn_be.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductController {

    private final ProductService productService;

    @GetMapping()
    public ResponseEntity<?> getAllProduct() {
        try {
            List<ProductEntity> products = productService.getAllProduct();
            return ResponseEntity.ok().body(products);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/popular")
    public ResponseEntity<?> getPopularProduct() {
        try {
            List<ProductEntity> products = productService.popularProduct(10);
            return ResponseEntity.ok().body(products);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
