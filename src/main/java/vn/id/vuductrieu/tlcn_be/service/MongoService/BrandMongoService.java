package vn.id.vuductrieu.tlcn_be.service.MongoService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.ProductCollection;
import vn.id.vuductrieu.tlcn_be.repository.mongodb.ProductRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandMongoService {

    private final ProductRepo productRepo;
    public List<String> getBrand() {
        List<ProductCollection> productCollections = productRepo.findBrandOnly();
        List<String> brands = productCollections.stream().map(ProductCollection::getBrandName).distinct().toList();
        return brands;
    }
}
