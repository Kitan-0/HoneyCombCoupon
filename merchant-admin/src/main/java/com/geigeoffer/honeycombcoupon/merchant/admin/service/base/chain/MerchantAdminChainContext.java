package com.geigeoffer.honeycombcoupon.merchant.admin.service.base.chain;

import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Component
public final class MerchantAdminChainContext<T> implements ApplicationContextAware, CommandLineRunner {

    private ApplicationContext applicationContext;
    private final Map<String, List<MerchantAdminAbstractChainHandler>> abstractChainHandlerContainer = new HashMap<>();

    /**
     * 执行责任链
     * @param mark 责任链标识
     * @param requestParam 待检验参数
     */
    public void handler(String mark, T requestParam) {
        List<MerchantAdminAbstractChainHandler> abstractChainHandlers = abstractChainHandlerContainer.get(mark);
        if(CollectionUtils.isEmpty(abstractChainHandlers)) {
            throw new RuntimeException(String.format("[%s] 责任链ID未定义.",mark));
        }
        abstractChainHandlers.forEach(chainHandler->chainHandler.handler(requestParam));
    }

    @Override
    public void run(String... args) throws Exception {
        Map<String, MerchantAdminAbstractChainHandler> chainFilterMap = applicationContext.getBeansOfType(MerchantAdminAbstractChainHandler.class);
        chainFilterMap.forEach((beanName, bean)->{
            List<MerchantAdminAbstractChainHandler> abstractChainHandlers = abstractChainHandlerContainer.getOrDefault(bean.mark(), new ArrayList<>());
            abstractChainHandlers.add(bean);
            abstractChainHandlerContainer.put(bean.mark(), abstractChainHandlers);
        });
        //对Mark相应的责任链进行排序，
        abstractChainHandlerContainer.forEach((mark, unsortedChainHandlers)->{
            unsortedChainHandlers.sort(Comparator.comparing(Ordered::getOrder));
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
