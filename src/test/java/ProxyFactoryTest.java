import com.whut.aop.advice.ExposeInvocationMethodInterceptor;
import com.whut.aop.advice.MethodInterceptor;
import com.whut.aop.advisor.DefaultAdvisor;
import com.whut.aop.chain.MethodInvocation;
import com.whut.aop.core.ProxyFactory;
import demo.Target;

import java.util.Collections;

/**
 * @author whut2024
 * @since 2024-09-17
 */
public class ProxyFactoryTest {
    public static void main(String[] args) {

        final Target target = new Target();

        final MethodInterceptor methodInterceptor = new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation methodInvocation) throws Throwable {
                System.out.println("before");
                final Object result = methodInvocation.proceed();
                System.out.println("after");

                return result;
            }
        };
        final DefaultAdvisor defaultAdvisor = new DefaultAdvisor(methodInterceptor);

        final ProxyFactory proxyFactory = new ProxyFactory(target, new Class[0], Collections.singletonList(defaultAdvisor), Target.class);
        proxyFactory.addAdvice(ExposeInvocationMethodInterceptor.INSTANCE);

        final Target proxy = (Target) proxyFactory.getProxy();
        System.out.println(proxy.getClass());

        System.out.println(proxy.m1(new Object()));
    }
}
