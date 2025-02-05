package com.neighbourhub.permissions;

import com.neighbourhub.permissions.service.CommunityPermissionService;
import com.neighbourhub.utils.exception.AccessDeniedByPermissionException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Aspect
@Component
@RequiredArgsConstructor
public class CommunityPermissionAspect {
    private final CommunityPermissionService permissionService;
    private final SpelExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer paramNameDiscoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(communityPermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, CommunityPermission communityPermission) throws Throwable {
        Long communityId = evaluateSpel(communityPermission.communityId(), joinPoint);
        Long userId = evaluateSpel(communityPermission.userId(), joinPoint);

        // Проверка прав
        if (!permissionService.hasPermission(userId, communityId, communityPermission.permission())) {
            throw new AccessDeniedByPermissionException("User " + userId + " has no permission: " + communityPermission.permission());
        }

        return joinPoint.proceed();
    }

    private Long evaluateSpel(String expression, ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        EvaluationContext context = new MethodBasedEvaluationContext(
                joinPoint.getTarget(),
                signature.getMethod(),
                joinPoint.getArgs(),
                paramNameDiscoverer
        );
        return parser.parseExpression(expression).getValue(context, Long.class);
    }
}