package com.neighbourhub.member.dto;

import com.neighbourhub.permissions.entity.RoleNamePermissionsDTO;
import lombok.Data;

import java.util.List;

@Data
public class CommunityMemberWithRoles {

    Long id;
    List<RoleNamePermissionsDTO> roles;

}
