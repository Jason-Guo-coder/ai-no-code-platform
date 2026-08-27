package com.gjq.ainocodeplatform.aop;

import com.gjq.ainocodeplatform.annotation.AuthCheck;
import com.gjq.ainocodeplatform.exception.BusinessException;
import com.gjq.ainocodeplatform.exception.ErrorCode;
import com.gjq.ainocodeplatform.model.entity.User;
import com.gjq.ainocodeplatform.model.enums.UserRoleEnum;
import com.gjq.ainocodeplatform.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 根据 {@link AuthCheck} 注解统一校验登录状态和用户角色。
 */
@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    /**
     * 在目标方法执行前完成权限校验。
     *
     * @param joinPoint 目标方法切入点
     * @param authCheck 权限校验注解
     * @return 目标方法执行结果
     * @throws Throwable 目标方法异常或权限校验异常
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        // 1. 获取当前登录用户
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        User loginUser = userService.getLoginUser(request);

        // 2. 仅要求登录时直接放行
        String mustRole = authCheck.mustRole();
        if (mustRole.isBlank()) {
            return joinPoint.proceed();
        }

        // 3. 校验目标角色和当前用户角色
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());
        if (mustRoleEnum == null || userRoleEnum == null) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        if (UserRoleEnum.ADMIN.equals(mustRoleEnum) && !UserRoleEnum.ADMIN.equals(userRoleEnum)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 4. 权限校验通过，执行目标方法
        return joinPoint.proceed();
    }
}
