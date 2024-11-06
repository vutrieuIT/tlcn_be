package vn.id.vuductrieu.tlcn_be.repository.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.ProductCollection;

public interface ProductRepo extends MongoRepository<ProductCollection, String> {
}
