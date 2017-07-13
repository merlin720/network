package com.merlin.network.internal;

import com.merlin.network.controller.ICacheController;

/**
 * User: Simon
 * Date: 2016/1/19
 * Desc:
 */
public class NullCache implements ICacheController {
    @Override
    public Entry get(String key) {
        return null;
    }

    @Override
    public void put(String key, Entry entry) {

    }

    @Override
    public void initialize() {

    }

    @Override
    public void remove(String key) {

    }

    @Override
    public void clear() {

    }
}
