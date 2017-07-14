package com.merlin.network.internal.dipatcher;


import com.merlin.network.beans.NetworkResponse;
import com.merlin.network.controller.IDeliveryController;
import com.merlin.network.controller.INetWork;
import com.merlin.network.controller.IRequestQueueController;
import com.merlin.network.internal.Request;


/**
 * User: Simon
 * Date: 2016/1/19
 * Desc: 网络请求分配器
 */
public class NetworkDispatcher extends Thread {

    private final IRequestQueueController messageQueueController;

    private final IDeliveryController deliveryController;

    private final INetWork httpStack;

    private volatile boolean mQuit = false;


    public NetworkDispatcher(IDeliveryController deliveryController, IRequestQueueController
            messageQueueController, INetWork httpStack) {
        this.messageQueueController = messageQueueController;
        this.deliveryController = deliveryController;
        this.httpStack = httpStack;
    }

    public void quit() {
        mQuit = true;
        interrupt();
    }


    @Override
    public void run() {


        Request request;
        while (true) {
            try {
                request = messageQueueController.takeNetworkMessage();

            } catch (InterruptedException e) {
                if (mQuit) {
                    return;
                }
                continue;
            }
            try {
                if (request.isCanceled()) {
                    request.finish();
                    continue;
                }
                request = resetRequest(request);
                NetworkResponse networkResponse = httpStack.performRequest(request);

                deliveryController.postResponse(request, resetResponse(networkResponse));
            } catch (Exception e) {
                deliveryController.postError(request, e);
            }
        }
    }

    private Request resetRequest(Request request) {
        return request;
    }

    private NetworkResponse resetResponse(NetworkResponse networkResponse){
        return networkResponse;
    }



}