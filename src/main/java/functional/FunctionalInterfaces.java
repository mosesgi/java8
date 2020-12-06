package functional;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class FunctionalInterfaces {
    //consumes only, no output
    public static void consumerTest() {
        Consumer<String> f = System.out::println;
        Consumer<String> f2 = n -> System.out.println(n + "-F2");
        f.andThen(f2).accept("test");
        f.andThen(f).andThen(f).andThen(f).accept("test1");
    }

    //one input and one output
    public static void functionTest() {
        //Input type, output Type
        Function<Integer, Integer> f = s -> ++s;
        Function<Integer, Integer> g = s -> s * 2;

        /*
          下面表示在执行F时，先执行G，并且执行F时使用G的输出当作输入。相当于以下代码：
          Integer a = g.apply(1);
          System.out.println(f.apply(a));
         */
        System.out.println(f.compose(g).apply(1));      //3

        /*
          表示执行F的Apply后使用其返回的值当作输入再执行G的Apply；相当于以下代码
          Integer a = f.apply(1);
          System.out.println(g.apply(a));
         */
        System.out.println(f.andThen(g).apply(1));      //4

        //identity方法会返回一个不进行任何处理的Function，即输出与输入值相等；
        System.out.println(Function.identity().apply("a"));
    }

    public static void predicateTest(){
        Predicate<String> p = o -> o.equals("test");
        Predicate<String> g = o -> o.startsWith("t");
        //negate: 用于对原来的Predicate做取反处理
        System.out.println(p.negate().test("test"));
        //and
        System.out.println(p.and(g).test("test"));
        //or
        System.out.println(p.or(g).test("ta"));
        System.out.println(Predicate.isEqual(123).test(123));
    }

    public static void main(String[] args) {
        FunctionalInterfaces.functionTest();
        FunctionalInterfaces.predicateTest();
    }
}
