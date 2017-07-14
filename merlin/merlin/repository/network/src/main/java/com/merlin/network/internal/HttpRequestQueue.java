package com.merlin.network.internal;


import com.merlin.network.controller.IRequestQueueController;
import com.merlin.network.internal.utils.NestLog;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: Simon
 * Date: 2016/1/19
 * Desc: 内部消息循环队列
 */
public class HttpRequestQueue implements IRequestQueueController {

    private RestTemplate restTemplate;

    private final static BlockingQueue<Request> cacheQueue = new LinkedBlockingQueue<>();

    private final static BlockingQueue<Request> networkQueue = new LinkedBlockingQueue<>();

    /**
     * 记录表格
     */
    private final static HashMap<String, HashMap<Object, Request>> collectRequests = new HashMap<>();

    private AtomicInteger mSequenceGenerator = new AtomicInteger();

    public HttpRequestQueue(){

    }


    @Override
    public Request takeCacheMessage() throws InterruptedException {
        return cacheQueue.take();
    }

    @Override
    public Request takeNetworkMessage() throws InterruptedException {
        return networkQueue.take();
    }

    @Override
    public void putCacheMessage(Request request) throws InterruptedException {
        if(request != null){
            cacheQueue.put(request);
        }

    }

    @Override
    public void putNetworkMessage(Request request) throws InterruptedException {
        if(request != null){
            networkQueue.put(request);
        }

    }

    @Override
    public void addRestTemplate(RestTemplate restTemplate){
        if(restTemplate == null){
            throw new NullPointerException("restTemplate must not be null");
        }
        this.restTemplate = restTemplate;
    }


    public Request add(Request request) {


        request.setRequestQueue(this);

        request.setSequence(getSequenceNumber());

        String threeVerTag = request.getThreeVerTag();
        NestLog.e("three ver tag : %s",  threeVerTag);
        NestLog.e("request seq : %s",  request.getSequence());

        if(collectRequests.containsKey(threeVerTag)){
            HashMap<Object, Request> objectRequestHashMap = collectRequests.get(threeVerTag);
            objectRequestHashMap.put(request.getTrafficStatsTag(), request);

        }else{
            HashMap<Object, Request> objectRequestHashMap = new HashMap<>();
            objectRequestHashMap.put(request.getTrafficStatsTag(), request);
            collectRequests.put(threeVerTag, objectRequestHashMap);
            try {
                putCacheMessage(request);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


        return request;
    }

    @Override
    public void finish(Request request) {

        if(request != null){

            String threeVerTag = request.getThreeVerTag();
            if(collectRequests.containsKey(threeVerTag)){ // 清空记录列表中的
                HashMap<Object, Request> objectRequestHashMap = collectRequests.get(threeVerTag);
                for(Map.Entry<Object, Request> entry : objectRequestHashMap.entrySet()){
                    Request value = entry.getValue();
                    if(!value.isCanceled())
                        value.cancel();
                }
                collectRequests.remove(threeVerTag);
            }

            request.cancel();
        }



    }

    public int getSequenceNumber() {
        return mSequenceGenerator.incrementAndGet();
    }


    public void cancelAll(){

        for(Map.Entry<String, HashMap<Object, Request>> entry : collectRequests.entrySet()){ // 全部取消
            HashMap<Object, Request> waitRequest = entry.getValue();
            for(Map.Entry<Object, Request> waitEntry : waitRequest.entrySet()){
                Request value = waitEntry.getValue();
                if(!value.isCanceled())
                    value.cancel();
            }
        }

        cacheQueue.clear();
        networkQueue.clear();
    }

    public void cancel(Object tag){
        if(tag != null){
            for(Map.Entry<String, HashMap<Object, Request>> entry : collectRequests.entrySet()){ // 全部取消
                HashMap<Object, Request> waitRequest = entry.getValue();
                if(waitRequest.containsKey(tag)){
                    Request request = waitRequest.get(tag);
                    request.cancel();
                }
            }
        }

    }
}
