package com.wtbu.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class R<T> {
    private Integer code;
    private String msg;
    private T data;
    private Map hashMap=new HashMap();

    public static <T>R<T> success(T object){
        R<T> r = new R<>();
        r.code=1;
        r.data=object;
        return r;
    }

    public static <T>R<T> error(String msg){
        R<T> r = new R<>();
        r.code=0;
        r.msg=msg;
        return r;
    }

    public R<T> add(String key,Object value){
        this.hashMap.put(key,value);
        return this;
    }
}
