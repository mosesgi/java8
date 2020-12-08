package functional;

import pojo.Person;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//https://blog.csdn.net/fzy629442466/article/details/84629422
public class StreamSamples {

    public static void union(){
        List<Integer> l1 = Arrays.asList(1,2,3,4);
        List<Integer> l2 = Arrays.asList(3,4,5,6);

        //交集
        List<Integer> intersect = l1.stream().filter(l2::contains).collect(Collectors.toList());
        System.out.println("combined: " + intersect);

        //差集
        List<Integer> reduce1 = l1.stream().filter(ele -> !l2.contains(ele)).collect(Collectors.toList());
        System.out.println("reduce from L1: " + reduce1);

        //并集
        List<Integer> listAll = l1.parallelStream().collect(Collectors.toList());
        listAll.addAll(l2.parallelStream().collect(Collectors.toList()));
        System.out.println("parallel union: " + listAll);

        //去重并集
        List<Integer> distinctList = listAll.stream().distinct().collect(Collectors.toList());
        System.out.println("distinct all:" + distinctList);
    }

    public static void filter(){
        List<Person> list = new ArrayList<>();
        Person match = list.stream().filter((p) -> p.getId()==1).findAny().get();
    }

    public static void mapToList(){
        Map<Integer, String> map = new HashMap<>();
        List<Person> list = map.entrySet().stream().sorted(Comparator.comparing(e -> e.getKey()))
                .map(e -> new Person(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    public static void listToMap(){
        List<Person> list = new ArrayList<>();
        Map<Integer, Person> personMap = list.stream().collect(Collectors.toMap(Person::getId, p -> p));
        personMap.forEach((k, v) -> System.out.println("k: " + k + ", v: " + v));

        Map<Integer, Person> personMap1 = list.stream().collect(HashMap::new, (map, p) -> map.put(p.getId(), p), Map::putAll);

        list.stream().collect(Collectors.toMap(Person::getId, p -> p, (p1, p2) -> p1));     //3rd param - mergeFunction. key conflict scenario.
    }

    public static void listToGroupedMap(){
        List<Person> list = new ArrayList<>();
        Map<Integer, List<Person>> groupedMap = list.stream().collect(Collectors.toMap(Person::getAge, Collections::singletonList, (a, b) -> {
            List<Person> combined = new ArrayList<>(a);
            combined.addAll(b);
            return combined;
        }));

        Map<Integer, List<Person>> groupedMap1 = list.stream().collect(Collectors.groupingBy(Person::getAge));
        //根据年龄分组，年龄对应的List存储的是相应person的name
        Map<Integer, List<String>> groupedNamesMap = list.stream().collect(Collectors.groupingBy(p -> p.getAge(), Collectors.mapping(p -> p.getName(), Collectors.toList())));
        groupedNamesMap.forEach((k, v) -> System.out.printf("age %s: %s\n", k, v));

        //groupingBy 分组后操作
        //Collectors中还提供了一些对分组后的元素进行downStream处理的方法：
        //counting方法返回所收集元素的总数；
        Map<Integer, Long> sexCount = list.stream().collect(Collectors.groupingBy(Person::getSex, Collectors.counting()));
        //summing方法会对元素求和；
        Map<Integer, Integer> ageCount = list.stream().collect(Collectors.groupingBy(Person::getSex, Collectors.summingInt(Person::getAge)));
        //maxBy和minBy会接受一个比较器，求最大值，最小值；
        Map<Integer, Optional<Person>> ageMax =  list.stream().collect(Collectors.groupingBy(Person::getSex, Collectors.maxBy(Comparator.comparing(Person::getAge))));
        //mapping函数会应用到downstream结果上，并需要和其他函数配合使用；
        Map<Integer, List<String>> nameMap =  list.stream().collect(Collectors.groupingBy(Person::getSex, Collectors.mapping(Person::getName,Collectors.toList())));

        //相同姓名为key，统计这些人的平均年龄 groupingBy + averagingInt
        Map<String, Double> avgAgeByName = list.stream().collect(Collectors.groupingBy(p -> p.getName(), Collectors.averagingInt(p -> p.getAge())));
//        Collectors.summingInt(Person::getAge);        //求和

        //根据年龄分成两组 partitioningBy
        Map<Boolean, List<Person>> age30Map = list.stream().collect(Collectors.partitioningBy(p -> p.getAge()<=30));

        //奇偶分组统计数量
        Map<Boolean, Long> partiCount = Stream.of(1,2,3,4).collect(Collectors.partitioningBy(it -> it%2==0, Collectors.counting()));
    }

    //Collectors joining Strings
    public static void collectorJoin(){
        List<Person> list = new ArrayList<>();
        String names = list.stream().map(p -> p.getName()).collect(Collectors.joining(","));
        String strJoin = Stream.of("1","2","3","4").collect(Collectors.joining(",","[","]"));
        System.out.println("strJoin: " + strJoin);

        String phrase = list.stream().filter(p -> p.getAge() >= 18).map(p -> p.getName()).collect(Collectors.joining(" and ", "In Germany ", " are of legal age."));
        System.out.println(phrase);
    }

    public static void summaryStat(){
        List<Person> list = new ArrayList<>();
        DoubleSummaryStatistics dss = list.stream().collect(Collectors.summarizingDouble(p -> p.getAge()));
        double average = dss.getAverage();
        double max = dss.getMax();
        double min = dss.getMin();
        double sum = dss.getSum();
        double count = dss.getCount();

        IntSummaryStatistics ageSummary = list.stream().collect(Collectors.summarizingInt(p -> p.getAge()));
        System.out.println(ageSummary);
    }

    //自定义Collector
    public static void customizedCollector(){
        List<Person> list = new ArrayList<>();
        Collector<Person, StringJoiner, String> personNameCollector = Collector.of(
                () -> new StringJoiner(" | "),          //supplier
                (j, p) -> j.add(p.getName().toUpperCase()),       //accumulator
                (j1, j2) -> j1.merge(j2),                         //combiner
                StringJoiner::toString                            //finisher
        );
        String names = list.stream().collect(personNameCollector);
        System.out.println(names);  // MAX | PETER | PAMELA | DAVID
    }

    public static void main(String[] args) {
        StreamSamples.union();
    }

}
