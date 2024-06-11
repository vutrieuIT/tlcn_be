package vn.id.vuductrieu.tlcn_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.id.vuductrieu.tlcn_be.dto.BrandDto;
import vn.id.vuductrieu.tlcn_be.service.BrandService;
import vn.id.vuductrieu.tlcn_be.service.PermissionService;

@RestController
@RequestMapping("/api/thuong-hieu")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    private final PermissionService permissionService;

    @GetMapping
    public ResponseEntity<Object> getAllBrand() {
        try {
            return ResponseEntity.ok().body(brandService.getAllBrand());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Object> createBrand(@RequestBody BrandDto brandDto) {
        try {
            if (!permissionService.isAdmin()) {
                return ResponseEntity.status(403).body("Bạn không có quyền thực hiện chức năng này");
            }
            brandService.createBrand(brandDto);
            return ResponseEntity.ok().body("Thêm thương hiệu thành công");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateBrand(@PathVariable Integer id,
            @RequestBody BrandDto brandDto) {
        try {
            if (!permissionService.isAdmin()) {
                return ResponseEntity.status(403).body("Bạn không có quyền thực hiện chức năng này");
            }
            brandService.updateBrand(id, brandDto);
            return ResponseEntity.ok().body("Cập nhật thương hiệu thành công");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBrand(@PathVariable Integer id) {
        try {
            if (!permissionService.isAdmin()) {
                return ResponseEntity.status(403).body("Bạn không có quyền thực hiện chức năng này");
            }
            brandService.deleteBrand(id);
            return ResponseEntity.ok().body("Xóa thương hiệu thành công");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
