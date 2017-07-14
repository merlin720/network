package com.merlin.network.internal;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.merlin.network.controller.ICacheController;
import com.merlin.network.controller.IDeliveryController;
import com.merlin.network.controller.INetWork;
import com.merlin.network.controller.IRequestQueueController;
import com.merlin.network.controller.IThreadHandler;
import com.merlin.network.ext.OkHttpStack;
import com.merlin.network.internal.support.HttpAccessor;
import com.merlin.network.internal.utils.Config;
import com.merlin.network.internal.utils.NestLog;


/**
 * User: Simon
 * Date: 2016/1/18
 * Desc: 网络组装器
 * 1.提供网络请求restful模式
 * 2.提供文件的上传下载
 * 3.提供传输信息加密
 * 4.提供扩展插件
 * 5.提供过滤器封装
 *
 *
 * 组装器
 * |
 * | --------- 线程管理器
 * |
 * | --------- 消息分发器
 * |
 * | --------- 缓存对外接口
 * |
 * | --------- 网络请求扩展接口
 * |
 * | --------- 请求预处理器
 * |
 * | --------- 响应解析器
 * |
 * | --------- 日志管理
 * |
 * | --------- 预置内核OkHttp
 * |
 * | ---------------------------------------------------------------------------------------------- |
 * |                                                                                                |
 * | 1.集成线程管理，在安卓系统中主线程不可以访问网络，所有网络访问通过线程器来分发线程单独处理     |
 * | 2.线程管理分为缓存管理线程和网络请求线程                                                       |
 * | 3.请求进入预处理器，进行基本设置检查，进行分配进入缓存队列还是请求队列                         |
 * | 4.实现内核工厂，获取当前处理请求的工厂类                                                       |
 * | 5.实现之前通过预处理器和解析器对一个请求的加密解密，参数拼装和响应解析                         |
 * | 6.通过分发器来返回结果                                                                         |
 * |                                                                                                |
 * | ---------------------------------------------------------------------------------------------- |
 */
public class RestTemplate extends HttpAccessor {


    private static final String TAG = "RestTemplate";

    private ICacheController cacheController;

    private IDeliveryController deliveryController;

    private IRequestQueueController messageQueueController;

    private IThreadHandler threadHandler;

    private INetWork netWork;

    public RestTemplate(){
        NestLog.v("%s init ", TAG);

    }

    public RestTemplate addCacheController(ICacheController cacheController){
        this.cacheController = cacheController;
        return this;
    }

    public ICacheController getCacheController(){
        return this.cacheController;
    }

    public RestTemplate addDeliveryController(IDeliveryController deliveryController){
        this.deliveryController = deliveryController;
        deliveryController.addRestTemplate(this);
        return this;
    }

    public IDeliveryController getDeliveryController(){
        return this.deliveryController;
    }


    public RestTemplate addMessageQueueController(IRequestQueueController messageQueueController){
        this.messageQueueController = messageQueueController;
        this.messageQueueController.addRestTemplate(this);
        return this;
    }

    public IRequestQueueController getMessageQueueController(){
        return this.messageQueueController;
    }


    public RestTemplate addThreadHandler(IThreadHandler threadHandler){
        this.threadHandler = threadHandler;
        this.threadHandler.addRestTemplate(this);
        return this;
    }

    public IThreadHandler getThreadHandler(){
        return this.threadHandler;
    }

    public RestTemplate addNetWork(INetWork netWork){
        this.netWork = netWork;
        this.netWork.addRestTemplate(this);
        return this;
    }

    public INetWork getNetWork(){
        return this.netWork;
    }


    /**
     * 初始化网络加载器
     * @param context 上下文引用
     * @param debug 调试模式是否开启
     * @return
     */
    public static RestTemplate init(Context context, boolean debug){

        Config.setContext(context);
        Config.setDebug(debug);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.addCacheController(new NullCache());
        restTemplate.addMessageQueueController(new HttpRequestQueue());
        restTemplate.addThreadHandler(new HttpThreadHandler());
        restTemplate.addDeliveryController(new DefaultDelivery(new Handler(Looper.getMainLooper())));
        restTemplate.addNetWork(new OkHttpStack());
        restTemplate.getThreadHandler().start();


        NestLog.v("restTemplate init complete");

        return restTemplate;
    }

    public Request add(Request request){
        if(request.getUrl() == null || request.getUrl().length() == 0) return request;
        return getMessageQueueController().add(request);
    }

    public void cancelAll(){
        getMessageQueueController().cancelAll();
    }

    public void finish(Request request){
        getMessageQueueController().finish(request);
    }


    public void cancel(Object tag){
        getMessageQueueController().cancel(tag);
    }



}
