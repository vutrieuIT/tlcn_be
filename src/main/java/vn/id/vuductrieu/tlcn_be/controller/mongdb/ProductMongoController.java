package vn.id.vuductrieu.tlcn_be.controller.mongdb;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vn.id.vuductrieu.tlcn_be.constants.MyConstants;
import vn.id.vuductrieu.tlcn_be.dto.mongodb.ProductMongoDto;
import vn.id.vuductrieu.tlcn_be.dto.mongodb.RatingMongoDto;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.RatingCollection;
import vn.id.vuductrieu.tlcn_be.repository.mongodb.ProductRepo;
import vn.id.vuductrieu.tlcn_be.repository.mongodb.RatingRepo;
import vn.id.vuductrieu.tlcn_be.service.PermissionService;
import vn.id.vuductrieu.tlcn_be.service.MongoService.ProductMongoService;

import java.util.Map;

@RestController
@RequestMapping("/api/mongo")
@RequiredArgsConstructor
public class ProductMongoController {

    private final ProductMongoService productMongoService;

    private final PermissionService permissionService;
    private final ProductRepo productRepo;

    private final RatingRepo ratingRepo;


    @GetMapping("/san-pham")
    public ResponseEntity getProducts() {
        try {
            return ResponseEntity.ok(productRepo.findAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @RequestMapping(value = "/san-pham", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity saveProduct(@RequestBody ProductMongoDto productMongoDto) {
        try {
            if (!permissionService.checkRole(MyConstants.Role.EMPLOYEE.getValue(), MyConstants.Role.ADMIN.getValue())) {
                return ResponseEntity.status(403).body("Bạn không có quyền thao tác");
            }
            String id = productMongoService.saveProduct(productMongoDto);
            return ResponseEntity.ok().body(
                    Map.of("message", "Create product successfully", "id", id, "status", "success")
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping({ "/san-pham-ban-chay", "/san-pham-hot", "/san-pham-moi" })
    public ResponseEntity getPopularProducts() {
        try {
            return ResponseEntity.ok(productMongoService.getProducts(10, 0));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/san-pham/{id}")
    public ResponseEntity getProductById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(productMongoService.getProductById(id));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/san-pham/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        try {
            if (!permissionService.checkRole(MyConstants.Role.EMPLOYEE.getValue(), MyConstants.Role.ADMIN.getValue())) {
                return ResponseEntity.status(403).body("Bạn không có quyền thao tác");
            }
            productMongoService.deleteProduct(id);
            return ResponseEntity.ok().body("Xóa sản phẩm thành công");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/san-pham/comment/{id}")
    public ResponseEntity getCommentByCellphoneId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(productMongoService.getCommentByCellphoneId(id));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/san-pham/comment")
    public ResponseEntity<?> createComment(@RequestBody RatingMongoDto ratingMongoDto) {
        try {
            productMongoService.createComment(ratingMongoDto);
            return ResponseEntity.ok().body("Thêm bình luận thành công");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/san-pham/comment/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable String id) {
        try {
            if (!permissionService.checkRole(MyConstants.Role.EMPLOYEE.getValue(), MyConstants.Role.ADMIN.getValue())) {
                String userId = permissionService.getUserId();
                RatingCollection rating = ratingRepo.findById(id).orElse(null);
                if (rating == null || !rating.getUserId().equals(userId)) {
                    return ResponseEntity.status(403).body("Bạn không có quyền thao tác comment này");
                }
            }
            productMongoService.deleteComment(id);
            return ResponseEntity.ok().body("Xóa bình luận thành công");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
