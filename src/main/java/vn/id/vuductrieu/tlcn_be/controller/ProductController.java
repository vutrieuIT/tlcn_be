package vn.id.vuductrieu.tlcn_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.id.vuductrieu.tlcn_be.dto.ProductDto;
import vn.id.vuductrieu.tlcn_be.dto.SpecificationDto;
import vn.id.vuductrieu.tlcn_be.entity.ProductEntity;
import vn.id.vuductrieu.tlcn_be.entity.ProductVariationEntity;
import vn.id.vuductrieu.tlcn_be.service.CloudaryService;
import vn.id.vuductrieu.tlcn_be.service.PermissionService;
import vn.id.vuductrieu.tlcn_be.service.ProductService;
import vn.id.vuductrieu.tlcn_be.service.SpecificationService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductController {

    private final ProductService productService;

    private final SpecificationService specificationService;

    private final PermissionService permissionService;

    private final CloudaryService cloudaryService;

    @GetMapping("/san-pham")
    public ResponseEntity<?> getAllProduct() {
        try {
            List<ProductEntity> products = productService.getAllProduct();
            return ResponseEntity.ok().body(products);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/san-pham/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Integer id) {
        try {
            ProductEntity product = productService.getProductById(id);
            return ResponseEntity.ok().body(product);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping({ "/san-pham-ban-chay", "/san-pham-hot", "/san-pham-moi" })
    public ResponseEntity<?> getPopularProduct() {
        try {
            List<ProductEntity> products = productService.popularProduct(10);
            return ResponseEntity.ok().body(products);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/san-pham")
    public ResponseEntity<?> createProduct(@RequestBody(required = false) ProductDto productDto) {
        try {
            if (!permissionService.isAdmin()) {
                return ResponseEntity.status(403).body("Permission denied");
            }
            Integer id = productService.createProduct(productDto);
            return ResponseEntity.ok().body(
                    Map.of("message", "Create product successfully", "id", id, "status", "success")
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/san-pham/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody ProductDto productDto) {
        try {
            if (!permissionService.isAdmin()) {
                return ResponseEntity.status(403).body("Permission denied");
            }
            productService.updateProduct(id, productDto);
            return ResponseEntity.ok().body("Update product successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/san-pham/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        try {
            if (!permissionService.isAdmin()) {
                return ResponseEntity.status(403).body("Permission denied");
            }
            productService.deleteProduct(id);
            return ResponseEntity.ok().body("Delete product successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // variational product
    @PostMapping("/san-pham/variant")
    public ResponseEntity<?> createVariationProduct(@RequestBody(required = false) ProductVariationEntity productVariationEntity,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            if (!permissionService.isAdmin()) {
                return ResponseEntity.status(403).body("Permission denied");
            }
            if (file != null) {
                productVariationEntity.setImage_url(cloudaryService.uploadImage(file, "variant"));
            }
            productService.createVariationProduct(productVariationEntity);
            return ResponseEntity.ok().body("Create product successfully");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/san-pham/variant/{id}")
    public ResponseEntity<?> updateVariationProduct(@PathVariable Integer id, @RequestBody ProductVariationEntity productVariationEntity,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            if (!permissionService.isAdmin()) {
                return ResponseEntity.status(403).body("Permission denied");
            }
            if (file != null) {
                productVariationEntity.setImage_url(cloudaryService.uploadImage(file, "variant"));
            }
            productService.updateVariationProduct(id, productVariationEntity);
            return ResponseEntity.ok().body("Update product successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/san-pham/variant/{id}")
    public ResponseEntity<?> deleteVariationProduct(@PathVariable Integer id) {
        try {
            if (!permissionService.isAdmin()) {
                return ResponseEntity.status(403).body("Permission denied");
            }
            productService.deleteVariationProduct(id);
            return ResponseEntity.ok().body("Delete product successfully");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/san-pham/specification/{id}")
    public ResponseEntity<?> getSpecificationByCellphoneId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok().body(specificationService.getSpecificationByProductId(id));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/san-pham/specification/{id}")
    public ResponseEntity<?> upsertSpecification(@PathVariable Integer id, @RequestBody SpecificationDto specificationDto) {
        try {
            if (!permissionService.isAdmin()) {
                return ResponseEntity.status(403).body("Permission denied");
            }
            specificationDto.setCellphoneId(id);
            specificationService.upsertSpecification(specificationDto);
            return ResponseEntity.ok().body("Update specification successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/san-pham/specification/{id}")
    public ResponseEntity<?> deleteSpecificationByCellphoneId(@PathVariable Integer id) {
        try {
            if (!permissionService.isAdmin()) {
                return ResponseEntity.status(403).body("Permission denied");
            }
            specificationService.deleteByProductId(id);
            return ResponseEntity.ok().body("Delete specification successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
