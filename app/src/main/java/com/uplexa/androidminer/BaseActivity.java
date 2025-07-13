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

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import static com.uplexa.androidminer.MainActivity.contextOfApplication;

public abstract class BaseActivity extends AppCompatActivity {
    private static int sessionDepth = 0;

    @Override
    protected void onStart() {
        super.onStart();
        sessionDepth++;
        if(sessionDepth == 1){
            //app came to foreground;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (sessionDepth > 0)
            sessionDepth--;
        if (sessionDepth == 0) {
            // app went to background
            if(com.uplexa.androidminer.MainActivity.isDeviceMiningBackground())
                Toast.makeText(contextOfApplication, getResources().getString(R.string.miningbackground), Toast.LENGTH_SHORT).show();
        }
    }
}
