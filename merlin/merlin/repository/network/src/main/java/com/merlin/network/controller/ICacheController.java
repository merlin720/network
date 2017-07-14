package com.merlin.network.controller;

import java.util.Collections;
import java.util.Map;

/**
 * User: Simon
 * Date: 2016/1/19
 * Desc: 缓存处理器接口
 */
public interface ICacheController {

    /**
     * 根据唯一键值获取缓存
     * @param key 钥匙
     * @return
     */
    Entry get(String key);

    /**
     * 添加或者更新缓存
     * @param key 缓存主键
     * @param entry 带有请求头的数据
     */
    void put(String key, Entry entry);

    /**
     * 初始化缓存模块
     */
    void initialize();

    /**
     * 根据缓存键值清除缓存
     * @param key 缓存主键
     */
    void remove(String key);

    /**
     * 清除所有缓存
     */
    void clear();

    /**
     * 缓存数据类型
     */
    class Entry {
        /** 缓存数据 */
        public byte[] data;


        /** 缓存数据的请求头 */
        public Map<String, String> responseHeaders = Collections.emptyMap();

        /** 缓存是否需要刷新 */
        public boolean refreshNeeded() {
            return true;
        }
    }
}
