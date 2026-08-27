package com.gjq.ainocodeplatform.service.impl;

import com.gjq.ainocodeplatform.common.DeleteRequest;
import com.gjq.ainocodeplatform.exception.BusinessException;
import com.gjq.ainocodeplatform.exception.ErrorCode;
import com.gjq.ainocodeplatform.model.dto.user.UserAddRequest;
import com.gjq.ainocodeplatform.model.dto.user.UserQueryRequest;
import com.gjq.ainocodeplatform.model.dto.user.UserUpdateRequest;
import com.gjq.ainocodeplatform.model.entity.User;
import com.gjq.ainocodeplatform.model.vo.UserVO;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class UserServiceImplTest {

    private final UserServiceImpl userService = spy(new UserServiceImpl());

    @Test
    void shouldCreateUserWithEncryptedDefaultPassword() {
        UserAddRequest request = new UserAddRequest();
        request.setUserAccount("testAccount");
        request.setUserName("测试用户");
        request.setUserRole("user");
        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(10L);
            return true;
        }).when(userService).save(any(User.class));

        long userId = userService.addUser(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals(10L, userId);
        assertEquals("testAccount", savedUser.getUserAccount());
        assertEquals("测试用户", savedUser.getUserName());
        assertEquals("user", savedUser.getUserRole());
        assertEquals(userService.getEncryptPassword("12345678"), savedUser.getUserPassword());
    }

    @Test
    void shouldConvertUserToSanitizedUserVO() {
        LocalDateTime createTime = LocalDateTime.now();
        User user = User.builder()
                .id(1L)
                .userAccount("testAccount")
                .userPassword("secret")
                .userName("测试用户")
                .userAvatar("avatar.png")
                .userProfile("简介")
                .userRole("user")
                .createTime(createTime)
                .build();

        UserVO userVO = userService.getUserVO(user);

        assertNotNull(userVO);
        assertEquals(user.getId(), userVO.getId());
        assertEquals(user.getUserAccount(), userVO.getUserAccount());
        assertEquals(user.getUserName(), userVO.getUserName());
        assertEquals(user.getUserAvatar(), userVO.getUserAvatar());
        assertEquals(user.getUserProfile(), userVO.getUserProfile());
        assertEquals(user.getUserRole(), userVO.getUserRole());
        assertEquals(createTime, userVO.getCreateTime());
        assertFalse(Arrays.stream(UserVO.class.getDeclaredFields())
                .anyMatch(field -> "userPassword".equals(field.getName())));
    }

    @Test
    void shouldHandleNullAndEmptyUsers() {
        assertNull(userService.getUserVO(null));
        assertEquals(List.of(), userService.getUserVOList(null));
        assertEquals(List.of(), userService.getUserVOList(List.of()));
    }

    @Test
    void shouldRejectNullQueryRequest() {
        BusinessException exception = assertThrows(BusinessException.class,
                () -> userService.getQueryWrapper(null));

        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), exception.getCode());
    }

    @Test
    void shouldBuildQueryWrapperFromRequest() {
        UserQueryRequest emptyRequest = new UserQueryRequest();
        UserQueryRequest conditionRequest = new UserQueryRequest();
        conditionRequest.setId(1L);

        assertFalse(userService.getQueryWrapper(emptyRequest).hasCondition());
        assertNotNull(userService.getQueryWrapper(conditionRequest));
        assertTrue(userService.getQueryWrapper(conditionRequest).hasCondition());
    }

    @Test
    void shouldGetDeleteAndUpdateUser() {
        User user = User.builder().id(1L).userAccount("testAccount").userPassword("secret").build();
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.setId(1L);
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setId(1L);
        updateRequest.setUserName("新昵称");
        doReturn(user).when(userService).getById(1L);
        doReturn(true).when(userService).removeById(1L);
        doReturn(true).when(userService).updateById(any(User.class));

        assertEquals(user, userService.getUserById(1L));
        assertEquals(1L, userService.getUserVOById(1L).getId());
        assertTrue(userService.deleteUser(deleteRequest));
        assertTrue(userService.updateUser(updateRequest));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService).updateById(userCaptor.capture());
        assertEquals(1L, userCaptor.getValue().getId());
        assertEquals("新昵称", userCaptor.getValue().getUserName());
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturnSanitizedUserPage() {
        UserQueryRequest request = new UserQueryRequest();
        User user = User.builder().id(1L).userAccount("testAccount").userPassword("secret").build();
        Page<User> userPage = new Page<>(1, 10, 1);
        userPage.setRecords(List.of(user));
        doReturn(userPage).when(userService).page(any(Page.class), any(QueryWrapper.class));

        Page<UserVO> result = userService.listUserVOByPage(request);

        assertEquals(1, result.getTotalRow());
        assertEquals(1, result.getRecords().size());
        assertEquals(1L, result.getRecords().get(0).getId());
        assertFalse(Arrays.stream(UserVO.class.getDeclaredFields())
                .anyMatch(field -> "userPassword".equals(field.getName())));
    }
}
