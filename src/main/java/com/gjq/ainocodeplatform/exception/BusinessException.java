package com.gjq.ainocodeplatform.exception;

import lombok.Getter;

/**
 * 应用业务异常，携带可供前端统一处理的错误码。
 */
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码。
     */
    private final int code;

    /**
     * 使用指定错误码和错误信息创建业务异常。
     *
     * @param code    错误码
     * @param message 错误信息
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 使用错误码枚举的默认信息创建业务异常。
     *
     * @param errorCode 错误码
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    /**
     * 使用错误码枚举和自定义信息创建业务异常。
     *
     * @param errorCode 错误码
     * @param message    自定义错误信息
     */
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }
}
