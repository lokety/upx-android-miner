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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.uplexa.androidminer.api.ProviderManager;

public class WizardSettingsActivity extends com.uplexa.androidminer.BaseActivity {
    private SeekBar sbCores, sbCPUTemp, sbBatteryTemp, sbCooldown;
    private TextView tvCPUMaxTemp, tvBatteryMaxTemp, tvCooldown;

    private Integer nMaxCPUTemp = 65; // 55,60,65,70,75
    private Integer nMaxBatteryTemp = 45; // 30,35,40,45,50
    private Integer nCooldownTheshold = 15; // 10,15,20,25,30

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }

        setContentView(R.layout.fragment_wizard_settings);

        View view = findViewById(android.R.id.content).getRootView();

        sbCores = view.findViewById(R.id.seekbarcores);
        TextView tvCoresNb = view.findViewById(R.id.coresnb);
        TextView tvCoresMax = view.findViewById(R.id.coresmax);

        sbCPUTemp = view.findViewById(R.id.seekbarcputemperature);
        tvCPUMaxTemp = view.findViewById(R.id.cpumaxtemp);

        sbBatteryTemp = view.findViewById(R.id.seekbarbatterytemperature);
        tvBatteryMaxTemp = view.findViewById(R.id.batterymaxtemp);

        sbCooldown = view.findViewById(R.id.seekbarcooldownthreshold);
        tvCooldown = view.findViewById(R.id.cooldownthreshold);

        Button btnHardwareHelp = view.findViewById(R.id.btnHardwareHelp);
        btnHardwareHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // inflate the layout of the popup window
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

                View popupView = inflater.inflate(R.layout.helper_hardware_settings, null);
                com.uplexa.androidminer.Utils.showPopup(v, inflater, popupView);
            }
        });

        // Cores
        int cores = Runtime.getRuntime().availableProcessors();

        int suggested = cores / 2;
        if (suggested == 0) suggested = 1;

        sbCores.setMax(cores);
        tvCoresMax.setText(Integer.toString(cores));

        if (com.uplexa.androidminer.Config.read("cores").equals("")) {
            sbCores.setProgress(suggested);
            tvCoresNb.setText(Integer.toString(suggested));
        } else {
            int corenb = Integer.parseInt(com.uplexa.androidminer.Config.read("cores"));
            sbCores.setProgress(corenb);
            tvCoresNb.setText(Integer.toString(corenb));
        }

        // CPU Temp
        if (!com.uplexa.androidminer.Config.read("maxcputemp").equals("")) {
            nMaxCPUTemp = Integer.parseInt(com.uplexa.androidminer.Config.read("maxcputemp"));
        }

        int nProgress = ((nMaxCPUTemp - com.uplexa.androidminer.Utils.MIN_CPU_TEMP)/ com.uplexa.androidminer.Utils.INCREMENT) + 1;
        sbCPUTemp.setProgress(nProgress);
        updateCPUTemp();

        if (!com.uplexa.androidminer.Config.read("maxbatterytemp").equals("")) {
            nMaxBatteryTemp = Integer.parseInt(com.uplexa.androidminer.Config.read("maxbatterytemp"));
        }

        // Battery Temp
        nProgress = ((nMaxBatteryTemp- com.uplexa.androidminer.Utils.MIN_BATTERY_TEMP)/ com.uplexa.androidminer.Utils.INCREMENT)+1;
        sbBatteryTemp.setProgress(nProgress);
        updateBatteryTemp();

        if (!com.uplexa.androidminer.Config.read("cooldownthreshold").equals("")) {
            nCooldownTheshold = Integer.parseInt(com.uplexa.androidminer.Config.read("cooldownthreshold"));
        }

        // Cooldown
        nProgress = ((nCooldownTheshold - com.uplexa.androidminer.Utils.MIN_COOLDOWN) / com.uplexa.androidminer.Utils.INCREMENT) + 1;
        sbCooldown.setProgress(nProgress);
        updateCooldownThreshold();

        sbCores.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvCoresNb.setText(Integer.toString(progress));
            }
        });

        sbCPUTemp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateCPUTemp();
            }
        });

        sbBatteryTemp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateBatteryTemp();
            }
        });

        sbCooldown.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateCooldownThreshold();
            }
        });

        // Set click listener for saveSettings
        findViewById(R.id.saveSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart(v); // Reuse existing method
            }
        });
    }

    private Integer getCPUTemp() {
        return ((sbCPUTemp.getProgress() - 1) * com.uplexa.androidminer.Utils.INCREMENT) + com.uplexa.androidminer.Utils.MIN_CPU_TEMP;
    }

    private Integer getBatteryTemp() {
        return ((sbBatteryTemp.getProgress() - 1) * com.uplexa.androidminer.Utils.INCREMENT) + com.uplexa.androidminer.Utils.MIN_BATTERY_TEMP;
    }

    private Integer getCooldownTheshold() {
        return ((sbCooldown.getProgress() - 1) * com.uplexa.androidminer.Utils.INCREMENT) + com.uplexa.androidminer.Utils.MIN_COOLDOWN;
    }

    private void updateCPUTemp(){
        tvCPUMaxTemp.setText(Integer.toString(getCPUTemp()));
    }

    private void updateBatteryTemp() {
        tvBatteryMaxTemp.setText(Integer.toString(getBatteryTemp()));
    }

    private void updateCooldownThreshold() {
        tvCooldown.setText(Integer.toString(getCooldownTheshold()));
    }

    public void onStart(View view) {
        com.uplexa.androidminer.Config.write("workername", com.uplexa.androidminer.Tools.getDeviceName());

        com.uplexa.androidminer.Config.write("cores", Integer.toString(sbCores.getProgress()));
        com.uplexa.androidminer.Config.write("maxcputemp", Integer.toString(getCPUTemp()));
        com.uplexa.androidminer.Config.write("maxbatterytemp", Integer.toString(getBatteryTemp()));
        com.uplexa.androidminer.Config.write("cooldownthreshold", Integer.toString(getCooldownTheshold()));

        com.uplexa.androidminer.Config.write("threads", "1"); // Default value
        com.uplexa.androidminer.Config.write("intensity", "1"); // Default value

        com.uplexa.androidminer.Config.write("disableamayc", "0");

        com.uplexa.androidminer.Config.write("init", "1");

        ProviderManager.generate();

        startActivity(new Intent(WizardSettingsActivity.this, com.uplexa.androidminer.MainActivity.class));
        finish();
        com.uplexa.androidminer.Config.write("hide_setup_wizard", "1");
    }
}