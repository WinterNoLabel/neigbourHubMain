package com.neighbourhub.utils.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class AccessDeniedByPermissionException extends ResponseStatusException {
    public AccessDeniedByPermissionException(String message) {
        super(HttpStatusCode.valueOf(403), message);
    }
}
