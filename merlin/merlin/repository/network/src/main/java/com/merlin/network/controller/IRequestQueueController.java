package com.merlin.network.controller;


import com.merlin.network.internal.Request;
import com.merlin.network.internal.RestTemplate;

/**
 * User: Simon
 * Date: 2016/1/19
 * Desc: 消息队列处理器
 */
public interface IRequestQueueController {

    Request takeCacheMessage() throws InterruptedException;

    Request takeNetworkMessage() throws InterruptedException;

    void putCacheMessage(Request request) throws InterruptedException;

    void putNetworkMessage(Request request) throws InterruptedException;

    void addRestTemplate(RestTemplate restTemplate);

    Request add(Request request);

    void finish(Request request);

    void cancelAll();

    /**
     * 根据标签进行取消
     * @param tag 标签
     */
    void cancel(Object tag);

}
