package com.neighbourhub.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class CommunityMemberId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "community_id")
    private Long communityId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommunityMemberId that = (CommunityMemberId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(communityId, that.communityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, communityId);
    }
}