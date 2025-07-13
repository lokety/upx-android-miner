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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.text.DecimalFormat;

public class WizardPoolActivity extends com.uplexa.androidminer.BaseActivity {
    private static final String LOG_TAG = "WizardPoolActivity";

    private int selectedPoolIndex = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }

        setContentView(R.layout.fragment_wizard_pool);

        View view = findViewById(android.R.id.content).getRootView();

        RequestQueue queue = Volley.newRequestQueue(this);

        // IoT Proxy
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://uplexa.com/o3.php?r=stats&wsd",
                response -> {
                    try {
                        Log.i(LOG_TAG, "response: " + response);

                        JSONObject obj = new JSONObject(response);
                        JSONObject objStats = obj.getJSONObject("pool_statistics");

                        TextView tvMinersGNTL = view.findViewById(R.id.minersuPlexa);
                        tvMinersGNTL.setText(String.format("%s %s", objStats.getString("miners"), getResources().getString(R.string.miners)));

                        TextView tvHrGNTL = view.findViewById(R.id.hruPlexa);
                        float fHrGNTL = com.uplexa.androidminer.Utils.convertStringToFloat(objStats.getString("hashRate")) / 1000.0f;
                        tvHrGNTL.setText(String.format("%s kH/s", new DecimalFormat("##.#").format(fHrGNTL)));

                    } catch (Exception e) {
                        //Do nothing
                    }
                }
                , this::parseVolleyError);

        queue.add(stringRequest);

        // Miner.rocks
        stringRequest = new StringRequest(Request.Method.GET, "https://api.uplexapool.com/stats",
                response -> {
                    try {
                        Log.i(LOG_TAG, "response: " + response);

                        JSONObject obj = new JSONObject(response);
                        JSONObject objConfig = obj.getJSONObject("config");
                        JSONObject objConfigPool = obj.getJSONObject("pool");

                        TextView tvMinersMR = view.findViewById(R.id.minersMR);
                        tvMinersMR.setText(String.format("%s %s", objConfigPool.getString("miners"), getResources().getString(R.string.miners)));

                        TextView tvHrMR = view.findViewById(R.id.hrMR);
                        float fHrMR = com.uplexa.androidminer.Utils.convertStringToFloat(objConfigPool.getString("hashrate")) / 1000.0f;
                        tvHrMR.setText(String.format("%s kH/s", new DecimalFormat("##.#").format(fHrMR)));

                    } catch (Exception e) {
                        //Do nothing
                    }
                }
                , this::parseVolleyError);

        queue.add(stringRequest);

        // uPlexa Online
        stringRequest = new StringRequest(Request.Method.GET, "https://upxstats.miningocean.org/stats",
                response -> {
                    try {
                        Log.i(LOG_TAG, "response: " + response);

                        JSONObject obj = new JSONObject(response);
                        JSONObject objConfig = obj.getJSONObject("config");
                        JSONObject objConfigPool = obj.getJSONObject("pool");
                        TextView tvMinersHM = view.findViewById(R.id.minersHM);
                        tvMinersHM.setText(String.format("%s %s", objConfigPool.getString("miners"), getResources().getString(R.string.miners)));

                        TextView tvHrHM = view.findViewById(R.id.hrHM);
                        float fHrHM = com.uplexa.androidminer.Utils.convertStringToFloat(objConfigPool.getString("hashrate")) / 1000.0f;
                        tvHrHM.setText(String.format("%s kH/s", new DecimalFormat("##.#").format(fHrHM)));

                    } catch (Exception e) {
                        //Do nothing
                    }
                }
                , this::parseVolleyError);

        queue.add(stringRequest);

        // MiningOcean
        stringRequest = new StringRequest(Request.Method.GET, "https://uplexa.miner.rocks/api/stats",
                response -> {
                    try {
                        Log.i(LOG_TAG, "response: " + response);

                        JSONObject obj = new JSONObject(response);
                        JSONObject objStats = obj.getJSONObject("pool");

                        TextView tvMinersGNTL = view.findViewById(R.id.minersGNTL);
                        tvMinersGNTL.setText(String.format("%s %s", objStats.getString("miners"), getResources().getString(R.string.miners)));

                        TextView tvHrGNTL = view.findViewById(R.id.hrGNTL);
                        float fHrGNTL = com.uplexa.androidminer.Utils.convertStringToFloat(objStats.getString("hashRate")) / 1000.0f;
                        tvHrGNTL.setText(String.format("%s kH/s", new DecimalFormat("##.#").format(fHrGNTL)));

                    } catch (Exception e) {
                        //Do nothing
                    }
                }
                , this::parseVolleyError);

        queue.add(stringRequest);

        // Set click listener for lluPlexa
        LinearLayout lluPlexa = findViewById(R.id.lluPlexa);
        if (lluPlexa != null) {
            lluPlexa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickuPlexa(v); // Reuse existing method
                }
            });
        }

        // Set click listener for llMR
        LinearLayout llMR = findViewById(R.id.llMR);
        if (llMR != null) {
            llMR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickMR(v); // Reuse existing method
                }
            });
        }

        // Set click listener for llHM
        LinearLayout llHM = findViewById(R.id.llHM);
        if (llHM != null) {
            llHM.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickHM(v); // Reuse existing method
                }
            });
        }

        // Set click listener for llGNTL
        LinearLayout llGNTL = findViewById(R.id.llGNTL);
        if (llGNTL != null) {
            llGNTL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickGNTL(v); // Reuse existing method
                }
            });
        }

        // Set click listener for btnNext
        findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNext(v); // Reuse existing method
            }
        });
    }

    private void parseVolleyError(VolleyError error) {
        // Do nothing
    }

    public void onClickuPlexa(View view) {
        View view2 = findViewById(android.R.id.content).getRootView();

        selectedPoolIndex = 1;

        LinearLayout lluPlexa = view2.findViewById(R.id.lluPlexa);
        int bottom = lluPlexa.getPaddingBottom();
        int top = lluPlexa.getPaddingTop();
        int right = lluPlexa.getPaddingRight();
        int left = lluPlexa.getPaddingLeft();
        lluPlexa.setBackgroundResource(R.drawable.corner_radius_lighter_border_blue);
        lluPlexa.setPadding(left, top, right, bottom);

        LinearLayout llMR = view2.findViewById(R.id.llMR);
        bottom = llMR.getPaddingBottom();
        top = llMR.getPaddingTop();
        right = llMR.getPaddingRight();
        left = llMR.getPaddingLeft();
        llMR.setBackgroundResource(R.drawable.corner_radius_lighter);
        llMR.setPadding(left, top, right, bottom);

        LinearLayout llHM = view2.findViewById(R.id.llHM);
        bottom = llHM.getPaddingBottom();
        top = llHM.getPaddingTop();
        right = llHM.getPaddingRight();
        left = llHM.getPaddingLeft();
        llHM.setBackgroundResource(R.drawable.corner_radius_lighter);
        llHM.setPadding(left, top, right, bottom);

        LinearLayout llGNTL = view2.findViewById(R.id.llGNTL);
        bottom = llGNTL.getPaddingBottom();
        top = llGNTL.getPaddingTop();
        right = llGNTL.getPaddingRight();
        left = llGNTL.getPaddingLeft();
        llGNTL.setBackgroundResource(R.drawable.corner_radius_lighter);
        llGNTL.setPadding(left, top, right, bottom);
    }

    public void onClickMR(View view) {
        View view2 = findViewById(android.R.id.content).getRootView();

        selectedPoolIndex = 2;

        LinearLayout lluPlexa = view2.findViewById(R.id.lluPlexa);
        int bottom = lluPlexa.getPaddingBottom();
        int top = lluPlexa.getPaddingTop();
        int right = lluPlexa.getPaddingRight();
        int left = lluPlexa.getPaddingLeft();
        lluPlexa.setBackgroundResource(R.drawable.corner_radius_lighter);
        lluPlexa.setPadding(left, top, right, bottom);

        LinearLayout llMR = view2.findViewById(R.id.llMR);
        bottom = llMR.getPaddingBottom();
        top = llMR.getPaddingTop();
        right = llMR.getPaddingRight();
        left = llMR.getPaddingLeft();
        llMR.setBackgroundResource(R.drawable.corner_radius_lighter_border_blue);
        llMR.setPadding(left, top, right, bottom);

        LinearLayout llHM = view2.findViewById(R.id.llHM);
        bottom = llHM.getPaddingBottom();
        top = llHM.getPaddingTop();
        right = llHM.getPaddingRight();
        left = llHM.getPaddingLeft();
        llHM.setBackgroundResource(R.drawable.corner_radius_lighter);
        llHM.setPadding(left, top, right, bottom);

        LinearLayout llGNTL = view2.findViewById(R.id.llGNTL);
        bottom = llGNTL.getPaddingBottom();
        top = llGNTL.getPaddingTop();
        right = llGNTL.getPaddingRight();
        left = llGNTL.getPaddingLeft();
        llGNTL.setBackgroundResource(R.drawable.corner_radius_lighter);
        llGNTL.setPadding(left, top, right, bottom);
    }

    public void onClickHM(View view) {
        View view2 = findViewById(android.R.id.content).getRootView();

        selectedPoolIndex = 3;

        LinearLayout lluPlexa = view2.findViewById(R.id.lluPlexa);
        int bottom = lluPlexa.getPaddingBottom();
        int top = lluPlexa.getPaddingTop();
        int right = lluPlexa.getPaddingRight();
        int left = lluPlexa.getPaddingLeft();
        lluPlexa.setBackgroundResource(R.drawable.corner_radius_lighter);
        lluPlexa.setPadding(left, top, right, bottom);

        LinearLayout llMR = view2.findViewById(R.id.llMR);
        bottom = llMR.getPaddingBottom();
        top = llMR.getPaddingTop();
        right = llMR.getPaddingRight();
        left = llMR.getPaddingLeft();
        llMR.setBackgroundResource(R.drawable.corner_radius_lighter);
        llMR.setPadding(left, top, right, bottom);

        LinearLayout llHM = view2.findViewById(R.id.llHM);
        bottom = llHM.getPaddingBottom();
        top = llHM.getPaddingTop();
        right = llHM.getPaddingRight();
        left = llHM.getPaddingLeft();
        llHM.setBackgroundResource(R.drawable.corner_radius_lighter_border_blue);
        llHM.setPadding(left, top, right, bottom);

        LinearLayout llGNTL = view2.findViewById(R.id.llGNTL);
        bottom = llGNTL.getPaddingBottom();
        top = llGNTL.getPaddingTop();
        right = llGNTL.getPaddingRight();
        left = llGNTL.getPaddingLeft();
        llGNTL.setBackgroundResource(R.drawable.corner_radius_lighter);
        llGNTL.setPadding(left, top, right, bottom);
    }

    public void onClickGNTL(View view) {
        View view2 = findViewById(android.R.id.content).getRootView();

        selectedPoolIndex = 4;

        LinearLayout lluPlexa = view2.findViewById(R.id.lluPlexa);
        int bottom = lluPlexa.getPaddingBottom();
        int top = lluPlexa.getPaddingTop();
        int right = lluPlexa.getPaddingRight();
        int left = lluPlexa.getPaddingLeft();
        lluPlexa.setBackgroundResource(R.drawable.corner_radius_lighter);
        lluPlexa.setPadding(left, top, right, bottom);

        LinearLayout llMR = view2.findViewById(R.id.llMR);
        bottom = llMR.getPaddingBottom();
        top = llMR.getPaddingTop();
        right = llMR.getPaddingRight();
        left = llMR.getPaddingLeft();
        llMR.setBackgroundResource(R.drawable.corner_radius_lighter);
        llMR.setPadding(left, top, right, bottom);

        LinearLayout llHM = view2.findViewById(R.id.llHM);
        bottom = llHM.getPaddingBottom();
        top = llHM.getPaddingTop();
        right = llHM.getPaddingRight();
        left = llHM.getPaddingLeft();
        llHM.setBackgroundResource(R.drawable.corner_radius_lighter);
        llHM.setPadding(left, top, right, bottom);

        LinearLayout llGNTL = view2.findViewById(R.id.llGNTL);
        bottom = llGNTL.getPaddingBottom();
        top = llGNTL.getPaddingTop();
        right = llGNTL.getPaddingRight();
        left = llGNTL.getPaddingLeft();
        llGNTL.setBackgroundResource(R.drawable.corner_radius_lighter_border_blue);
        llGNTL.setPadding(left, top, right, bottom);
    }

    public void onNext(View view) {
        com.uplexa.androidminer.Config.write("selected_pool", Integer.toString(selectedPoolIndex));

        startActivity(new Intent(WizardPoolActivity.this, com.uplexa.androidminer.WizardSettingsActivity.class));
        finish();
    }
}