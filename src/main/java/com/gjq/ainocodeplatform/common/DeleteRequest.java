package com.gjq.ainocodeplatform.common;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 通用删除请求参数。
 */
@Data
public class DeleteRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 待删除数据的 id。
     */
    private Long id;
}
