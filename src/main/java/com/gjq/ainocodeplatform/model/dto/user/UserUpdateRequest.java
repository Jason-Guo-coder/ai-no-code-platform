package com.gjq.ainocodeplatform.model.dto.user;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 管理员更新用户请求。
 */
@Data
public class UserUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户 id。
     */
    private Long id;

    /**
     * 用户昵称。
     */
    private String userName;

    /**
     * 用户头像。
     */
    private String userAvatar;

    /**
     * 用户简介。
     */
    private String userProfile;

    /**
     * 用户角色：user、admin。
     */
    private String userRole;
}
