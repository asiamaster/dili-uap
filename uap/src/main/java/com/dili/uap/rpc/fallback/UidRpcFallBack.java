package com.dili.uap.rpc.fallback;

import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.rpc.UidRpc;

/**
 * @description:
 * @author: WM
 * @time: 2020/10/15 10:20
 */
public class UidRpcFallBack implements UidRpc {

    private Throwable throwable;

    UidRpcFallBack(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public BaseOutput<String> getFirmSerialNumber() {
        return BaseOutput.failure("服务熔断").setCode(ResultCode.FLOW_LIMIT);
    }

    @Override
    public BaseOutput<String> getBizNumber(String type) {
        return BaseOutput.failure("服务熔断").setCode(ResultCode.FLOW_LIMIT);
    }
}
