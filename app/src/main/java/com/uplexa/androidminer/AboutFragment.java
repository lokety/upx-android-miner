// Copyright (c) 2020, uPlexa
// Copyright (c) 2019, Mine2Gether.com
// Copyright (c) 2021 Scala
// Please see the included LICENSE file for more information.

// Note: This file contains some code taken from Scala, a project that had
// forked uPlexa's original android miner and stripped all copyright and
// and released the miner as their own without any credit to the uPlexa
// contributors. Since then, the only thing the Scala team has completed in their original
// whitepaper from 2018 is the android miner (after we were able to
// get one working for them) Their new UI is shiny, and thus, some of their code has
// been used.

package io.uplexaproject.androidminer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

public class AboutFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        TextView tvBuild, tvuPlexa, tvMine2gether, tvMonerominer, tvMaterialDesign, tvFontAwesome;

        tvBuild = view.findViewById(R.id.build);

        tvuPlexa = view.findViewById(R.id.uPlexaURL);
        tvMine2gether = view.findViewById(R.id.Mine2getherURL);
        tvMonerominer = view.findViewById(R.id.MoneroMinerURL);
        tvMaterialDesign = view.findViewById(R.id.MaterialDesignURL);
        tvFontAwesome = view.findViewById(R.id.FontAwesomeURL);

        Button btnGitHub = view.findViewById(R.id.btnGitHub);
        btnGitHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(getResources().getString(R.string.githubLink));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        Button btnDiscord = view.findViewById(R.id.btnDiscord);
        btnDiscord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(getResources().getString(R.string.discordLink));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        Button btnMedium = view.findViewById(R.id.btnMedium);
        btnMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(getResources().getString(R.string.mediumLink));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        Button btnTwitter = view.findViewById(R.id.btnTwitter);
        btnTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(getResources().getString(R.string.twitterLink));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        Button btnTelegram = view.findViewById(R.id.btnTelegram);
        btnTelegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(getResources().getString(R.string.telegramLink));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        Button btnDonationAddressesHelp = view.findViewById(R.id.btnDonationsHelp);
        btnDonationAddressesHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // inflate the layout of the popup window
                View popupView = inflater.inflate(R.layout.helper_donation_addresses, null);
                Utils.showPopup(v, inflater, popupView);
            }
        });

        Button btnDonateBTC = view.findViewById(R.id.btnDonateBTC);
        btnDonateBTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.copyToClipboard("uPlexa BTC Donation Address", Utils.UPLEXA_BTC_ADDRESS);
                Toast.makeText(getContext(), getResources().getString(R.string.donationadressbtc_copied), Toast.LENGTH_SHORT).show();
            }
        });

        Button btnDonateETH = view.findViewById(R.id.btnDonateETH);
        btnDonateETH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.copyToClipboard("uPlexa ETH Donation Address", Utils.UPLEXA_ETH_ADDRESS);
                Toast.makeText(getContext(), getResources().getString(R.string.donationadresseth_copied), Toast.LENGTH_SHORT).show();
            }
        });

        Button btnDonateUPX = view.findViewById(R.id.btnDonateUPX);
        btnDonateUPX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.copyToClipboard("uPlexa UPX Donation Address", Utils.UPLEXA_UPX_ADDRESS);
                Toast.makeText(getContext(), getResources().getString(R.string.donationadressupx_copied), Toast.LENGTH_SHORT).show();
            }
        });

        StringBuilder cpuinfo = new StringBuilder(Config.read("CPUINFO").trim());
        if(cpuinfo.length() == 0) {
            try {
                Map<String, String> m = Tools.getCPUInfo();

                cpuinfo = new StringBuilder("ABI: " + Tools.getABI() + "\n");
                for (Map.Entry<String, String> pair : m.entrySet()) {
                    cpuinfo.append(pair.getKey()).append(": ").append(pair.getValue()).append("\n");
                }
            } catch (Exception e) {
                cpuinfo = new StringBuilder();
            }

            Config.write("CPUINFO", cpuinfo.toString().trim());
        }

        // Convert build time to readable date
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(BuildConfig.BUILD_TIME);
        String build_time = DateFormat.getDateInstance(DateFormat.LONG).format(calendar.getTime());

        tvBuild.setText(BuildConfig.VERSION_NAME + " (" + build_time + ")");

        tvuPlexa.setText(Html.fromHtml(getString(R.string.uPlexaLink)));
        tvuPlexa.setMovementMethod(LinkMovementMethod.getInstance());

        tvMine2gether.setText(Html.fromHtml(getString(R.string.Mine2getherLink)));
        tvMine2gether.setMovementMethod(LinkMovementMethod.getInstance());

        tvMonerominer.setText(Html.fromHtml(getString(R.string.MoneroMinerLink)));
        tvMonerominer.setMovementMethod(LinkMovementMethod.getInstance());

        tvMaterialDesign.setText(Html.fromHtml(getString(R.string.MaterialDesignLink)));
        tvMaterialDesign.setMovementMethod(LinkMovementMethod.getInstance());

        tvFontAwesome.setText(Html.fromHtml(getString(R.string.FontAwesomeLink)));
        tvFontAwesome.setMovementMethod(LinkMovementMethod.getInstance());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH);
        String build_time_debug = formatter.format(calendar.getTime());

        String sDebugInfo = "Version Code: " + BuildConfig.VERSION_CODE + "\n" +
                "Version Name: " + BuildConfig.VERSION_NAME + "\n" +
                "Build Time: " + build_time_debug + "\n\n" +
                "Device Name: " + Tools.getDeviceName() + "\n" +
                "CPU Info: " + cpuinfo;

        Button btnDebugInfo = view.findViewById(R.id.btnDebugInfo);
        btnDebugInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.copyToClipboard("uPlexa Android Miner Debug Info", sDebugInfo);
                Toast.makeText(getContext(), getResources().getString(R.string.debuginfo_copied), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
