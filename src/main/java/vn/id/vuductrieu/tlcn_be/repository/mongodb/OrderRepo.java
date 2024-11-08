package vn.id.vuductrieu.tlcn_be.repository.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.OrderCollection;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends MongoRepository<OrderCollection, String> {

    @Query("{'user': ?0}")
    List<OrderCollection> findAllByUserId(String userId);

    @Query("{'_id': ?0, 'user': ?1}")
    OrderCollection findByIdAndUserId(String id, String userId);
}
