package vn.id.vuductrieu.tlcn_be.repository.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.UserCollection;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.document.ItemDocument;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends MongoRepository<UserCollection, String> {

    @Query("{'email': ?0}")
    Optional<UserCollection> findByEmail(String email);


    @Query(value = "{}", fields = "{'cart': 0, 'password': 0, 'resetCode': 0}")
    List<UserCollection> findAllExceptCartAndPassAndCode();

    @Query(value = "{'id': ?0}", fields = "{'cart': 0, 'password': 0, 'resetCode': 0}")
    Optional<UserCollection> findByIdExceptCartAndPassAndCode(String id);
}
