import com.whut.aop.pointcut.ExpressionPointcut;
import demo.Target;

/**
 * @author whut2024
 * @since 2024-09-17
 */
public class PointcutTest {
    public static void main(String[] args) throws Exception {
        final ExpressionPointcut pointcut = new ExpressionPointcut("test");

        System.out.println(pointcut.matches(Target.class.getMethod("m1", Object.class), Target.class));
    }
}
