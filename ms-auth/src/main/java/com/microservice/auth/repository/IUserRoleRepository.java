package com.microservice.auth.repository;

import com.microservice.auth.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findUserRoleByUid(@Param("uid") Long uid);

    @Query(value = "delete from user_role where uid=?1 and role_id in ?2", nativeQuery = true)
    void deleteAllByUidAndRoleIds(@Param("uid")Long uid, List<Long> roleIds);

}
