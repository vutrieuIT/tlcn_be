package vn.id.vuductrieu.tlcn_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.entity.ProductCollection;
import vn.id.vuductrieu.tlcn_be.repository.ProductRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandMongoService {

    private final ProductRepo productRepo;

    public List<String> getBrand() {
        List<ProductCollection> productCollections = productRepo.findBrandOnly();
        List<String> brands =
            productCollections.stream().map(ProductCollection::getBrandName).map(String::toUpperCase).map(String::trim).distinct().toList();
        return brands;
    }
}
