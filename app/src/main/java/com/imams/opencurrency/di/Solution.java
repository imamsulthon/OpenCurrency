package com.imams.opencurrency.di;

import java.util.ArrayList;
import java.util.List;

public class Solution {

    public List<String> solution(String str){
        List<String> arr = new ArrayList<>();
        if (str == null || str.length() == 0) return arr;
        for (int i = 0; i < str.length(); i++) {
            arr.set(i, String.valueOf(str.charAt(i)));
        }
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).contains("-")) {
                arr.get(i).replace("-", ".");
            }
        }
        return arr;
    }

}
