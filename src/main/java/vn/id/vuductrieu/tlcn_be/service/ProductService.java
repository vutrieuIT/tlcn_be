package vn.id.vuductrieu.tlcn_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.dto.CategoryDto;
import vn.id.vuductrieu.tlcn_be.dto.ProductDto;
import vn.id.vuductrieu.tlcn_be.dto.ProductVariationDto;
import vn.id.vuductrieu.tlcn_be.entity.ProductEntity;
import vn.id.vuductrieu.tlcn_be.entity.ProductVariationEntity;
import vn.id.vuductrieu.tlcn_be.repository.ProductRepository;
import vn.id.vuductrieu.tlcn_be.repository.ProductVariationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductVariationRepository productVariationRepository;

    public List<ProductEntity> getAllProduct() {
        List<ProductEntity> products = productRepository.find();
        for (ProductEntity product : products) {
            List<ProductVariationEntity> variations = productVariationRepository.findByProductId(product.getId());
            product.setVariations(variations);
        }

        return products;
    }

    public List<ProductEntity> popularProduct(int limit) {
        return productRepository.findPopularProduct(limit);

    }

    public ProductEntity getProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }
}
