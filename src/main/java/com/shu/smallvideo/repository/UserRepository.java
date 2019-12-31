package com.shu.smallvideo.repository;

import com.shu.smallvideo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shuxibing
 * @date 2019/12/28 11:30
 * @uint d9lab
 * @Description:
 */
@Repository
public interface UserRepository extends CrudRepository<User,Integer> {
   List<User> findByUserName(String userName);
   @Query(value = "select * from user where username like  CONCAT('%',?1,'%')",nativeQuery = true)
   List<User> findByUserNameLike(String userName);

    User findByMid(Integer mid);
}
