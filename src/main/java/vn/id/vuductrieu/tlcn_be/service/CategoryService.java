package vn.id.vuductrieu.tlcn_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.dto.CategoryDto;
import vn.id.vuductrieu.tlcn_be.entity.CategoriesProductEntity;
import vn.id.vuductrieu.tlcn_be.repository.CategoryProductRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryService {

    private final CategoryProductRepository categoryRepository;

    public List<CategoryDto> getAllCategory() {
        List<CategoriesProductEntity> categories = categoryRepository.findAll();
        List<CategoryDto> categoryDtos = categories.stream().map(category -> {
            CategoryDto categoryDto = new CategoryDto();
            BeanUtils.copyProperties(category, categoryDto, "products");
            return categoryDto;
        }).toList();
        return categoryDtos;
    }

    public void createCategory(CategoryDto categoryDto) {
        CategoriesProductEntity category = new CategoriesProductEntity();
        BeanUtils.copyProperties(categoryDto, category);
        category.setCreated_at(LocalDateTime.now());
        category.setPosition(categoryRepository.getPositionMax() + 1);
        category.setShow_hide(true);
        category.setTitle(categoryDto.getName());
        categoryRepository.save(category);
    }

    public void updateCategory(CategoryDto categoryDto) {
        CategoriesProductEntity category = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new EmptyResultDataAccessException("Category not found", 1));
        BeanUtils.copyProperties(categoryDto, category);
        category.setUpdated_at(LocalDateTime.now());
        categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        CategoriesProductEntity category = categoryRepository.findById(Integer.valueOf(id.toString()))
                .orElseThrow(() -> new EmptyResultDataAccessException("Category not found", 1));
        categoryRepository.delete(category);
    }
}
