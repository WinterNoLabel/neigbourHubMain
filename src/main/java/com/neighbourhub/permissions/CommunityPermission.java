package com.neighbourhub.permissions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommunityPermission {
    CommunityPermissionType permission();
    String communityId(); // SpEL-выражение (например, "#communityId")
    String userId();      // SpEL-выражение (например, "#userId")
}