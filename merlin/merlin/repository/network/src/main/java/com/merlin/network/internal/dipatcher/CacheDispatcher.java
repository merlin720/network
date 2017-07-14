package com.merlin.network.internal.dipatcher;


import android.os.Process;


import com.merlin.network.beans.NetworkResponse;
import com.merlin.network.controller.ICacheController;
import com.merlin.network.controller.IDeliveryController;
import com.merlin.network.controller.IRequestQueueController;
import com.merlin.network.internal.Request;
import com.merlin.network.internal.utils.NestLog;

/**
 * User: Simon
 * Date: 2016/1/19
 * Desc: 缓存处理分配器
 */
public class CacheDispatcher extends Thread {


    private final ICacheController cacheController;

    private final IRequestQueueController messageQueueController;

    private final IDeliveryController deliveryController;

    private volatile boolean mQuit = false;

    public CacheDispatcher(ICacheController cacheController, IRequestQueueController messageQueueController, IDeliveryController deliveryController) {

        this.cacheController = cacheController;
        this.messageQueueController = messageQueueController;
        this.deliveryController = deliveryController;
    }


    public void run() {


        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        cacheController.initialize();

        Request request;

        while (true){
            try {
                request = messageQueueController.takeCacheMessage();

                NestLog.e("" + request);
            } catch (InterruptedException e) {
                if (mQuit) {
                    return;
                }
                continue;
            }

            try {
                request.addMarker("cache-queue-take");

                if (request.isCanceled()) {
                    request.finish();
                    continue;
                }

                ICacheController.Entry entry = cacheController.get(request.getCacheKey());
                if (entry == null) {
                    request.addMarker("cache-miss");
                    messageQueueController.putNetworkMessage(request);
                    continue;
                }


                request.addMarker("cache-hit");

                NetworkResponse networkResponse = new NetworkResponse(entry);

                request.addMarker("cache-hit-refresh-needed");
                request.setCacheEntry(entry);


                deliveryController.postResponse(request, networkResponse);

                if(entry.refreshNeeded()){
                    messageQueueController.putNetworkMessage(request);
                }

            } catch (Exception e) {
                NestLog.e(e, "Unhandled exception %s", e.toString());
            }
        }
    }


    public void quit() {
        mQuit = true;
        interrupt();
    }

}
