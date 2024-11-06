package vn.id.vuductrieu.tlcn_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.dto.mongodb.ProductMongoDto;
import vn.id.vuductrieu.tlcn_be.dto.mongodb.RatingMongoDto;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.ProductCollection;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.RatingCollection;
import vn.id.vuductrieu.tlcn_be.repository.mongodb.ProductRepo;
import vn.id.vuductrieu.tlcn_be.repository.mongodb.RatingRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductMongoService {

    private final ProductRepo productRepo;

    private final RatingRepo ratingRepo;

    private final PermissionService permissionService;

    public List<ProductCollection> getProducts(Integer limit, Integer page) {
        if (limit == null || limit <= 0 || page == null) {
            return productRepo.findAll();
        } else {
            Pageable pageable = PageRequest.of(page, limit);
            return productRepo.findAll(pageable).getContent();
        }
    }

    public ProductCollection getProductById(String id) {
        ProductCollection product = productRepo.findById(id).orElse(null);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }
        return product;
    }

    public String saveProduct(ProductMongoDto product) {
        ProductCollection productCollection = new ProductCollection();
        BeanUtils.copyProperties(product, productCollection);
        ProductCollection savedProduct = productRepo.save(productCollection);
        return savedProduct.getId();
    }

    public void deleteProduct(String id) {
        productRepo.deleteById(id);
    }

    public void createComment(RatingMongoDto ratingMongoDto) {
        RatingCollection rating = new RatingCollection();
        BeanUtils.copyProperties(ratingMongoDto, rating);
        String userId = permissionService.getUserId().toString();
        if (userId == null) {
            throw new EmptyResultDataAccessException("User not found", 1);
        }
        rating.setUserId(userId);
        ratingRepo.save(rating);
    }

    public void deleteComment(String id) {
        ratingRepo.deleteById(id);
    }

    public List<RatingCollection> getCommentByCellphoneId(Integer id) {
        return ratingRepo.findByProductId(id);
    }
}
