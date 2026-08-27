package com.gjq.ainocodeplatform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.gjq.ainocodeplatform.common.DeleteRequest;
import com.gjq.ainocodeplatform.exception.BusinessException;
import com.gjq.ainocodeplatform.exception.ErrorCode;
import com.gjq.ainocodeplatform.exception.ThrowUtils;
import com.gjq.ainocodeplatform.mapper.UserMapper;
import com.gjq.ainocodeplatform.model.dto.user.UserLoginRequest;
import com.gjq.ainocodeplatform.model.dto.user.UserRegisterRequest;
import com.gjq.ainocodeplatform.model.dto.user.UserAddRequest;
import com.gjq.ainocodeplatform.model.dto.user.UserQueryRequest;
import com.gjq.ainocodeplatform.model.dto.user.UserUpdateRequest;
import com.gjq.ainocodeplatform.model.entity.User;
import com.gjq.ainocodeplatform.model.enums.UserRoleEnum;
import com.gjq.ainocodeplatform.model.vo.LoginUserVO;
import com.gjq.ainocodeplatform.model.vo.UserVO;
import com.gjq.ainocodeplatform.service.UserService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.gjq.ainocodeplatform.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户 服务层实现。
 *
 * @author <a href="https://github.com/Jason-Guo-coder">Jason-Guo-coder</a>
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    // 校验注册信息并创建新用户
    @Override
    public long userRegister(UserRegisterRequest userRegisterRequest) {
        // 1. 校验
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StrUtil.hasBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        // 2. 检查是否重复
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.mapper.selectCountByQuery(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }
        // 3. 加密
        String encryptPassword = getEncryptPassword(userPassword);
        // 4. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName("无名");
        user.setUserRole(UserRoleEnum.USER.getValue());
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        }
        return user.getId();
    }

    // 将用户实体转换为脱敏后的登录用户信息
    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtil.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    // 获取当前登录用户的脱敏信息
    @Override
    public LoginUserVO getLoginUserVO(HttpServletRequest request) {
        return getLoginUserVO(getLoginUser(request));
    }

    // 校验账号密码并记录用户登录态
    @Override
    public LoginUserVO userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request) {
        // 1. 校验
        ThrowUtils.throwIf(userLoginRequest == null || request == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StrUtil.hasBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2. 加密密码并查询用户
        String encryptPassword = getEncryptPassword(userPassword);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.mapper.selectOneByQuery(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 3. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        // 4. 获得脱敏后的用户信息
        return this.getLoginUserVO(user);
    }

    // 获取当前登录用户在数据库中的最新信息
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 1. 从 Session 中读取登录用户
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        // 2. 查询并返回数据库中的最新用户信息
        currentUser = this.getById(currentUser.getId());
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    // 清除当前用户的登录态
    @Override
    public boolean userLogout(HttpServletRequest request) {
        // 1. 校验当前用户是否已登录
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }

        // 2. 移除 Session 中的登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    // 对用户密码加盐并生成加密结果
    @Override
    public String getEncryptPassword(String userPassword) {
        // 盐值，混淆密码
        final String SALT = "ainocode";
        return DigestUtils.md5DigestAsHex((userPassword + SALT).getBytes(StandardCharsets.UTF_8));
    }

    // 将用户实体转换为脱敏后的用户信息
    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        return userVO;
    }

    // 批量将用户实体转换为脱敏后的用户信息
    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    // 根据用户查询请求构造数据库查询条件
    @Override
    public QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String userAccount = userQueryRequest.getUserAccount();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        return QueryWrapper.create()
                .eq("id", id)
                .eq("userRole", userRole)
                .like("userAccount", userAccount)
                .like("userName", userName)
                .like("userProfile", userProfile)
                .orderBy(sortField, "ascend".equals(sortOrder));
    }

    // 使用默认密码创建用户
    @Override
    public long addUser(UserAddRequest userAddRequest) {
        // 1. 校验请求并转换为用户实体
        ThrowUtils.throwIf(userAddRequest == null, ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtil.copyProperties(userAddRequest, user);

        // 2. 设置加密后的默认密码
        final String defaultPassword = "12345678";
        user.setUserPassword(getEncryptPassword(defaultPassword));

        // 3. 保存用户并返回主键
        boolean result = save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return user.getId();
    }

    // 根据 id 获取未脱敏的用户信息
    @Override
    public User getUserById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        User user = getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return user;
    }

    // 根据 id 获取脱敏后的用户信息
    @Override
    public UserVO getUserVOById(long id) {
        return getUserVO(getUserById(id));
    }

    // 根据 id 删除用户
    @Override
    public boolean deleteUser(DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return removeById(deleteRequest.getId());
    }

    // 更新指定用户的信息
    @Override
    public boolean updateUser(UserUpdateRequest userUpdateRequest) {
        // 1. 校验请求并转换为用户实体
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtil.copyProperties(userUpdateRequest, user);

        // 2. 更新用户信息
        boolean result = updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return true;
    }

    // 分页查询并返回脱敏后的用户列表
    @Override
    public Page<UserVO> listUserVOByPage(UserQueryRequest userQueryRequest) {
        // 1. 校验分页请求并查询用户实体
        ThrowUtils.throwIf(userQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long pageNum = userQueryRequest.getPageNum();
        long pageSize = userQueryRequest.getPageSize();
        Page<User> userPage = page(Page.of(pageNum, pageSize), getQueryWrapper(userQueryRequest));

        // 2. 转换为脱敏分页结果
        Page<UserVO> userVOPage = new Page<>(pageNum, pageSize, userPage.getTotalRow());
        userVOPage.setRecords(getUserVOList(userPage.getRecords()));
        return userVOPage;
    }
}
