package com.neighbourhub.community.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommunityEventRepository extends JpaRepository<CommunityEvent, Long> {

    @Query("SELECT e FROM CommunityEvent e WHERE e.community.id = :communityId AND e.deletedDate IS NULL")
    List<CommunityEvent> findAllByCommunityId(@Param("communityId") Long communityId);

    @Query("SELECT e FROM CommunityEvent e WHERE e.creatorUserId = :userId AND e.deletedDate IS NULL")
    List<CommunityEvent> findAllByCreatorId(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE CommunityEvent e SET e.deletedDate = CURRENT_TIMESTAMP WHERE e.id = :eventId")
    void softDelete(@Param("eventId") Long eventId);
}