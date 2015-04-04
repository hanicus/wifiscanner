package com.longlong.wifiscanner.android;

import java.util.LinkedList;
import java.util.List;

import android.net.wifi.WifiManager;

import com.longlong.wifiscanner.wifi.ScanResult;
import com.longlong.wifiscanner.wifi.WifiScannerAdapter;

public class WifiScanner implements WifiScannerAdapter {

    private WifiManager wifiManager;

    public WifiScanner(WifiManager wifiManager) {
        this.wifiManager = wifiManager;
    }

    @Override
    public List<ScanResult> getScanResults() {
        List<ScanResult> scanResults = new LinkedList<>();
        for (android.net.wifi.ScanResult androidScanResult : wifiManager.getScanResults()) {
            scanResults.add(new ScanResult(
                androidScanResult.BSSID,
                androidScanResult.SSID,
                androidScanResult.level));
        }
        return scanResults;
    }

}
