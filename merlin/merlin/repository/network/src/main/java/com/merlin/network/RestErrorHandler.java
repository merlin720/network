package com.merlin.network;

import com.merlin.network.internal.exception.HttpMessageNotReadableException;
import com.merlin.network.internal.exception.HttpMessageUnspectException;
import com.merlin.network.internal.exception.HttpStatusException;
import com.merlin.network.internal.utils.Config;

import java.net.SocketTimeoutException;


/**
 * UserBean: Simon
 * Date: 2015-11-20
 * Desc: 网络解析错误公共处理器
 */
public class RestErrorHandler {

    public RestErrorHandler() {

    }


    public void onRestClientExceptionThrown(Exception e) {

        if (e != null) {

            if(e instanceof HttpMessageUnspectException){
                return;
            }
            if (Config.isDebuggable()) {
//                if (Locale.getDefault().equals(Locale.CHINESE) || Locale.getDefault().equals(Locale.CHINA))
                    if (e instanceof HttpStatusException) {
                        HttpStatusException errorException = (HttpStatusException) e;
                        if (errorException.getStatusCode() == HttpStatus.SC_BAD_REQUEST) {
                            showShortToast("请求参数错误");
                        } else if (errorException.getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                            showShortToast("请求需要授权");
                        } else if (errorException.getStatusCode() == HttpStatus.SC_FORBIDDEN) {
                            showShortToast("请求被服务器拒绝");
                        } else if (errorException.getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                            showShortToast("请求资源不存在");
                        } else if (errorException.getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                            showShortToast("服务器错误");
                        } else if (errorException.getStatusCode() == HttpStatus.SC_BAD_GATEWAY) {
                            showShortToast("服务器无响应");
                        } else {
                            showShortToast("未知的网络请求错误");
                        }
                    } else if (e instanceof HttpMessageNotReadableException) {
                        showShortToast("请求结果解析失败");
                    } else if(e instanceof SocketTimeoutException){
                        showShortToast("请求超时");
                    } else {
                        showShortToast("连接异常，请检查网络");
                    }
            } else {
                if(e instanceof SocketTimeoutException){
                    showShortToast("请求超时");
                }else {
                    showShortToast("连接异常，请检查网络");
                }

            }
        }

    }

    public static void showShortToast(final String content) {

//        ToastUtils.show(content);

    }

}
