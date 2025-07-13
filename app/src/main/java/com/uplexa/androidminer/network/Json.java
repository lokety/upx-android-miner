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

package com.uplexa.androidminer.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Json {

    private static final String LOG_TAG = "MiningSvc";

    private static final Json ourInstance = new Json();

    public static Json getInstance() {
        return ourInstance;
    }

    private Json() {
    }

    public static String fetch(String url) {

        StringBuilder data = new StringBuilder();
        try {

            URL urlFetch = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlFetch.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                    data.append(line);
            }

        } catch (MalformedURLException e) {
                Log.i(LOG_TAG, e.toString());
//                e.printStackTrace();
        } catch (IOException e) {
            Log.i(LOG_TAG, e.toString());
//            e.printStackTrace();
        }

        return data.toString();
    }
}
