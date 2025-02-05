package com.neighbourhub.permissions.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class UserCommunityRoleId implements Serializable {
    private Long userId;
    private Long communityId;
    private Long roleId;
}