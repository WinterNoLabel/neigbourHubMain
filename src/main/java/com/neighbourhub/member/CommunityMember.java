package com.neighbourhub.member;

import com.neighbourhub.community.entity.Community;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "community_members")
@Getter
@Setter
@NoArgsConstructor
public class CommunityMember {

    @EmbeddedId
    private CommunityMemberId id;

    @ManyToOne
    @MapsId("communityId")
    @JoinColumn(name = "community_id", insertable = false, updatable = false)
    private Community community;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt = LocalDateTime.now();
}