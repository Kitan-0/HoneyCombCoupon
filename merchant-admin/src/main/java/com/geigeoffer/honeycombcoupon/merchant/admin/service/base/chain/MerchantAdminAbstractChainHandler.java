package com.geigeoffer.honeycombcoupon.merchant.admin.service.base.chain;

import org.springframework.core.Ordered;

public interface MerchantAdminAbstractChainHandler<T> extends Ordered {
    void handler(T requestParam);
    String mark();
}
