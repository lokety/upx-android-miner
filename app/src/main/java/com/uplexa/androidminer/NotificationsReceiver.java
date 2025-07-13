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

package com.uplexa.androidminer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationsReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "NotificationsReceiver";
    static public MainActivity activity = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(MainActivity.OPEN_ACTION.equals(action)) {
            Log.v(LOG_TAG,"OPEN_ACTION");
        } else if(MainActivity.STOP_ACTION.equals(action)) {
            Log.v(LOG_TAG,"STOP_ACTION");
            activity.stopMining();
        }
    }
}
