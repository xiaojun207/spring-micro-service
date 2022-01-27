package com.microservice.auth.repository;

import com.microservice.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    @Query("select b from User b where b.uid = ?1")
    User findUserByUid(@Param("uid") Long uid);

    @Query("select b from User b where b.mobile = ?1")
    User findUserByMobile(@Param("mobile") String mobile);
}
