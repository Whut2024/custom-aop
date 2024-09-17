import demo.Target;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author whut2024
 * @since 2024-09-17
 */
public class ProxyObjectDemo {


    public static void main(String[] args) {

        final Target target = new Target();

        final MethodInterceptor methodInterceptor = new MethodInterceptor() {
            @Override
            public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                System.out.println("before");
                final Object result = methodProxy.invoke(target, args);
                System.out.println("after");
                return result;
            }
        };

        final Target proxy = (Target) Enhancer.create(Target.class, methodInterceptor);

        System.out.println(proxy.m1(new Object()));
    }
}
