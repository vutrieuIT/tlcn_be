package vn.id.vuductrieu.tlcn_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.dto.ProductDto;
import vn.id.vuductrieu.tlcn_be.dto.RatingDto;
import vn.id.vuductrieu.tlcn_be.entity.CategoriesProductEntity;
import vn.id.vuductrieu.tlcn_be.entity.ProductEntity;
import vn.id.vuductrieu.tlcn_be.entity.ProductVariationEntity;
import vn.id.vuductrieu.tlcn_be.entity.RatingEntity;
import vn.id.vuductrieu.tlcn_be.repository.CategoryProductRepository;
import vn.id.vuductrieu.tlcn_be.repository.ProductRepository;
import vn.id.vuductrieu.tlcn_be.repository.ProductVariationRepository;
import vn.id.vuductrieu.tlcn_be.repository.RatingRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductService {

    private final ProductRepository productRepository;

    private final CategoryProductRepository categoryProductRepository;

    private final ProductVariationRepository productVariationRepository;

    private final PermissionService permissionService;
    
    private final RatingRepository ratingRepository;

    public List<ProductEntity> getAllProduct() {
        List<ProductEntity> products = productRepository.find();
        for (ProductEntity product : products) {
            List<ProductVariationEntity> variations = productVariationRepository.findByProductId(product.getId());
            product.setVariations(variations);
        }

        return products;
    }

    public List<ProductEntity> popularProduct(int limit) {
        Pageable pageable = Pageable.ofSize(limit);
        return productRepository.findPopularProduct(pageable);
    }

    public ProductEntity getProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    public Integer createProduct(ProductDto productDto) {
        ProductEntity product = new ProductEntity();
        BeanUtils.copyProperties(productDto, product);
        CategoriesProductEntity category = categoryProductRepository.findById(productDto.getCategory_id())
                .orElseThrow(() -> new EmptyResultDataAccessException("Category not found", 1));
        product.setCategory(category);
        product.setSeo_keywords(productDto.getName().toLowerCase());
        product.setShow_hide(productDto.getShow_hide());
        product.setSlug(productDto.getName().toLowerCase());
        product.setStatus("none");
        product.setCreated_at(LocalDateTime.now());
        ProductEntity productEntity = productRepository.save(product);
        return productEntity.getId();
    }

    public Integer updateProduct(Integer id, ProductDto productDto) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException("Product not found", 1));
        BeanUtils.copyProperties(productDto, product);
        CategoriesProductEntity category = categoryProductRepository.findById(productDto.getCategory_id())
                .orElseThrow(() -> new EmptyResultDataAccessException("Category not found", 1));
        product.setCategory(category);
        product.setUpdated_at(LocalDateTime.now());
        ProductEntity productEntity = productRepository.save(product);
        return productEntity.getId();
    }

    public void deleteProduct(Integer id) {
        List<ProductVariationEntity> variations = productVariationRepository.findByProductId(id);
        for (ProductVariationEntity variation : variations) {
            productVariationRepository.deleteById(variation.getId());
        }
        productRepository.deleteById(id);
    }

    public void createVariationProduct(ProductVariationEntity productVariationEntity) {
        ProductEntity product = productRepository.findById(productVariationEntity.getProduct_id())
                .orElseThrow(() -> new EmptyResultDataAccessException("Product not found", 1));
        productVariationEntity.setProduct(product);
        productVariationEntity.setShow_hide(1);
        productVariationEntity.setPosition(1);
        productVariationEntity.setQuantity_available(productVariationEntity.getQuantity());
        productVariationEntity.setQuantity_sold(0);
        productVariationRepository.save(productVariationEntity);
    }

    public void updateVariationProduct(Integer id, ProductVariationEntity productVariationEntity) {
        ProductVariationEntity variation = productVariationRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException("Variation not found", 1));
        BeanUtils.copyProperties(productVariationEntity, variation, "product_id", "product");
        productVariationRepository.save(variation);
    }

    public void deleteVariationProduct(Integer id) {
        ProductVariationEntity variation = productVariationRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException("Variation not found", 1));
        productVariationRepository.delete(variation);
    }

    public List<ProductEntity> getProductsByIds(List<Integer> recommend) {
        return productRepository.findAllById(recommend);
    }
    
    public void createComment(RatingDto ratingDto) {
        RatingEntity rating = new RatingEntity();
        BeanUtils.copyProperties(ratingDto, rating);
        Integer userId = Integer.parseInt(permissionService.getUserId());
        if (userId == null) {
            throw new EmptyResultDataAccessException("User not found", 1);
        }
        rating.setUserId(userId);
        ratingRepository.save(rating);
    }

    public List<RatingEntity> getCommentByCellphoneId(Integer id) {
        return ratingRepository.findByCellphoneId(id);
    }

    public void deleteComment(Integer userId, Integer cellphoneId) {
        RatingEntity rating = new RatingEntity();
        rating.setUserId(userId);
        rating.setCellphoneId(cellphoneId);
        ratingRepository.delete(rating);
    }
}
