package lab7._1_mapScheduler;

import java.util.TreeMap;

public class pr {
    public static void main(String[] args) {
        TreeMap<Integer, String> treeMap = new TreeMap<>();
        treeMap.put(1, "todor");
        treeMap.put(3, "mia");
        treeMap.put(2, "mario");
        treeMap.put(0, "matej");

        for(Integer integer : treeMap.keySet()) {
            System.out.println(treeMap.get(integer));
        }
    }
}
