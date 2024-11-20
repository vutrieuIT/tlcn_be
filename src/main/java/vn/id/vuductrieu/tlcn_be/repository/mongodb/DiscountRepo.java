package vn.id.vuductrieu.tlcn_be.repository.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.DiscountCollection;

import java.util.List;

public interface DiscountRepo extends MongoRepository<DiscountCollection, String> {

    @Query("{'code': ?0, 'status': ?1}")
    DiscountCollection findByCodeAndStatus(String code, String value);

    @Query(value = "{'code': ?0}", sort = "{'createdAt': -1}")
    List<DiscountCollection> findAllSortByCreatedAt();
}
