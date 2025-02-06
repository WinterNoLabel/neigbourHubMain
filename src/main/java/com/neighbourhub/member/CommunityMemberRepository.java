package com.neighbourhub.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommunityMemberRepository extends JpaRepository<CommunityMember, CommunityMemberId> {

    @Query("SELECT cm FROM CommunityMember cm WHERE cm.id.communityId = :communityId")
    List<CommunityMember> findAllByCommunityId(@Param("communityId") Long communityId);

    @Query("SELECT cm FROM CommunityMember cm WHERE cm.id.userId = :userId")
    List<CommunityMember> findAllByUserId(@Param("userId") Long userId);

    boolean existsById_CommunityIdAndId_UserId(Long communityId, Long userId);

    void deleteById_CommunityIdAndId_UserId(Long communityId, Long userId);
}