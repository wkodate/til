package com.wkodate.junit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wkodate on 2018/11/21.
 */
public class FuzzBuzz {

    public static List<String> createFizzBuzzList(int size) {
        List<String> list = new ArrayList<>(size);
        for (int i = 1; i <= size; i++) {
            if (i % 15 == 0) {
                list.add("FizzBuzz");
            } else if (i % 3 == 0) {
                list.add("Fizz");
            } else if (i % 5 == 0) {
                list.add("Buzz");
            } else {
                list.add(Integer.toString(i));
            }
        }
        return list;
    }

}
