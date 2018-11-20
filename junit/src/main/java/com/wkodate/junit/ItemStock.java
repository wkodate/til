package com.wkodate.junit;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wkodate on 2018/11/19.
 */
public class ItemStock {

    private final Map<String, Integer> values = new HashMap<>();

    public void add(Item item) {
        Integer num = values.get(item.name);
        if (num == null) num = 0;
        num++;
        values.put(item.name, num);
    }

    public int getNum(Item item) {
        Integer num = values.get(item.name);
        return num != null ? num : 0;
    }

}
