import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author liuqiao
 * @since 2024-10-02
 */
public class InvokeSuperTest {

    protected static class Father {

        void m1() {
            System.out.println("m1");
            m2();
        }

        void m2() {
            System.out.println("m2");
        }
    }

    protected static class Son extends Father {

        @Override
        void m1() {
            System.out.println("before");
            super.m1();
            System.out.println("after");
        }

        @Override
        void m2() {
            System.out.println("before");
            super.m2();
            System.out.println("after");
        }
    }


    public static void main(String[] args) throws Exception {
        /*Father proxy = (Father) Enhancer.create(Father.class, null, new MethodInterceptor() {
            @Override
            public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                System.out.println("before");

                final Object result = methodProxy.invokeSuper(proxy, args);

                System.out.println("after");

                return result;
            }
        });

        proxy.m1();*/

        final Son son = new Son();

        son.m1();
    }
}
