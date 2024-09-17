package com.whut.aop.chain;

import com.whut.aop.advice.Advice;
import net.sf.cglib.proxy.MethodProxy;

import java.util.List;

/**
 * 调用链对象
 * @author whut2024
 * @since 2024-09-17
 */
public class DefaultMethodInvocation implements MethodInvocation {


    private final Object target;

    private final Object[] args;

    private int index;

    private final MethodProxy methodProxy;

    private final List<Advice> adviceList;


    public DefaultMethodInvocation(Object target, Object[] args, MethodProxy methodProxy, List<Advice> adviceList) {
        this.target = target;
        this.args = args;
        this.methodProxy = methodProxy;
        this.adviceList = adviceList;
    }

    /**
     * 递归调用所有增强的方法
     */
    @Override
    public Object proceed() throws Throwable {
        if (index == adviceList.size()) {
            return methodProxy.invoke(target, args);
        }

        final Advice advice = adviceList.get(index++);
        return advice.invoke(this);
    }
}
