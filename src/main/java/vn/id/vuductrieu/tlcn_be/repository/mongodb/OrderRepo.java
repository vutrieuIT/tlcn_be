package vn.id.vuductrieu.tlcn_be.repository.mongodb;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.OrderCollection;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepo extends MongoRepository<OrderCollection, String> {

    @Query("{'userId': ?0}")
    List<OrderCollection> findAllByUserId(String userId);

    @Query("{'_id': ?0, 'userId': ?1}")
    OrderCollection findByIdAndUserId(String id, String userId);

    @Aggregation(pipeline = {
        "{ $match: { 'createdAt': { $gte: ?0, $lte: ?1 } } }",
        "{ $unwind: '$items' }",
        "{ $group: { _id: null, totalSales: { $sum: '$items.quantity' } } }",
        "{ $project: { _id: 0, totalSales: 1 } }"
    })
    Long findForSumarySales(LocalDateTime from, LocalDateTime to);

    @Aggregation(pipeline = {
        "{ $match: { 'createdAt': { $gte: ?0, $lte: ?1 } } }",
        "{ $unwind: '$items' }",
        "{ $group: { _id: null, totalRevenue: { $sum: { $multiply: ['$items.quantity', '$items.price'] } } } }",
        "{ $project: { _id: 0, totalRevenue: 1 } }"
    })
    Long findForSummaryRevenue(LocalDateTime from, LocalDateTime to);
}
