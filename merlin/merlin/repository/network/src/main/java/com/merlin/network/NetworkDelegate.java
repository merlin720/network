package com.merlin.network;


import java.io.File;
import java.util.HashMap;

/**
 * User: Simon
 * Date: 2016/3/7
 * Desc: 全局网路请求代理
 */
public class NetworkDelegate {
    private static NetworkDelegate networkDelegate;
    private NetworkOption networkOption;

    private NetworkDelegate() {
    }

    public static synchronized NetworkDelegate getInstance() {
        if (networkDelegate == null) {
            networkDelegate = new NetworkDelegate();
        }

        return networkDelegate;
    }

    public <T> NetworkOption get(Class<T> className, String url, HashMap<String, ?> params, CallBack<T> callBack) {
        if (this.networkOption != null) {
            this.networkOption.get(className, url, params, callBack);
        }
        return networkOption;
    }

    public <T> NetworkOption get(Class<T> className, String url, HashMap<String, ?> params, CallBack<T> callBack, Object tag) {
        if (this.networkOption != null) {
            this.networkOption.get(className, url, params, callBack);
            this.networkOption.setTag(tag);
        }
        return networkOption;
    }


    public <T> NetworkOption post(Class<T> className, String url, HashMap<String, ?> params, CallBack<T> callBack) {
        if (this.networkOption != null) {
            this.networkOption.post(className, url, params, callBack);
        }
        return networkOption;
    }

    public <T> NetworkOption post(Class<T> className, String url, HashMap<String, ?> params, CallBack<T> callBack, Object tag) {
        if (this.networkOption != null) {
            this.networkOption.post(className, url, params, callBack);
            this.networkOption.setTag(tag);
        }
        return networkOption;
    }

    public <T> NetworkOption uploadPost(Class<T> className, String url, HashMap<String, ?> params, CallBack<T> callBack, String path) {
        if (this.networkOption != null) {
            return this.networkOption.uploadPost(className, url, params, callBack, path);
        }
        return null;
    }

    public <T> NetworkOption formPost(Class<T> className, String url, HashMap<String, ?> params, CallBack<T> callBack) {
        if (this.networkOption != null) {
            if (null == params) {
                params = new HashMap<>();
            }
            return this.networkOption.formPost(className, url, params, callBack);
        }
        return null;
    }

    public <T> NetworkOption formPost(Class<T> className, String url, HashMap<String, ?> params, CallBack<T> callBack, Object tag) {
        if (this.networkOption != null) {
            this.networkOption.formPost(className, url, params, callBack);
            this.networkOption.setTag(tag);
        }
        return networkOption;
    }

    public NetworkOption downloadFile(String url, String dir, String fileName, CallBack<File> callBack) {
        if (this.networkOption != null) {
            this.networkOption.downloadFile(url, dir, fileName, callBack);
        }
        return networkOption;
    }

    public NetworkOption downloadFile(String url, String dir, String fileName, CallBack<File> callBack, Object tag) {
        if (this.networkOption != null) {
            this.networkOption.downloadFile(url, dir, fileName, callBack);
            this.networkOption.setTag(tag);
        }
        return networkOption;
    }

    public void setNetworkOption(NetworkOption networkOption) {
        this.networkOption = networkOption;
    }

    public NetworkOption cancelAll() {
        if (this.networkOption != null) {
            this.networkOption.cancelAll();
        }
        return networkOption;
    }

    public NetworkOption cancel(Object tag) {
        if (this.networkOption != null) {
            this.networkOption.cancel(tag);
        }
        return networkOption;
    }
}

