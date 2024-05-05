package vn.id.vuductrieu.tlcn_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.id.vuductrieu.tlcn_be.dto.CategoryDto;
import vn.id.vuductrieu.tlcn_be.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/danh-muc-san-pham")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Object> getAllCategory() {
        try {
            List<CategoryDto> categories = categoryService.getAllCategory();
            return ResponseEntity.ok().body(categories);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
