package com.example.permission.utils;

import java.util.HashMap;
import java.util.Objects;

/**
 * 提供线程本地存储工具的类。
 * 该类允许在同一个线程内存储和访问共享但不互相干扰的数据。
 */
public class ThreadLocalUtil {

    // 静态的ThreadLocal变量，用于存储线程本地的HashMap
    private static final ThreadLocal<HashMap<String, Object>> threadLocal = new ThreadLocal<>();

    /**
     * 将键值对存储到当前线程的本地存储中。
     * 如果当前线程本地存储尚未初始化，则首先进行初始化。
     *
     * @param key   要存储的数据的键。
     * @param object   要存储的数据的值。
     */
    public static void set(String key,Object object) {
        // 检查当前线程的本地存储是否已初始化，如未初始化则先初始化
        if(Objects.isNull(threadLocal.get())){
            set();
        }
        HashMap<String,Object> threadLocalMap = threadLocal.get();
        threadLocalMap.put(key,object);
        threadLocal.set(threadLocalMap); // 更新线程本地存储
    }

    /**
     * 初始化当前线程的本地存储为一个新的空HashMap。
     */
    public static void set() {
        threadLocal.set(new HashMap<>()); // 初始化线程本地存储
    }

    /**
     * 获取当前线程本地存储的所有键值对。
     *
     * @return 当前线程本地存储的HashMap。
     */
    public static HashMap<String,Object> get() {
        return threadLocal.get(); // 获取线程本地存储
    }

    /**
     * 根据键从当前线程本地存储中获取值。
     *
     * @param key 要获取数据的键。
     * @return 与给定键关联的值，如果不存在则返回null。
     */
    public static Object get(String key){
        if(Objects.isNull(threadLocal.get())){
            return null; // 如果线程本地存储未初始化，返回null
        }
        return threadLocal.get().get(key); // 获取指定键的值
    }
}
