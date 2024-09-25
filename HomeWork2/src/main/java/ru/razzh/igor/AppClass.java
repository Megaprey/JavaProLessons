package ru.razzh.igor;

import java.util.*;

import ru.razzh.igor.pojo.Worker;

import java.util.stream.Collectors;

public class AppClass {
    public static void main(String[] args) {
        //1
        System.out.println("task №1 : " + deleteDuplicates(Arrays.asList(1, 2, 1, 1, 1, 1, 2)));
        //2
        System.out.println("task №2 : " + thirdBiggestNumber(Arrays.asList(5, 2, 9, 10, 4, 3, 10, 1, 13)));
        //3
        System.out.println("task №3 : " + thirdBiggestUniqueNumber(Arrays.asList(5, 2, 9, 4, 10, 3, 10, 1, 13)));
        List<Worker> workers = Arrays.asList(new Worker("Igor", 32, "Инженер"), new Worker("Sergey", 20, "Менеджер"),
                new Worker("Petr", 65, "Инженер"), new Worker("Oleg", 19, "Инженер"),
                new Worker("Andrey", 32, "HR-меннеджер"),new Worker("Ivan", 40, "Инженер"));
        //4
        System.out.println("task №4 : " + threeOldestEngineer(workers));
        //5
        System.out.println("task №5 : " + averageAgeEngineers(workers));
        //6
        List<String> wordList = Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" );
        System.out.println("task №6 : " + longestWord(wordList));
        //7
        String words = "mouse wolf mouse wolf mouse cat mouse cat mouse cat mouse mouse wolf";
        System.out.println("task №7 : " + getMapWithCountWordsInString(words));
        //8
        List<String> wList = Arrays.asList("mouse", "cat", "dog", "car", "dark", "white", "dart", "umbrella", "waffle");
        System.out.println("task №8 : " + sortForLength(wList));
        //9
        String[] strArray = new String[]{"mouse cat dog car dark", "white dart umbrella waffle moon",
                "milk bread salt chocolate pasta", "red blue braun yellow green", "peek strong bathroom kitchen window"};
        System.out.println("task №9 : " + getLongestWordFromStringArray(strArray));
    }

    //1
    public static List<Integer> deleteDuplicates(List<Integer> list) {
        return list.stream().distinct().toList();
    }

    //2
    public static Integer thirdBiggestNumber(List<Integer> list) {
        return list.stream().sorted(Collections.reverseOrder()).toList().get(2);
    }

    //3
    public static Integer thirdBiggestUniqueNumber(List<Integer> list) {
        return list.stream().distinct().sorted(Collections.reverseOrder()).toList().get(2);
    }

    //4
    public static List<Worker> threeOldestEngineer(List<Worker> list) {
        return list.stream().filter(worker -> worker.getPosition().equals("Инженер"))
                .sorted(Comparator.comparing(Worker::getAge).reversed()).limit(3).toList();
    }

    //5
    public static Integer averageAgeEngineers(List<Worker> list) {
        return list.stream().map(Worker::getAge).collect(Collectors.averagingInt(Integer::intValue)).intValue();
    }

    //6
    public static String longestWord(List<String> list) {
        return list.stream().sorted(Comparator.comparing(String::length).reversed()).findFirst().orElse("");
    }

    //7
    public static Map<String, Integer> getMapWithCountWordsInString(String s) {
        return Arrays.stream(s.split("\\s+"))
                .collect(HashMap::new, (map, word) -> map.put(word, map.getOrDefault(word, 0) + 1), HashMap::putAll);
    }

    //8
    public static List<String> sortForLength(List<String> stringList) {
        return stringList.stream().sorted(Comparator.comparingInt(String::length)
                .thenComparing(Comparator.naturalOrder())).toList();
    }

    //9
    public static String getLongestWordFromStringArray(String[] stringArray) {
        return Arrays.stream(stringArray).flatMap(str -> Arrays.stream(str.split("\\s+")))
                .max(Comparator.comparingInt(String::length)).orElse("");
    }
}
