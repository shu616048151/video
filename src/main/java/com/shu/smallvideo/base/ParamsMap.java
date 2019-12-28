package com.shu.smallvideo.base;

/**
 * mybatis查询参数封装
 * 用了多态,更灵活一点
 */

import java.util.HashMap;

public class ParamsMap extends HashMap<String, Object> {
    private ParamsMap() {
        super();
    }

    public static ParamsMap getPageInstance(int current, int size) {
        ParamsMap paramsMap = new ParamsMap();
        paramsMap.put("firstIndex", size*(current-1));
        paramsMap.put("size", size);
        return paramsMap;
    }

    public static ParamsMap getPageInstance(int current, int size, String orderBy, Boolean asc) {
        ParamsMap paramsMap = getPageInstance(current, size);
        if(orderBy != null && !orderBy.isEmpty()){
            if(asc == null){
                asc = false;
            }
            paramsMap.put("order", orderBy+" "+(asc?"asc":"desc"));
        }
        return paramsMap;
    }

    public static ParamsMap getPageInstance() {
        return getPageInstance(1, 5);
    }

}
