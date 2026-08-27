package com.gjq.ainocodeplatform.service;

import com.gjq.ainocodeplatform.common.DeleteRequest;
import com.gjq.ainocodeplatform.model.dto.user.UserLoginRequest;
import com.gjq.ainocodeplatform.model.dto.user.UserRegisterRequest;
import com.gjq.ainocodeplatform.model.dto.user.UserAddRequest;
import com.gjq.ainocodeplatform.model.dto.user.UserQueryRequest;
import com.gjq.ainocodeplatform.model.dto.user.UserUpdateRequest;
import com.gjq.ainocodeplatform.model.entity.User;
import com.gjq.ainocodeplatform.model.vo.LoginUserVO;
import com.gjq.ainocodeplatform.model.vo.UserVO;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 用户 服务层。
 *
 * @author <a href="https://github.com/Jason-Guo-coder">Jason-Guo-coder</a>
 */
public interface UserService extends IService<User> {

    /**
     * 注册用户。
     *
     * @param userRegisterRequest 用户注册请求
     * @return 新用户 id
     */
    long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 加密用户密码。
     *
     * @param userPassword 原始密码
     * @return 加密后的密码
     */
    String getEncryptPassword(String userPassword);

    /**
     * 获取脱敏后的登录用户信息。
     *
     * @param user 用户实体
     * @return 脱敏后的用户信息；用户为空时返回 {@code null}
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取当前登录用户的脱敏信息。
     *
     * @param request HTTP 请求
     * @return 脱敏后的当前用户信息
     */
    LoginUserVO getLoginUserVO(HttpServletRequest request);

    /**
     * 校验账号密码并记录登录态。
     *
     * @param userLoginRequest 用户登录请求
     * @param request HTTP 请求
     * @return 脱敏后的登录用户信息
     * @throws com.gjq.ainocodeplatform.exception.BusinessException 参数错误或账号密码不匹配时抛出
     */
    LoginUserVO userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request);

    /**
     * 获取当前登录用户的最新信息。
     *
     * @param request HTTP 请求
     * @return 当前登录用户实体
     * @throws com.gjq.ainocodeplatform.exception.BusinessException 用户未登录或用户不存在时抛出
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 注销当前用户并清除 Session 中的登录态。
     *
     * @param request HTTP 请求
     * @return 注销成功返回 {@code true}
     * @throws com.gjq.ainocodeplatform.exception.BusinessException 用户未登录时抛出
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏后的用户信息。
     *
     * @param user 用户实体
     * @return 脱敏后的用户信息；用户为空时返回 {@code null}
     */
    UserVO getUserVO(User user);

    /**
     * 批量获取脱敏后的用户信息。
     *
     * @param userList 用户实体列表
     * @return 脱敏后的用户列表
     */
    List<UserVO> getUserVOList(List<User> userList);

    /**
     * 根据用户查询请求构造查询条件。
     *
     * @param userQueryRequest 用户查询请求
     * @return MyBatis-Flex 查询条件
     * @throws com.gjq.ainocodeplatform.exception.BusinessException 请求为空时抛出
     */
    QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 管理员创建用户并设置加密后的默认密码。
     *
     * @param userAddRequest 用户创建请求
     * @return 新用户 id
     */
    long addUser(UserAddRequest userAddRequest);

    /**
     * 根据 id 获取未脱敏的用户实体。
     *
     * @param id 用户 id
     * @return 用户实体
     */
    User getUserById(long id);

    /**
     * 根据 id 获取脱敏后的用户信息。
     *
     * @param id 用户 id
     * @return 脱敏后的用户信息
     */
    UserVO getUserVOById(long id);

    /**
     * 根据 id 删除用户。
     *
     * @param deleteRequest 删除请求
     * @return 删除结果
     */
    boolean deleteUser(DeleteRequest deleteRequest);

    /**
     * 更新用户信息。
     *
     * @param userUpdateRequest 用户更新请求
     * @return 更新成功返回 {@code true}
     */
    boolean updateUser(UserUpdateRequest userUpdateRequest);

    /**
     * 分页查询并返回脱敏后的用户列表。
     *
     * @param userQueryRequest 用户查询请求
     * @return 脱敏后的用户分页数据
     */
    Page<UserVO> listUserVOByPage(UserQueryRequest userQueryRequest);
}
