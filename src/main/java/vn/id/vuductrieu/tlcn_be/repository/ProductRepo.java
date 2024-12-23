package vn.id.vuductrieu.tlcn_be.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import vn.id.vuductrieu.tlcn_be.entity.ProductCollection;

import java.util.List;

public interface ProductRepo extends MongoRepository<ProductCollection, String> {

    @Query(value = "{}", fields = "{'brandName': 1}")
    List<ProductCollection> findBrandOnly();

    @Query(value = "{_id: {$in: ?0}}")
    List<ProductCollection> findByIds(List<String> recommend);

    @Query(value = "{}", fields = "{'variants': 0}")
    List<ProductCollection> findChatbotProducts();
}
