package com.neighbourhub.permissions.repository;

import com.neighbourhub.permissions.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByCommunityId(Long communityId);

    Optional<Role> findByIdAndCommunity_Id(Long id, Long communityId);

}