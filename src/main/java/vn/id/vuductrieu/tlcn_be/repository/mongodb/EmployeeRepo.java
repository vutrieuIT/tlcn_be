package vn.id.vuductrieu.tlcn_be.repository.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.EmployeeCollection;

public interface EmployeeRepo extends MongoRepository<EmployeeCollection, String> {

    @Query("{'account': ?0}")
    EmployeeCollection findByAccount(String account);
}
