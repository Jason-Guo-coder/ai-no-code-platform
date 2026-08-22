package com.gjq.ainocodeplatform.controller;

import com.gjq.ainocodeplatform.common.BaseResponse;
import com.gjq.ainocodeplatform.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供应用健康检查接口。
 */
@RestController
@RequestMapping("/health")
public class HealthController {

    /**
     * 返回应用存活状态。
     *
     * @return 应用状态文本
     */
    @GetMapping("/")
    public BaseResponse<String> healthCheck() {
        return ResultUtils.success("ok");
    }
}
