package com.demo.dao;

import com.demo.pojo.PushUser;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author mifei
 * @create 2021-04-02 17:03
 **/
@Repository
public interface UserJpaRepository extends MongoRepository<PushUser, ObjectId> {
	int updateWithPullAllUserIdsByAppIdAndTerminalIdAndTerminalPort(PushUser user, ObjectId appId, ObjectId terminalId, Long port);
}
