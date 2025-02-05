package com.neighbourhub.permissions.repository;

import com.neighbourhub.permissions.entity.Role;
import com.neighbourhub.permissions.entity.UserCommunityRole;
import com.neighbourhub.permissions.entity.UserCommunityRoleId;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserCommunityRoleRepository extends JpaRepository<UserCommunityRole, UserCommunityRoleId> {

    @Query("SELECT ur.role FROM UserCommunityRole ur JOIN FETCH ur.role.permissions WHERE ur.id.userId = :userId AND ur.community.id = :communityId")
    List<Role> findRolesByUserAndCommunity(@Param("userId") Long userId, @Param("communityId") Long communityId);

    void deleteAllByRoleId(Long roleId);
}
