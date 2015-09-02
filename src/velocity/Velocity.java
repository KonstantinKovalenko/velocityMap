package velocity;

import java.util.*;
import java.util.concurrent.*;
import static mytoys.Print.*;

class MyMap {

    public final int MAP_SIZE;

    public MyMap(int mapSize) {
        MAP_SIZE = mapSize;
    }

    public void fillMap(Map<Integer, Integer> map) {
        long timer = -System.currentTimeMillis();
        for (int i = 0; i < MAP_SIZE; i++) {
            map.put(i, (int) (Math.random() * MAP_SIZE));
        }
        timer += System.currentTimeMillis();
        println(map.getClass().getSimpleName() + ".fillMap() in " + timer + " ms");
    }

    public void clearRandomQuantity(int quantity, Map<Integer, Integer> map) {
        int workQuantity = quantity;
        long timer = -System.currentTimeMillis();
        while (workQuantity-- != 0) {
            boolean breaker = true;
            Integer mapRemove = map.remove((int) (Math.random() * MAP_SIZE));
            if (mapRemove == null) {
                while (breaker) {
                    mapRemove = map.remove((int) (Math.random() * MAP_SIZE));
                    if (mapRemove != null) {
                        breaker = false;
                    }
                }
            }
        }
        timer += System.currentTimeMillis();
        println(map.getClass().getSimpleName() + ".clearRandomQuantity(" + quantity + ") in " + timer + " ms");
    }

    public void getRandomElements(int numberOfElements, Map<Integer, Integer> map) {
        try {
            long timer = -System.currentTimeMillis();
            int workNumberOfElements = numberOfElements;
            String itsALongString = "";
            while (workNumberOfElements-- != 0) {
                itsALongString += map.get((int) (Math.random() * MAP_SIZE)).toString();
            }
            timer += System.currentTimeMillis();
            println(map.getClass().getSimpleName() + ".getRandomElements(" + numberOfElements + ") in " + timer + " ms");
        } catch (NullPointerException e) {
            System.err.println(Thread.currentThread().getName() + ".getRandomElements(" + numberOfElements + ") failed");
        }
    }
}

public class Velocity {

    private static Map<Integer, Integer> treeMap = new TreeMap<>();
    private static Map<Integer, Integer> concurrentSkipListMap = new ConcurrentSkipListMap<>();
    private static Map<Integer, Integer> hashMap = new HashMap<>();
    private static Map<Integer, Integer> linkedHashMap = new LinkedHashMap<>();
    private static Map<Integer, Integer> weakHashMap = new WeakHashMap<>();
    private static Map<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();
    private static Map<Integer, Integer> identityHashMap = new IdentityHashMap<>();
    private static List<Map<Integer, Integer>> mapList = new LinkedList<>();
    private static Thread t;

    private static void fillMapList(List<Map<Integer, Integer>> workList) {
        workList.add(treeMap);
        workList.add(concurrentSkipListMap);
        workList.add(hashMap);
        workList.add(linkedHashMap);
        //workList.add(weakHashMap);
        workList.add(concurrentHashMap);
        //workList.add(identityHashMap);
    }

    public static void main(String[] args) {
        MyMap myMap = new MyMap(100000);
        fillMapList(mapList);
        for (Map<Integer, Integer> map : mapList) {
            myMap.fillMap(map);
            println();
            t = new Thread(map.getClass().getSimpleName()) {
                @Override
                public void run() {
                    myMap.getRandomElements((myMap.MAP_SIZE / 2), map);
                    myMap.clearRandomQuantity((myMap.MAP_SIZE / 2), map);
                    println();
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException ignored) {

                    }
                }
            };
            t.start();
        }
    }
}
