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

package com.uplexa.androidminer.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class ProviderData {
    public static class Network {
        public String lastBlockHeight = "";
        public String difficulty = "";
        public String lastRewardAmount = "";
        public String lastBlockTime = "";
        public String hashrate = "";
    }

    public static class Pool {
        public String miners = "";
        public String lastBlockHeight = "";
        public String difficulty = "";
        public String lastRewardAmount = "";
        public String lastBlockTime = "";
        public String hashrate = "";
        public String blocks = "";
        public String minPayout = "";
        int type = -1;
    }

    public static class Miner {
        public String hashrate = "";
        public String balance = "";
        public String paid = "";
        public String lastShare = "";
        public String blocks = "";
        public List<Payment> payments = new ArrayList<>();
    }

    public static class Coin {
        public String name = "uPlexa";
        public String symbol = "UPX";
        public long units;
        public long denominationUnit = 100L;
    }

    public static class Payment {
        public float amount;
        public Date timestamp;
    }

    final public Network network = new Network();
    final public Pool pool = new Pool();
    final public Coin coin = new Coin();
    final public Miner miner = new Miner();

    public boolean isNew = true;
}
