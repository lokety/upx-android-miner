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

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WizardHomeActivity extends com.uplexa.androidminer.BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }

        setContentView(R.layout.fragment_wizard_home);

        View view = findViewById(android.R.id.content).getRootView();

        String sDisclaimerText = getResources().getString(R.string.disclaimer_agreement);
        String sDiclaimer = getResources().getString(R.string.disclaimer);

        SpannableString ss = new SpannableString(sDisclaimerText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                showDisclaimer();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        int iStart = sDisclaimerText.indexOf(sDiclaimer);
        int iEnd = iStart + sDiclaimer.length();
        ss.setSpan(clickableSpan, iStart, iEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView tvDisclaimer = view.findViewById(R.id.disclaimer);
        tvDisclaimer.setText(ss);
        tvDisclaimer.setMovementMethod(LinkMovementMethod.getInstance());
        tvDisclaimer.setLinkTextColor(getResources().getColor(R.color.c_blue));
        tvDisclaimer.setHighlightColor(Color.TRANSPARENT);

        // Set click listener for enterWalletAddress
        Button btnEnterAddress = findViewById(R.id.enterWalletAddress);
        if (btnEnterAddress != null) {
            btnEnterAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEnterAddress(v); // Reuse existing method
                }
            });
        }

        // Set click listener for createWalletAddress
        Button btnCreateWallet = findViewById(R.id.createWalletAddress);
        if (btnCreateWallet != null) {
            btnCreateWallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCreateWallet(v); // Reuse existing method
                }
            });
        }
    }

    public void onEnterAddress(View view) {
        startActivity(new Intent(WizardHomeActivity.this, com.uplexa.androidminer.WizardAddressActivity.class));
        finish();
    }

    public void onCreateWallet(View view) {
        Uri uri = Uri.parse(getResources().getString(R.string.uplexa_vault_play_store));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void onSkip(View view) {
        startActivity(new Intent(WizardHomeActivity.this, com.uplexa.androidminer.MainActivity.class));
        finish();

        com.uplexa.androidminer.Config.write("hide_setup_wizard", "1");
    }

    private void showDisclaimer() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.disclaimer);
        dialog.setTitle("Disclaimer");
        dialog.setCancelable(false);

        Button btnOK = dialog.findViewById(R.id.btnAgree);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.uplexa.androidminer.Config.write("disclaimer_agreed", "1");
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}