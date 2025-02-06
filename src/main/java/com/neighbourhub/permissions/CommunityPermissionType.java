package com.neighbourhub.permissions;

import java.util.Arrays;
import java.util.List;

public enum CommunityPermissionType {
    EDIT_DESCRIPTION,
    EDIT_NAME,
    WRITE_TO_CHAT,
    INVITE_USERS,
    KICK_MEMBER,
    CREATE_ROLES,
    MANAGE_ROLES,
    STREAM,
    MANAGE_MEMBERS;

    public static List<CommunityPermissionType> allPerms() {
        return Arrays.stream(CommunityPermissionType.values()).toList();
    }
}