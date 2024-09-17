import com.whut.aop.core.ProxyFactory;
import demo.Target;

/**
 * @author whut2024
 * @since 2024-09-17
 */
public class FullTest {

    public static void main(String[] args) {
        final Target target = new Target();

        final ProxyFactory proxyFactory = new ProxyFactory(target, null, AnnotationTest.AnnotationAdvisor.class, Target.class);

        Target proxy = (Target) proxyFactory.getProxy();

        System.out.println(proxy.m1(new Object()));
    }
}
