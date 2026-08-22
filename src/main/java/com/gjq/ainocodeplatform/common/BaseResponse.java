package com.gjq.ainocodeplatform.common;

import com.gjq.ainocodeplatform.exception.ErrorCode;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统一接口响应结果，包含调用码、数据和调用信息。
 *
 * @param <T> 响应数据类型
 */
@Data
public class BaseResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int code;

    private T data;

    private String message;

    /**
     * 创建完整响应结果。
     *
     * @param code    调用码
     * @param data    响应数据
     * @param message 调用信息
     */
    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    /**
     * 创建默认调用信息为空的响应结果。
     *
     * @param code 调用码
     * @param data 响应数据
     */
    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    /**
     * 根据错误码创建失败响应结果。
     *
     * @param errorCode 错误码
     */
    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}
