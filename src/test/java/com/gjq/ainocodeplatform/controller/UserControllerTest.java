package com.gjq.ainocodeplatform.controller;

import com.gjq.ainocodeplatform.annotation.AuthCheck;
import com.gjq.ainocodeplatform.common.BaseResponse;
import com.gjq.ainocodeplatform.common.DeleteRequest;
import com.gjq.ainocodeplatform.model.dto.user.UserLoginRequest;
import com.gjq.ainocodeplatform.model.dto.user.UserRegisterRequest;
import com.gjq.ainocodeplatform.model.dto.user.UserAddRequest;
import com.gjq.ainocodeplatform.model.dto.user.UserQueryRequest;
import com.gjq.ainocodeplatform.model.dto.user.UserUpdateRequest;
import com.gjq.ainocodeplatform.model.entity.User;
import com.gjq.ainocodeplatform.model.vo.LoginUserVO;
import com.gjq.ainocodeplatform.model.vo.UserVO;
import com.gjq.ainocodeplatform.service.UserService;
import com.mybatisflex.core.paginate.Page;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;

import static com.gjq.ainocodeplatform.constant.UserConstant.ADMIN_ROLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private UserController userController;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userController = new UserController();
        userService = mock(UserService.class);
        ReflectionTestUtils.setField(userController, "userService", userService);
    }

    @Test
    void shouldDelegateAccountOperationsToService() {
        UserRegisterRequest registerRequest = new UserRegisterRequest();
        UserLoginRequest loginRequest = new UserLoginRequest();
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        LoginUserVO loginUserVO = new LoginUserVO();
        loginUserVO.setId(1L);
        when(userService.userRegister(registerRequest)).thenReturn(1L);
        when(userService.userLogin(loginRequest, servletRequest)).thenReturn(loginUserVO);
        when(userService.getLoginUserVO(servletRequest)).thenReturn(loginUserVO);
        when(userService.userLogout(servletRequest)).thenReturn(true);

        assertEquals(1L, userController.userRegister(registerRequest).getData());
        assertEquals(loginUserVO, userController.userLogin(loginRequest, servletRequest).getData());
        assertEquals(loginUserVO, userController.getLoginUser(servletRequest).getData());
        assertTrue(userController.userLogout(servletRequest).getData());

        verify(userService).userRegister(registerRequest);
        verify(userService).userLogin(loginRequest, servletRequest);
        verify(userService).getLoginUserVO(servletRequest);
        verify(userService).userLogout(servletRequest);
    }

    @Test
    void shouldDelegateUserManagementOperationsToService() {
        UserAddRequest addRequest = new UserAddRequest();
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        UserQueryRequest queryRequest = new UserQueryRequest();
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.setId(1L);
        User user = User.builder().id(1L).build();
        UserVO userVO = new UserVO();
        userVO.setId(1L);
        Page<UserVO> userVOPage = new Page<>(1, 10, 1);
        when(userService.addUser(addRequest)).thenReturn(1L);
        when(userService.getUserById(1L)).thenReturn(user);
        when(userService.getUserVOById(1L)).thenReturn(userVO);
        when(userService.deleteUser(deleteRequest)).thenReturn(true);
        when(userService.updateUser(updateRequest)).thenReturn(true);
        when(userService.listUserVOByPage(queryRequest)).thenReturn(userVOPage);

        assertEquals(1L, userController.addUser(addRequest).getData());
        assertEquals(user, userController.getUserById(1L).getData());
        assertEquals(userVO, userController.getUserVOById(1L).getData());
        assertTrue(userController.deleteUser(deleteRequest).getData());
        assertTrue(userController.updateUser(updateRequest).getData());
        BaseResponse<Page<UserVO>> response = userController.listUserVOByPage(queryRequest);
        assertEquals(userVOPage, response.getData());

        verify(userService).addUser(addRequest);
        verify(userService).getUserById(1L);
        verify(userService).getUserVOById(1L);
        verify(userService).deleteUser(deleteRequest);
        verify(userService).updateUser(updateRequest);
        verify(userService).listUserVOByPage(queryRequest);
    }

    @Test
    void shouldRequireAdminRoleForManagementMethods() throws NoSuchMethodException {
        assertAdminMethod("addUser", UserAddRequest.class);
        assertAdminMethod("getUserById", long.class);
        assertAdminMethod("deleteUser", DeleteRequest.class);
        assertAdminMethod("updateUser", UserUpdateRequest.class);
        assertAdminMethod("listUserVOByPage", UserQueryRequest.class);

        Method publicMethod = UserController.class.getDeclaredMethod("getUserVOById", long.class);
        assertNull(publicMethod.getAnnotation(AuthCheck.class));
    }

    private void assertAdminMethod(String methodName, Class<?> parameterType) throws NoSuchMethodException {
        Method method = UserController.class.getDeclaredMethod(methodName, parameterType);
        AuthCheck authCheck = method.getAnnotation(AuthCheck.class);

        assertNotNull(authCheck);
        assertEquals(ADMIN_ROLE, authCheck.mustRole());
    }
}
