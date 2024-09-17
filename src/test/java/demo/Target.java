package demo;

public class Target {


    public Object m1(Object object) {
        System.out.println("m1");
        m2(null);
        return object;
    }


    public Object m2(Object object) {
        System.out.println("m2");
        return object;
    }
}