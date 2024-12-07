package vn.id.vuductrieu.tlcn_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.constants.MyConstants;
import vn.id.vuductrieu.tlcn_be.dto.ProductMongoDto;
import vn.id.vuductrieu.tlcn_be.dto.RatingMongoDto;
import vn.id.vuductrieu.tlcn_be.entity.ProductCollection;
import vn.id.vuductrieu.tlcn_be.entity.RatingCollection;
import vn.id.vuductrieu.tlcn_be.entity.document.SpecificationDocument;
import vn.id.vuductrieu.tlcn_be.repository.ProductRepo;
import vn.id.vuductrieu.tlcn_be.repository.RatingRepo;

import java.util.ArrayList;
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
            throw new RuntimeException("Không tìm thấy sản phẩm");
        }
        return product;
    }

    public String saveProduct(ProductMongoDto product) {
        String employeeId = permissionService.getUserId();
        ProductCollection productCollection = new ProductCollection();
        BeanUtils.copyProperties(product, productCollection);
        if (product.getSpecifications() == null){
            productCollection.setSpecifications(new ArrayList<>());
        }
        if (product.getVariants() == null){
            productCollection.setVariants(new ArrayList<>());
        }
        setStatusProductByQuantity(productCollection);
        productCollection.setEmployeeId(employeeId);
        ProductCollection savedProduct = productRepo.save(productCollection);
        return savedProduct.getId();
    }

    public void deleteProduct(String id) {
        productRepo.deleteById(id);
    }

    public void createComment(RatingMongoDto ratingMongoDto) {
        RatingCollection rating = new RatingCollection();
        BeanUtils.copyProperties(ratingMongoDto, rating);
        String userId = permissionService.getUserId();
        if (userId == null) {
            throw new EmptyResultDataAccessException("Không tìm thấy người dùng", 1);
        }
        rating.setUserId(userId);
        ratingRepo.save(rating);
    }

    public void deleteComment(String id) {
        ratingRepo.deleteById(id);
    }

    public List<RatingCollection> getCommentByCellphoneId(String id) {
        return ratingRepo.findByProductId(id);
    }

    public List<ProductCollection> getProductsByIds(List<String> recommend) {
        return productRepo.findByIds(recommend);
    }

    public List<ProductCollection> popularProduct(int i) {
        Pageable pageable = PageRequest.of(0, i);
        return productRepo.findAll(pageable).getContent();
    }

    public void setStatusProductByQuantity(ProductCollection productCollection) {
        List<SpecificationDocument> specifications = productCollection.getSpecifications();
        if (specifications != null) {
            int sumQuantity = specifications.stream().mapToInt(SpecificationDocument::sumQuantity).sum();

            if (sumQuantity > 10) {
                productCollection.setStatus(MyConstants.ProductStatus.IN_STOCK.getValue());
            } else if (sumQuantity > 0) {
                productCollection.setStatus(MyConstants.ProductStatus.RUNNING_OUT.getValue());
            } else {
                productCollection.setStatus(MyConstants.ProductStatus.OUT_OF_STOCK.getValue());
            }
        }
    }

    public List<ProductCollection> getChatbotProducts() {
        return productRepo.findChatbotProducts();
    }
}
