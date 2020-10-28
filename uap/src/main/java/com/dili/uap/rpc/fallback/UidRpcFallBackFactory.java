package com.dili.uap.rpc.fallback;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: WM
 * @time: 2020/10/15 10:19
 */
@Component
public class UidRpcFallBackFactory implements FallbackFactory<UidRpcFallBack> {
    @Override
    public UidRpcFallBack create(Throwable cause) {
        return new UidRpcFallBack(cause);
    }
}
