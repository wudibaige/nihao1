package com.nihao.common;

public class BaseContext {
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();
    //设置值
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
