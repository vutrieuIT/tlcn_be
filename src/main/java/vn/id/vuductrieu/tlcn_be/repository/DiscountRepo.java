package vn.id.vuductrieu.tlcn_be.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import vn.id.vuductrieu.tlcn_be.entity.DiscountCollection;

public interface DiscountRepo extends MongoRepository<DiscountCollection, String> {

    @Query("{'code': ?0, 'status': ?1}")
    DiscountCollection findByCodeAndStatus(String code, String value);

    @Query("{'code': ?0}")
    DiscountCollection findByCode(String code);
}
