/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package velocity;

import java.util.*;
import java.util.concurrent.*;
import static mytoys.Print.*;

class MyMap{
    private Random rand = new Random();
    public final int MAP_SIZE;
    public MyMap(int mapSize){
        MAP_SIZE = mapSize;
    }
    public void fillMap(Map<Integer,Integer> map){
        long timer = -System.currentTimeMillis();
        for(int i = 0;i<MAP_SIZE;i++ )
            map.put(i, rand.nextInt(MAP_SIZE));
        timer += System.currentTimeMillis();
        println(map.getClass().getSimpleName() + ".fillMap() in "+timer+" ms");
    }
    public void clearHalfMap(int zeroOneSwitcher, Map<Integer,Integer> map){
        switch(zeroOneSwitcher){
            case 0:{
                long timer = -System.currentTimeMillis();
                for(int i = 0;i<MAP_SIZE;i++){
                    if(i%2==0)
                        map.remove(i);
                }
                timer += System.currentTimeMillis();
                println(map.getClass().getSimpleName() + ".clearHalfMap(even int) in "+timer+" ms");
                return;
            }
            case 1:{
                long timer = -System.currentTimeMillis();
                for(int i = 0;i<MAP_SIZE;i++){
                    if(i%2!=0)
                        map.remove(i);
                }
                timer += System.currentTimeMillis();
                println(map.getClass().getSimpleName() + ".clearHalfMap(odd int) in "+timer+" ms");
                return;
            }
        }
    }
    public void clearMap(Map<Integer,Integer> map){
        long timer = -System.currentTimeMillis();
        map.clear();
        timer += System.currentTimeMillis();
        println(map.getClass().getSimpleName() + ".clear() in "+timer+" ms");
    }
    public void getElementByKey(int element,Map<Integer,Integer> map){
        long timer = -System.currentTimeMillis();
        map.get(element);
        timer += System.currentTimeMillis();
        println(map.getClass().getSimpleName() + ".getElementByKey("+element+") in "+timer+" ms");
    }
    public void getAllElements(Map<Integer,Integer> map){
        try{
            long timer = -System.currentTimeMillis();
            String itsALongString = "";
            for(int i : map.keySet())
                itsALongString += map.get(i).toString();
            timer += System.currentTimeMillis();
            println(map.getClass().getSimpleName() + ".getAllElements() in "+timer+" ms");
        }catch(NullPointerException e){
            System.err.println(Thread.currentThread().getName()+".getAllElements() failed");
        }
    }
}
public class Velocity {
    private static Map<Integer,Integer> treeMap= new TreeMap<>();
    private static Map<Integer,Integer> enumMap= new ConcurrentSkipListMap<>();
    private static Map<Integer,Integer> hashMap= new HashMap<>();
    private static Map<Integer,Integer> linkedHashMap= new LinkedHashMap<>();
    private static Map<Integer,Integer> weakHashMap= new WeakHashMap<>();
    private static Map<Integer,Integer> concurrentHashMap= new ConcurrentHashMap<>();
    private static Map<Integer,Integer> identityHashMap= new IdentityHashMap<>();
    private static List<Map<Integer,Integer>> mapList = new LinkedList<>();
    private static Thread t;
    private static void fillMapList(){
        mapList.add(treeMap);
        mapList.add(enumMap);
        mapList.add(hashMap);
        mapList.add(linkedHashMap);
        mapList.add(weakHashMap);
        mapList.add(concurrentHashMap);
        mapList.add(identityHashMap);
        
    }
    
    public static void main(String[] args) {
        MyMap myMap = new MyMap(10000);
        fillMapList();
        for(Map<Integer,Integer> map : mapList){
            myMap.fillMap(map);
            myMap.getElementByKey(new Random().nextInt(myMap.MAP_SIZE),map);
            println();
            t = new Thread(map.getClass().getSimpleName()){
                public void run(){
                    try{
                        myMap.getAllElements(map);
                        myMap.clearHalfMap(new Random().nextInt(2),map);
                        myMap.clearMap(map);
                        println();
                        TimeUnit.SECONDS.sleep(1);
                    }catch(InterruptedException e){
                        System.err.println(e+", sleep() interrupted");
                    }
                }
            };t.start();
        }
    }
}
