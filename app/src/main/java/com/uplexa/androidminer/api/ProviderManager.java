// Copyright (c) 2021 Scala
// Copyright (c) 2020, uPlexa
// Please see the included LICENSE file for more information.

// Note: This file contains some code taken from Scala, a project that had
// forked uPlexa's original android miner and stripped all copyright and
// and released the miner as their own without any credit to the uPlexa
// contributors. Since then, the only thing the Scala team has completed in their original
// whitepaper from 2018 is the android miner (after we were able to
// get one working for them) Their new UI is shiny, and thus, some of their code has
// been used.

package com.uplexa.androidminer.api;

import java.util.ArrayList;
import com.uplexa.androidminer.Config;

public final class ProviderManager {

    static private ArrayList<PoolItem> mPools = new ArrayList<PoolItem>();

    static public void add(PoolItem poolItem) {
        mPools.add(poolItem);
    }

    static public void add(String key, String pool,String port, int poolType, String poolUrl, String poolIP) {
        mPools.add(new PoolItem(key, pool, port, poolType, poolUrl, poolIP));
    }

    static public void add(String key, String pool, String port, int poolType, String poolUrl, String poolIP, String poolApi) {
        mPools.add(new PoolItem(key, pool, port, poolType, poolUrl, poolIP, poolApi, "",""));
    }

    static public PoolItem[] getPools() {
        return mPools.toArray(new PoolItem[mPools.size()]);
    }

    static public PoolItem getPoolById(int idx) {
        return mPools.get(idx);
    }

    static public PoolItem getPoolById(String idx) {
        int index = Integer.parseInt(idx);

        if (idx.equals("") || mPools.size() < index || mPools.size() == 0) {
            return null;
        }

        return mPools.get(index);
    }

    static final public ProviderData data = new ProviderData();

    static public PoolItem getSelectedPool() {
        if(request.mPoolItem != null) {
            return request.mPoolItem;
        }

        String sp = Config.read("selected_pool");
        if (sp.equals("")) {
            return null;
        }

        return getPoolById(sp);
    }
    static public void afterSave() {
        if(request.mPoolItem != null)  {
            return;
        }

        PoolItem pi = getSelectedPool();
        if(pi == null) {
            return;
        }

        //mPools.clear();
        request.mPoolItem = pi;
        data.isNew = true;
        request.start();
    }

    static final public com.uplexa.androidminer.api.ProviderRequest request = new com.uplexa.androidminer.api.ProviderRequest();

    static public void generate() {
        request.stop();
        request.mPoolItem = null;
        //mPools.clear();

        if(!mPools.isEmpty())
            return;

        // User Defined
        add("custom", "custom", "3333", 0, "custom", "");

        // uPlexa Official pool
        add(
                "uPleax IoT Proxy",
                "iot-proxy.uplexa.com",
                "9194",
                4, // uPlexa IoT Proxy
                "https://iot-proxy.uplexa.com",
                "45.79.218.212",
                "https://uplexa.com/o3.php?r="
        );

        // uPlexa Pool
        add(
                "uPlexaPool.com (Official Pool)",
                "mine.uplexapool.com",
                "1111",
                3, // CryptonoteNodeJS
                "https://uplexapool.com",
                "194.163.131.219",
                "https://api.uplexapool.com"
        );

        // MiningOcean
        add(
                "MiningOcean",
                "upx.miningocean.org",
                "4372",
                3, // NodeJS
                "https://upx.miningocean.org",
                "54.36.98.147",
                "https://upxstats.miningocean.org"
        );

        // Miner.rocks
        add(
                "Miner.Rocks",
                "upx.miner.rocks",
                "30022",
                2, // NodeJS
                "https://uplexa.miner.rocks",
                "54.36.98.147"
        );


    }
}
