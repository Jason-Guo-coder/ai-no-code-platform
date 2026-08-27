package com.gjq.ainocodeplatform.model.dto.user;

import com.gjq.ainocodeplatform.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户分页查询请求。
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {

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
     * 账号。
     */
    private String userAccount;

    /**
     * 用户简介。
     */
    private String userProfile;

    /**
     * 用户角色：user、admin、ban。
     */
    private String userRole;
}
