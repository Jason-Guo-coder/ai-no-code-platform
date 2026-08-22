package com.gjq.ainocodeplatform.common;

import com.gjq.ainocodeplatform.exception.ErrorCode;

/**
 * 统一响应结果构造工具。
 */
public final class ResultUtils {

    private ResultUtils() {
    }

    /**
     * 创建成功响应。
     *
     * @param data 响应数据
     * @param <T>  数据类型
     * @return 成功响应
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(ErrorCode.SUCCESS.getCode(), data, ErrorCode.SUCCESS.getMessage());
    }

    /**
     * 根据错误码创建失败响应。
     *
     * @param errorCode 错误码
     * @return 失败响应
     */
    public static BaseResponse<?> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 根据错误码和错误信息创建失败响应。
     *
     * @param code    错误码
     * @param message 错误信息
     * @return 失败响应
     */
    public static BaseResponse<?> error(int code, String message) {
        return new BaseResponse<>(code, null, message);
    }

    /**
     * 根据错误码枚举和自定义错误信息创建失败响应。
     *
     * @param errorCode 错误码
     * @param message   错误信息
     * @return 失败响应
     */
    public static BaseResponse<?> error(ErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode.getCode(), null, message);
    }
}
