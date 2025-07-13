// Copyright (c) 2020, uPlexa
// Copyright (c) 2021 Scala
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
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.uplexa.androidminer.api.ProviderData;
import com.uplexa.androidminer.api.PoolItem;
import com.uplexa.androidminer.api.IProviderListener;
import com.uplexa.androidminer.api.ProviderManager;

public class StatsFragment extends Fragment {

    private static final String LOG_TAG = "MiningSvc";

    private TextView tvViewStatsOnline;

    protected IProviderListener statsListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        statsListener = new IProviderListener() {
            public void onStatsChange(ProviderData d) {
                updateFields(d, view);
            }

            @Override
            public boolean onEnabledRequest() {
                return checkValidState();
            }
        };

        tvViewStatsOnline = view.findViewById(R.id.checkstatsonline);
        tvViewStatsOnline.setPaintFlags(tvViewStatsOnline.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvViewStatsOnline.setEnabled(false);
        tvViewStatsOnline.setTextColor(getResources().getColor(R.color.c_grey));

        // Set up the click listener to trigger onShowCores (sendInput("h"))
        tvViewStatsOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((com.uplexa.androidminer.MainActivity) requireActivity()).sendInput("h");
            }
        });

        ProviderManager.request.setListener(statsListener).start();
        ProviderManager.afterSave();
        updateFields(ProviderManager.data, view);

        return view;
    }

    private void updateFields(ProviderData d, View view) {
        if (view == null || view.getContext() == null)
            return;

        if (d.isNew) {
            enableOnlineStats(false);
            return;
        }

        PoolItem pm = ProviderManager.getSelectedPool();

        // Network
        TextView tvNetworkHashrate = view.findViewById(R.id.hashratenetwork);
        tvNetworkHashrate.setText(d.network.hashrate.isEmpty() ? "n/a" : d.network.hashrate);

        TextView tvNetworkDifficulty = view.findViewById(R.id.difficultynetwork);
        tvNetworkDifficulty.setText(d.network.difficulty.isEmpty() ? "n/a" : d.network.difficulty);

        TextView tvNetworkBlocks = view.findViewById(R.id.lastblocknetwork);
        tvNetworkBlocks.setText(d.network.lastBlockTime.isEmpty() ? "n/a" : d.network.lastBlockTime);

        TextView tvNetworkHeight = view.findViewById(R.id.height);
        tvNetworkHeight.setText(d.network.lastBlockHeight.isEmpty() ? "n/a" : d.network.lastBlockHeight);

        TextView tvNetworkRewards = view.findViewById(R.id.rewards);
        tvNetworkRewards.setText(d.network.lastRewardAmount.isEmpty() ? "n/a" : d.network.lastRewardAmount);

        // Pool
        TextView tvPoolURL = view.findViewById(R.id.poolurl);
        tvPoolURL.setText(pm.getPool() == null ? "" : pm.getPool());

        TextView tvPoolHashrate = view.findViewById(R.id.hashratepool);
        tvPoolHashrate.setText(d.pool.hashrate.isEmpty() ? "n/a" : d.pool.hashrate);

        TextView tvPoolMiners = view.findViewById(R.id.miners);
        tvPoolMiners.setText(d.pool.miners.isEmpty() ? "n/a" : d.pool.miners);

        TextView tvPoolLastBlock = view.findViewById(R.id.blockspool);
        tvPoolLastBlock.setText(d.pool.blocks.isEmpty() ? "n/a" : d.pool.blocks);

        // Address
        String wallet = com.uplexa.androidminer.Config.read("address");
        String prettyaddress = wallet.substring(0, 7) + "..." + wallet.substring(wallet.length() - 7);

        TextView tvWalletAddress = view.findViewById(R.id.walletaddress);
        tvWalletAddress.setText(prettyaddress);

        String sHashrate = d.miner.hashrate;
        if (sHashrate != null) {
            sHashrate = sHashrate.replace("H", "").trim();
            TextView tvAddressHashrate = view.findViewById(R.id.hashrateminer);
            tvAddressHashrate.setText(sHashrate);

            TextView tvAddressLastShare = view.findViewById(R.id.lastshareminer);
            tvAddressLastShare.setText(d.miner.lastShare.isEmpty() ? "n/a" : d.miner.lastShare);

            TextView tvAddressBlocks = view.findViewById(R.id.blocksminedminer);
            tvAddressBlocks.setText(d.miner.blocks.isEmpty() ? "n/a" : d.miner.blocks);

            String sBalance = d.miner.balance.replace("UPX", "").trim();
            TextView tvBalance = view.findViewById(R.id.balance);
            tvBalance.setText(sBalance);

            String sPaid = d.miner.paid.replace("UPX", "").trim();
            TextView tvPaid = view.findViewById(R.id.paid);
            tvPaid.setText(sPaid);
        }

        enableOnlineStats(true);
    }

    private void enableOnlineStats(boolean enable) {
        tvViewStatsOnline.setEnabled(enable);

        if (enable) {
            tvViewStatsOnline.setTextColor(getResources().getColor(R.color.c_blue));
        } else {
            tvViewStatsOnline.setTextColor(getResources().getColor(R.color.c_grey));
        }
    }

    public boolean checkValidState() {
        if (getContext() == null)
            return false;

        if (com.uplexa.androidminer.Config.read("address").equals("")) {
            Toast.makeText(getContext(), "Wallet address is empty.", Toast.LENGTH_LONG).show();
            enableOnlineStats(false);
            return false;
        }

        PoolItem pi = ProviderManager.getSelectedPool();

        if (!com.uplexa.androidminer.Config.read("init").equals("1") || pi == null) {
            Toast.makeText(getContext(), "Start mining to view statistics.", Toast.LENGTH_LONG).show();
            enableOnlineStats(false);
            return false;
        }

        if (pi.getPoolType() == 0) {
            Toast.makeText(getContext(), "Statistics are not available for custom pools.", Toast.LENGTH_LONG).show();
            enableOnlineStats(false);
            return false;
        }

        enableOnlineStats(true);

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        ProviderManager.request.setListener(statsListener).start();
    }

    @Override
    public void onPause() {
        super.onPause();
        enableOnlineStats(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        enableOnlineStats(false);
    }
}