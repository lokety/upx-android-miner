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

package io.uplexaproject.androidminer.api;

import java.util.Timer;
import java.util.TimerTask;

import io.uplexaproject.androidminer.Config;

public class ProviderRequest{

    protected PoolItem mPoolItem;

    private ProviderTask current;
    private IProviderListener mListener;
    public ProviderRequest setListener(IProviderListener listener) {
        if(mListener == listener) {
            return this;
        }
        mListener = listener;

        PoolItem pi = ProviderManager.getSelectedPool();
        if(pi == null) {
            return this;
        }
        pi.getInterface().mListener = listener;
        return this;
    }

    public class ProviderTask extends TimerTask{

        private ProviderAbstract mProvider;

        public ProviderTask(ProviderAbstract abs) {
            mProvider = abs;
        }

        @Override
        public void run(){
            mProvider.execute();
            repeat();
        }
    }

    ProviderTimer timer;

    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            current.cancel();
            timer = null;
            current = null;
        }
    }

    public void start() {
        if(current != null || mPoolItem == null) {
            return;
        }
        ProviderAbstract pa = mPoolItem.getInterface();
        pa.mListener = mListener;
        current = new ProviderTask(pa);
        current.run();
    }

    private void repeat() {
        stop();
        timer = new ProviderTimer();
        ProviderAbstract pa = mPoolItem.getInterface();
        pa.mListener = mListener;
        current = new ProviderTask(pa);
        timer.schedule(current, Config.statsDelay);
    }

    public class ProviderTimer extends Timer {
        public ProviderTimer() {
            super("ProviderTimer");
        }
    }
}
