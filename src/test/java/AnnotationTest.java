import com.whut.aop.advice.annotation.*;
import com.whut.aop.advisor.DefaultAdvisor;

import java.util.List;

/**
 * @author whut2024
 * @since 2024-09-17
 */
public class AnnotationTest {

    public final static class AnnotationAdvisor {


        @Before("execution(* m1(Object))")
        public void before() {
            System.out.println("before");
        }

        @Around("execution(* m1(Object))")
        public void around() {
            System.out.println("around");
        }

        @After("execution(* m1(Object))")
        public void after() {
            System.out.println("after");
        }

        @AfterReturning("execution(* m1(Object))")
        public void afterReturning() {
            System.out.println("after returning");
        }

        @AfterThrowing("execution(* m1(Object))")
        public void afterThrowing() {
            System.out.println("after throwing");
        }


    }

    public static void main(String[] args) {
        final List<DefaultAdvisor> advisorList = AdviceAnnotationUtil.resolveAdvisorFromClass(AnnotationAdvisor.class);
        System.out.println(advisorList);
        advisorList.forEach(System.out::println);
    }
}
