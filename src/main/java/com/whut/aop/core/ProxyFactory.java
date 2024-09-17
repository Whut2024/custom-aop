package com.whut.aop.core;

import com.whut.aop.advice.Advice;
import com.whut.aop.advisor.Advisor;
import com.whut.aop.advisor.DefaultAdvisor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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

    public ProxyFactory(Object target, Class<?>[] implementedInterfaces, List<DefaultAdvisor> advisorList, Class<?> superClass) {
        this.target = target;
        this.implementedInterfaces = implementedInterfaces;
        this.advisorList = advisorList;
        this.superClass = superClass;
    }

    private final transient ConcurrentHashMap<String, List<Advice>> adviceCacheMap = new ConcurrentHashMap<>();


    public Object getProxy() {

        return Enhancer.create(superClass, implementedInterfaces, new AopProxy.ChainDefinitionMethodInterceptor(this));
    }

    public void addAdvice(Advice advice) {
        this.adviceList.add(advice);
    }


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
