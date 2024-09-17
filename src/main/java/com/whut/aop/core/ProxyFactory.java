package com.whut.aop.core;

import com.whut.aop.advice.Advice;
import com.whut.aop.advice.annotation.AdviceAnnotationUtil;
import com.whut.aop.advisor.DefaultAdvisor;
import lombok.Getter;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> we use this to create proxy object </p>
 * <p> it includes advisor collection and invocation chain building</p>
 *
 * @author whut2024
 * @since 2024-09-17
 */

public class ProxyFactory {

    @Getter
    private final Object target;

    private final Class<?>[] implementedInterfaces;

    private final List<DefaultAdvisor> advisorList;

    private final List<Advice> adviceList = new ArrayList<>();

    private final Class<?> superClass;

    /**
     * 构造方法，创建一个ProxyFactory对象
     *
     * @param target                目标对象，代理对象要代理的原始对象
     * @param implementedInterfaces 目标对象实现的接口数组
     * @param advisorList           切面列表，包含所有增强器
     * @param superClass            目标对象的父类
     */
    public ProxyFactory(Object target, Class<?>[] implementedInterfaces, List<DefaultAdvisor> advisorList, Class<?> superClass) {
        this.target = target;
        this.implementedInterfaces = implementedInterfaces;
        this.advisorList = advisorList;
        this.superClass = superClass;
    }

    /**
     * 构造方法，用于创建ProxyFactory对象
     *
     * @param target                代理对象要代理的原始对象
     * @param implementedInterfaces 目标对象实现的接口数组
     * @param advisorsClass         切面类，用于解析出切面列表
     * @param superClass            目标对象的父类
     */
    public ProxyFactory(Object target, Class<?>[] implementedInterfaces, Class<?> advisorsClass, Class<?> superClass) {
        this.target = target;
        this.implementedInterfaces = implementedInterfaces;
        this.advisorList = AdviceAnnotationUtil.resolveAdvisorFromClass(advisorsClass);
        this.superClass = superClass;
    }

    private final transient ConcurrentHashMap<String, List<Advice>> adviceCacheMap = new ConcurrentHashMap<>();


    /**
     * 获取代理对象
     *
     * @return 代理对象
     */
    public Object getProxy() {

        return Enhancer.create(superClass, implementedInterfaces, new AopProxy.ChainDefinitionMethodInterceptor(this));
    }

    public void addAdvice(Advice advice) {
        this.adviceList.add(advice);
    }


    /**
     * 获取方法的拦截器和动态拦截通知
     *
     * @param method      目标方法
     * @param targetClass 目标类
     * @return 方法的拦截器和动态拦截通知列表
     */
    protected List<Advice> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) {
        final String key = (method.getName() + "advice").intern();
        if (adviceCacheMap.containsKey(key))
            return adviceCacheMap.get(key);

        List<Advice> adviceList = null;
        synchronized (key) {
            if (!adviceCacheMap.containsKey(key)) {
                final List<Advice> finalAdviceList = new ArrayList<>(this.adviceList);
                advisorList.stream()
                        .filter(defaultAdvisor -> defaultAdvisor.getPointcut().matches(method, targetClass))
                        .forEach(defaultAdvisor -> finalAdviceList.add(defaultAdvisor.getAdvice()));

                adviceCacheMap.put(key, finalAdviceList);
                adviceList = finalAdviceList;
            }
        }

        return adviceList;
    }


}
