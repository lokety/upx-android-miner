// Copyright (c) 2019, Mine2Gether.com
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

package com.uplexa.androidminer.api.providers;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

import com.uplexa.androidminer.api.ProviderData;
import com.uplexa.androidminer.network.Json;
import com.uplexa.androidminer.api.ProviderAbstract;
import com.uplexa.androidminer.api.PoolItem;

import static com.uplexa.androidminer.Tools.getReadableHashRateString;
import static com.uplexa.androidminer.Tools.parseCurrency;
import static com.uplexa.androidminer.Tools.tryParseLong;

public class uPlexaPool extends ProviderAbstract {

    public uPlexaPool(PoolItem pi){
        super(pi);
    }

    @Override
    protected void onBackgroundFetchData() {
        PrettyTime pTime = new PrettyTime();
        ProviderData mBlockData = getBlockData();
        mBlockData.isNew = false;

        try {
            String url = mPoolItem.getApiUrl() + "/stats";
            String dataStatsNetwork  = Json.fetch(url);
            Log.i(LOG_TAG, dataStatsNetwork);

            JSONObject joStats = new JSONObject(dataStatsNetwork);
            JSONObject joStatsConfig = joStats.getJSONObject("config");
            JSONObject joStatsLastBlock = joStats.getJSONObject("lastblock");
            JSONObject joStatsNetwork = joStats.getJSONObject("network");
            JSONObject joStatsPool = joStats.getJSONObject("pool");
            JSONObject joStatsPoolStats = joStatsPool.getJSONObject("stats");

            mBlockData.coin.name = joStatsConfig.optString("coin").toUpperCase();
            mBlockData.coin.units = tryParseLong(joStatsConfig.optString("coinUnits"), 1L);
            mBlockData.coin.symbol = joStatsConfig.optString("symbol").toUpperCase();
            mBlockData.coin.denominationUnit = tryParseLong(joStatsConfig.optString("denominationUnit"), 1L);

            mBlockData.pool.miners = joStatsPool.optString("miners", "0");
            mBlockData.pool.difficulty = getReadableHashRateString(joStatsPoolStats.optLong("totalDiff"));
            mBlockData.pool.lastBlockTime = pTime.format(new Date(joStatsPoolStats.optLong("lastblock_timestamp") * 1000));
            mBlockData.pool.lastRewardAmount = parseCurrency(joStatsPoolStats.optString("lastblock_lastReward", "0"), mBlockData.coin.units, mBlockData.coin.denominationUnit, mBlockData.coin.symbol);
            mBlockData.pool.hashrate = String.valueOf(tryParseLong(joStatsPool.optString("hashrate"),0L) / 1000L);
            mBlockData.pool.blocks = joStatsPoolStats.optString("blocksFound", "0");
            mBlockData.pool.minPayout = parseCurrency(joStatsConfig.optString("minPaymentThreshold", "0"), mBlockData.coin.units, mBlockData.coin.denominationUnit, mBlockData.coin.symbol);

            mBlockData.network.lastBlockHeight = joStatsLastBlock.optString("height");
            mBlockData.network.difficulty = getReadableHashRateString(joStatsNetwork.optLong("difficulty"));
            mBlockData.network.lastBlockTime = pTime.format(new Date(joStatsLastBlock.optLong("timestamp") * 1000));
            mBlockData.network.lastRewardAmount = parseCurrency(joStatsPoolStats.optString("lastblock_lastReward", "0"), mBlockData.coin.units, mBlockData.coin.denominationUnit, mBlockData.coin.symbol);
        } catch (JSONException e) {
            Log.i(LOG_TAG, "NETWORK\n" + e.toString());
            e.printStackTrace();
        }

        String wallet = getWalletAddress();
        Log.i(LOG_TAG, "Wallet: " + wallet);
        if(wallet.equals("")) {
            return;
        }
        try {
            String url = mPoolItem.getApiUrl() + "/stats_address?address=" + getWalletAddress();

            String dataWallet  = Json.fetch(url);

            JSONObject joStatsAddress = new JSONObject(dataWallet);
            JSONObject joStatsAddressStats = joStatsAddress.getJSONObject("stats");

            ProviderData.Coin coin = mBlockData.coin;
            String hashRate = joStatsAddressStats.optString("hashrate", "0");
            String balance = parseCurrency(joStatsAddressStats.optString("balance", "0"), coin.units, coin.denominationUnit, coin.symbol);
            String paid = parseCurrency(joStatsAddressStats.optString("paid", "0"), coin.units, coin.denominationUnit, coin.symbol);
            String lastShare = pTime.format(new Date(joStatsAddressStats.optLong("lastShare") * 1000));
            String blocks = String.valueOf(tryParseLong(joStatsAddressStats.optString("roundHashes"), 0L));

            Log.i(LOG_TAG, "hashRate: " + hashRate);

            mBlockData.miner.hashrate = hashRate;
            mBlockData.miner.balance = balance;
            mBlockData.miner.paid = paid;
            mBlockData.miner.lastShare = lastShare;
            mBlockData.miner.blocks = blocks;
        } catch (JSONException e) {
            Log.i(LOG_TAG, "ADDRESS\n" + e.toString());
            e.printStackTrace();
        }
    }
}
