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

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
        Config.initialize(preferences);

        String configversion = Config.read("config_version");
        if(!configversion.equals(Config.version)) {
            Config.clear();
            Config.write("config_version", Config.version);
        }

        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }

        setContentView(R.layout.splashscreen);

        int millisecondsDelay = 2000;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                String hide_setup_wizard = Config.read("hide_setup_wizard");

                //startActivity(new Intent(SplashActivity.this, WizardHomeActivity.class));

                if (hide_setup_wizard.equals("")) {
                    startActivity(new Intent(SplashActivity.this, com.uplexa.androidminer.WizardHomeActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }

                finish();
            }
        }, millisecondsDelay);
    }
}
