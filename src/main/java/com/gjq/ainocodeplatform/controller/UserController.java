package com.gjq.ainocodeplatform.controller;

import com.gjq.ainocodeplatform.annotation.AuthCheck;
import com.gjq.ainocodeplatform.common.BaseResponse;
import com.gjq.ainocodeplatform.common.DeleteRequest;
import com.gjq.ainocodeplatform.common.ResultUtils;
import com.gjq.ainocodeplatform.constant.UserConstant;
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
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户 控制层。
 *
 * @author <a href="https://github.com/Jason-Guo-coder">Jason-Guo-coder</a>
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;


    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册请求
     * @return 注册结果
     */
    @PostMapping("register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        return ResultUtils.success(userService.userRegister(userRegisterRequest));
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登录请求
     * @param request HTTP 请求
     * @return 脱敏后的登录用户信息
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest,
                                               HttpServletRequest request) {
        return ResultUtils.success(userService.userLogin(userLoginRequest, request));
    }

    /**
     * 获取当前登录用户
     *
     * @param request HTTP 请求
     * @return 脱敏后的当前用户信息
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        return ResultUtils.success(userService.getLoginUserVO(request));
    }

    /**
     * 用户注销
     *
     * @param request HTTP 请求
     * @return 注销结果
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        return ResultUtils.success(userService.userLogout(request));
    }

    /**
     * 管理员创建用户
     *
     * <p>新用户使用默认密码 {@code 12345678}，保存前会按当前密码规则加密。</p>
     *
     * @param userAddRequest 用户创建请求
     * @return 新用户 id
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        return ResultUtils.success(userService.addUser(userAddRequest));
    }

    /**
     * 管理员根据 id 获取未脱敏的用户信息
     *
     * @param id 用户 id
     * @return 用户实体
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(long id) {
        return ResultUtils.success(userService.getUserById(id));
    }

    /**
     * 根据 id 获取脱敏后的用户信息
     *
     * @param id 用户 id
     * @return 脱敏后的用户信息
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(long id) {
        return ResultUtils.success(userService.getUserVOById(id));
    }

    /**
     * 管理员根据 id 删除用户
     *
     * @param deleteRequest 删除请求
     * @return 删除结果
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        return ResultUtils.success(userService.deleteUser(deleteRequest));
    }

    /**
     * 管理员更新用户
     *
     * @param userUpdateRequest 用户更新请求
     * @return 更新结果
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        return ResultUtils.success(userService.updateUser(userUpdateRequest));
    }

    /**
     * 管理员分页获取脱敏后的用户列表
     *
     * @param userQueryRequest 用户查询请求
     * @return 脱敏后的用户分页数据
     */
    @PostMapping("/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest) {
        return ResultUtils.success(userService.listUserVOByPage(userQueryRequest));
    }

}
