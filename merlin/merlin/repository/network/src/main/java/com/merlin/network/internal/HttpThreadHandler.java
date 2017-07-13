package com.merlin.network.internal;

import com.merlin.network.controller.IThreadHandler;
import com.merlin.network.internal.dipatcher.CacheDispatcher;
import com.merlin.network.internal.dipatcher.NetworkDispatcher;

/**
 * User: Simon
 * Date: 2016/1/19
 * Desc: 线程管理器
 */
public class HttpThreadHandler implements IThreadHandler {

    private RestTemplate restTemplate;

    private CacheDispatcher mCacheDispatcher;

    private final static int NETWORK_POOL_SIZE = 12;

    private NetworkDispatcher[] mNetworkDispatchers = new NetworkDispatcher[NETWORK_POOL_SIZE];

    @Override
    public void start() {
        stop();
        mCacheDispatcher = new CacheDispatcher(restTemplate.getCacheController(), restTemplate.getMessageQueueController(), restTemplate.getDeliveryController());
        mCacheDispatcher.start();

        for (int i = 0; i < mNetworkDispatchers.length; i++) {
            NetworkDispatcher networkDispatcher = new NetworkDispatcher(restTemplate.getDeliveryController(), restTemplate.getMessageQueueController(), restTemplate.getNetWork());
            mNetworkDispatchers[i] = networkDispatcher;
            networkDispatcher.start();
        }

    }

    @Override
    public void stop() {
        for (NetworkDispatcher mNetworkDispatcher : mNetworkDispatchers) {
            if (mNetworkDispatcher != null) {
                mNetworkDispatcher.quit();
            }
        }
        if(mCacheDispatcher != null) {
            mCacheDispatcher.quit();
        }
    }

    @Override
    public void addRestTemplate(RestTemplate restTemplate){
        if(restTemplate == null){
            throw new NullPointerException("restTemplate must not be null");
        }
        this.restTemplate = restTemplate;
    }
}
