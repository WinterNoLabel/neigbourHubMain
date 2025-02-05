package com.neighbourhub.permissions.entity;

import com.neighbourhub.community.entity.Community;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_community_roles")
@Getter
@Setter
@NoArgsConstructor
public class UserCommunityRole {
    @EmbeddedId
    private UserCommunityRoleId id;

    @ManyToOne
    @MapsId("communityId")
    private Community community;

    @ManyToOne
    @MapsId("roleId")
    private Role role;
}