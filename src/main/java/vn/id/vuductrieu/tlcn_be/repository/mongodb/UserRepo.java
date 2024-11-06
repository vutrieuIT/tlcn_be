package vn.id.vuductrieu.tlcn_be.repository.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.UserCollection;

public interface UserRepo extends MongoRepository<UserCollection, String> {
}
