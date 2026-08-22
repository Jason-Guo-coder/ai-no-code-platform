package com.gjq.ainocodeplatform.exception;

/**
 * 统一业务异常抛出工具。
 */
public final class ThrowUtils {

    private ThrowUtils() {
    }

    /**
     * 条件成立时抛出指定运行时异常。
     *
     * @param condition        抛出异常的条件
     * @param runtimeException 要抛出的运行时异常
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立时根据错误码抛出业务异常。
     *
     * @param condition 抛出异常的条件
     * @param errorCode 错误码
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }

    /**
     * 条件成立时根据错误码和自定义信息抛出业务异常。
     *
     * @param condition 抛出异常的条件
     * @param errorCode 错误码
     * @param message   自定义错误信息
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }
}
