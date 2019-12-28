package com.shu.smallvideo.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * com.whut.athena.util
 * Created by YTY on 2016/3/26.
 */
public class ResponseMap extends HashMap<String, Object> {
    private ResponseMap() {
        super();
    }

    public static ResponseMap getInstance() {
        return new ResponseMap();
    }

    public ResponseMap putFailure(String message, int code) {
        put("success", false);
        put("message", message);
        put("code", code);
        return this;
    }

    public ResponseMap putFailure(String message, String messageCn, int code) {
        put("success", false);
        put("message", message);
        put("messageCn", messageCn);
        put("code", code);
        return this;
    }

    public ResponseMap putSuccess() {
        return putSuccess(null);
    }

    public ResponseMap putSuccess(String message) {
        put("success", true);
        put("message", message);
        put("code", 0);
        return this;
    }

    public ResponseMap putValue(Object value) {
        return putValue(value, null);
    }

    public ResponseMap putValue(Object value, String message) {
        put("value", value);
        put("success", true);
        put("message", message);
        put("code", 0);
        return this;
    }

    public ResponseMap putList(List list) {
        return putList(list, null);
    }

    public ResponseMap putList(List list, String message) {
        put("list", list);
        put("success", true);
        put("message", message);
        put("code", 0);
        return this;
    }

    public ResponseMap putListAndValue(List list, Object value, String message) {
        put("list", list);
        put("value", value);
        put("success", true);
        put("code", 0);
        return this;
    }

    public Map putPage(Page page) {
        return putPage(page, null);
    }

    public Map putPage(Page page, String message) {
        put("page", page);
        put("message", message);
        put("success", true);
        put("code", 0);
        return this;
    }
}
