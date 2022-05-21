package com.planet.develop.Repository;

import com.planet.develop.Entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.userName= :username WHERE u.userId= :user_id")
    void updateName(@Param("username") String username, @Param("user_id") String user_id);

    // 이미 가입된 계정인지 찾기
    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query(" select u from User u where u.userId = :user_id")
    Optional<User> findByEmail(@Param("user_id") String user_id);

}
