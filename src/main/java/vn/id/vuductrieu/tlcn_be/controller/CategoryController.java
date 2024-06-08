package vn.id.vuductrieu.tlcn_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
import vn.id.vuductrieu.tlcn_be.dto.CategoryDto;
import vn.id.vuductrieu.tlcn_be.service.CategoryService;
import vn.id.vuductrieu.tlcn_be.service.PermissionService;

import java.util.List;

@RestController
@RequestMapping("/api/danh-muc-san-pham")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryController {

    private final CategoryService categoryService;

    private final PermissionService permissionService;

    @GetMapping
    public ResponseEntity<Object> getAllCategory() {
        try {
            List<CategoryDto> categories = categoryService.getAllCategory();
            return ResponseEntity.ok().body(categories);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Object> createCategory(@RequestBody CategoryDto categoryDto) {
        if (!permissionService.isAdmin()) {
            return ResponseEntity.status(403).body("Permission denied");
        }
        try {
            categoryService.createCategory(categoryDto);
            return ResponseEntity.ok().body("Create category successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<Object> updateCategory(@RequestBody CategoryDto categoryDto) {
        if (!permissionService.isAdmin()) {
            return ResponseEntity.status(403).body("Permission denied");
        }
        try {
            categoryService.updateCategory(categoryDto);
            return ResponseEntity.ok().body("Update category successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long id) {
        if (!permissionService.isAdmin()) {
            return ResponseEntity.status(403).body("Permission denied");
        }
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok().body("Delete category successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
