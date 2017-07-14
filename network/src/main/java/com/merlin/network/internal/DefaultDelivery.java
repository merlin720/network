package com.merlin.network.internal;

import android.os.Handler;

import com.merlin.network.RestErrorHandler;
import com.merlin.network.beans.NetworkResponse;
import com.merlin.network.beans.Response;
import com.merlin.network.controller.IDeliveryController;
import com.merlin.network.internal.exception.RestClientException;

import java.util.concurrent.Executor;

/**
 * User: Simon
 * Date: 2016/1/19
 * Desc: 默认的消息分发器
 */
public class DefaultDelivery implements IDeliveryController {

    private RestTemplate restTemplate;

    private final Executor mResponsePoster;

    private final RestErrorHandler restErrorHandler = new RestErrorHandler();

    public DefaultDelivery(final Handler handler) {
        mResponsePoster = new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
    }


    @Override
    public void postResponse(Request request, NetworkResponse networkResponse) {
        request.markDelivered();

        // 解析请求结果
        Response response = request.parse(networkResponse);
        if(response == null){
            postError(request, new RestClientException());
        }else {
            mResponsePoster.execute(new ResponseDeliveryRunnable(request, response, null));
        }


    }

    @Override
    public void postError(Request request, Exception error) {
        mResponsePoster.execute(new ResponseDeliveryRunnable(request, null, error));
    }

    @Override
    public void addRestTemplate(RestTemplate restTemplate) {
        if (restTemplate == null) {
            throw new NullPointerException("restTemplate must not be null");
        }
        this.restTemplate = restTemplate;

    }

    private class ResponseDeliveryRunnable implements Runnable {
        private final Request mRequest;
        private final Exception mException;
        private final Response mResponse;

        public ResponseDeliveryRunnable(Request request, Response response, Exception exception) {
            mRequest = request;
            mException = exception;
            mResponse = response;
        }


        @Override
        @SuppressWarnings("unchecked")
        public void run() {
            if(mRequest == null)
                return;
            if (mRequest.isCanceled()) {
                mRequest.finish();
                return;
            }
            if (mException == null) {
                mRequest.deliverResponse(mResponse.getResponseBody());
            } else {
                mRequest.deliverError(mException);
                restErrorHandler.onRestClientExceptionThrown(mException);

            }
            mRequest.finish();

        }
    }
}
