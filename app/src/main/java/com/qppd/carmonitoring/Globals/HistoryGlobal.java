package com.qppd.carmonitoring.Globals;

import com.qppd.carmonitoring.Classes.History;

public class HistoryGlobal {

    private static String history_key;
    private static History history;

    public static String getHistory_key() {
        return history_key;
    }

    public static void setHistory_key(String history_key) {
        HistoryGlobal.history_key = history_key;
    }

    public static History getHistory() {
        return history;
    }

    public static void setHistory(History history) {
        HistoryGlobal.history = history;
    }
}
