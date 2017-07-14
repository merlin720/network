package com.merlin.network;

/**
 * 网络请求数据回掉配置
 * Created by ${Shenfeifei} on 2016/4/26.
 */
public class RequestBackConfig {
    /**访问网络失败*/
    public static final String FILE_BACK="FILE_BACK";
    /**成功访问网络，但是服务端返回错误*/
    public static final String SUCCESS_ERROR="SUCCESS_ERROR";

    /**成功访问网络---获取指定数据失败*/
    public static final String SUCCESS_NO_DATA_BACK="SUCCESS_NO_DATA_BACK";
}
