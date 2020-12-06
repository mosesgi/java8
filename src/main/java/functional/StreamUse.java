package functional;

import pojo.Person;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamUse {
    public static void basic(){
        List<String> list = Arrays.asList("a", "b", "c", "d", "e", "f", "g");
        Stream<String> listStream = list.stream();
        listStream.forEach((a) -> {System.out.println("Thread: " + Thread.currentThread().getName() + ", Value: " + a);});
        Stream<String> parallelStream = list.parallelStream();
        parallelStream.forEach((a) -> {System.out.println("Thread: " + Thread.currentThread().getName() + ", Value: " + a);});

        Stream<String> s = Stream.of("test");
        Stream<String> s1 = Stream.of("a", "b", "c", "d");

        Stream.iterate(1, integer -> integer+1).limit(10).forEach(System.out::println);     //generate infinite stream

        Stream.generate(new Supplier<Double>() {
            @Override
            public Double get() {
                return Math.random();
            }
        }).limit(10).forEach(System.out::println);      // use Supplier to generate
    }

    public static void streamOp(){
        Stream<String> s = Stream.of("test", "t1", "t2", "teeeee", "aaaa");
        s.filter(n -> n.contains("t")).forEach(System.out::println);    //filter

        s = Stream.of("test", "t1", "t2", "teeeee", "aaaa");
        s.map(n -> n.concat(".txt")).forEach(System.out::println);      //map

        s = Stream.of("test", "t1", "t2", "teeeee", "aaaa");
        s.map(String::toUpperCase).forEach(System.out::println);      //map

        s = Stream.of("test", "t1", "t2", "teeeee", "aaaa");
        s.flatMap(n -> Stream.of(n.split( ""))).forEach(System.out::println);   //split to letter

        s = Stream.of("test", "t1", "t2", "teeeee");
        System.out.println(s.allMatch(n -> n.startsWith("t")));
    }

    public static void collect(){
        String[] arr = {"aa", "ccc", "sss"};
        System.out.println(Arrays.stream(arr).collect(Collectors.joining(",", "{", "}")));

        List<Person> list = Collections.emptyList();
        //group by age, count numbers. 以年龄分组，统计数量
        Map<Integer, Long> personGroups = list.stream().collect(Collectors.groupingBy(Person::getAge, Collectors.counting()));
    }

    /**
     * 参数supplier 是一个生成目标类型实例的方法，代表着目标容器是什么；
     * accumulator是将操作的目标数据填充到supplier 生成的目标类型实例中去的方法，代表着如何将元素添加到容器中；
     * 而combiner是将多个supplier 生成的实例整合到一起的方法，代表着规约操作，将多个结果合并。
     */
    public static void collectAdv(){
        List<Person> list = Collections.emptyList();
        Map<Integer, Person> personMap = list.stream().collect(HashMap::new, (map, p) -> map.put(p.getId(), p), Map::putAll);
    }

    public static void main(String[] args) {
        StreamUse.streamOp();
        StreamUse.collect();
    }

}
