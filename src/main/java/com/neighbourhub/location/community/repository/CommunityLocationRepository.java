package com.neighbourhub.location.community.repository;

import com.neighbourhub.location.community.entity.CommunityLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CommunityLocationRepository extends JpaRepository<CommunityLocation, Long>, JpaSpecificationExecutor<CommunityLocation> {
    Optional<CommunityLocation> findByCommunityId(Long communityId);
}