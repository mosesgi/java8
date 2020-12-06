package functional;

import java.util.Optional;

public class OptionalUse {
    public static void test(){
        Optional<String> s = Optional.of("abc");
        s.ifPresent(System.out::println);

        Optional<String> s1 = Optional.empty();
        System.out.println(s1.orElse("default"));

    }

    public static void main(String[] args) {
        OptionalUse.test();
    }
}
