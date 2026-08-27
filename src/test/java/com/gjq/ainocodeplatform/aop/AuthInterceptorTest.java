package com.gjq.ainocodeplatform.aop;

import com.gjq.ainocodeplatform.annotation.AuthCheck;
import com.gjq.ainocodeplatform.exception.BusinessException;
import com.gjq.ainocodeplatform.exception.ErrorCode;
import com.gjq.ainocodeplatform.model.entity.User;
import com.gjq.ainocodeplatform.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.gjq.ainocodeplatform.constant.UserConstant.ADMIN_ROLE;
import static com.gjq.ainocodeplatform.constant.UserConstant.DEFAULT_ROLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthInterceptorTest {

    private AuthInterceptor authInterceptor;

    private UserService userService;

    private ProceedingJoinPoint joinPoint;

    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        authInterceptor = new AuthInterceptor();
        userService = mock(UserService.class);
        joinPoint = mock(ProceedingJoinPoint.class);
        request = new MockHttpServletRequest();
        ReflectionTestUtils.setField(authInterceptor, "userService", userService);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void shouldAllowAdministratorToAccessAdminMethod() throws Throwable {
        when(userService.getLoginUser(request)).thenReturn(User.builder().userRole(ADMIN_ROLE).build());
        when(joinPoint.proceed()).thenReturn("success");

        Object result = authInterceptor.doInterceptor(joinPoint, getAuthCheck("adminOnly"));

        assertEquals("success", result);
        verify(joinPoint).proceed();
    }

    @Test
    void shouldRejectNormalUserFromAdminMethod() throws Throwable {
        when(userService.getLoginUser(request)).thenReturn(User.builder().userRole(DEFAULT_ROLE).build());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> authInterceptor.doInterceptor(joinPoint, getAuthCheck("adminOnly")));

        assertEquals(ErrorCode.NO_AUTH_ERROR.getCode(), exception.getCode());
        verify(joinPoint, never()).proceed();
    }

    @Test
    void shouldAllowLoggedInUserWhenNoRoleIsRequired() throws Throwable {
        when(userService.getLoginUser(request)).thenReturn(User.builder().userRole(DEFAULT_ROLE).build());
        when(joinPoint.proceed()).thenReturn("success");

        Object result = authInterceptor.doInterceptor(joinPoint, getAuthCheck("loginRequired"));

        assertEquals("success", result);
        verify(joinPoint).proceed();
    }

    @Test
    void shouldRejectUnknownRequiredRole() throws Throwable {
        when(userService.getLoginUser(request)).thenReturn(User.builder().userRole(ADMIN_ROLE).build());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> authInterceptor.doInterceptor(joinPoint, getAuthCheck("unknownRole")));

        assertEquals(ErrorCode.NO_AUTH_ERROR.getCode(), exception.getCode());
        verify(joinPoint, never()).proceed();
    }

    private AuthCheck getAuthCheck(String methodName) throws NoSuchMethodException {
        return SecuredMethods.class.getDeclaredMethod(methodName).getAnnotation(AuthCheck.class);
    }

    private static class SecuredMethods {

        @AuthCheck(mustRole = ADMIN_ROLE)
        void adminOnly() {
        }

        @AuthCheck
        void loginRequired() {
        }

        @AuthCheck(mustRole = "unknown")
        void unknownRole() {
        }
    }
}
