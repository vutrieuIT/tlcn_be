package vn.id.vuductrieu.tlcn_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.dto.CategoryDto;
import vn.id.vuductrieu.tlcn_be.entity.CategoriesProductEntity;
import vn.id.vuductrieu.tlcn_be.repository.CategoryProductRepository;

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
}
