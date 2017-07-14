package com.merlin.network;
import java.io.File;
import java.util.HashMap;

/**
 * User: Simon
 * Date: 2016/3/7
 * Desc: 接口扩展
 *
 * 不希望网络框架和本地视图层、中间层有更多的交互细节
 * 所以委托一个代理来做中间规则封装和控制
 */
public interface NetworkOption {


    <T> NetworkOption get(Class<T> className, String url, HashMap<String, ?> params, CallBack<T> callBack);

    <T> NetworkOption post(Class<T> className, String url, HashMap<String, ?> params, CallBack<T> callBack);

    NetworkOption downloadFile(String url, String dir, String fileName, CallBack<File> callBack);

    <T> NetworkOption formPost(Class<T> className, String url, HashMap<String, ?> params, CallBack<T> callBack);

    <T> NetworkOption uploadPost(Class<T> className, String url, HashMap<String, ?> params, final CallBack<T> callBack,String path);
    /**
     * 设置标签
     * @param tag
     * @return
     */
    NetworkOption setTag(Object tag);

    /**
     * 取消所有
     *
     * @return
     */
    NetworkOption cancelAll();

    /**
     * 根据标签取消相应请求
     * @param tag 标签
     * @return
     */
    NetworkOption cancel(Object tag);
}
