package vn.id.vuductrieu.tlcn_be.repository.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.RatingCollection;

import java.util.List;

public interface RatingRepo extends MongoRepository<RatingCollection, String> {
    List<RatingCollection> findByProductId(String id);
}
