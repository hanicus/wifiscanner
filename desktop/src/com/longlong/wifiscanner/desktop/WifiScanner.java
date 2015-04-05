package com.longlong.wifiscanner.desktop;

import java.util.LinkedList;
import java.util.List;

import com.longlong.wifiscanner.WifiScannerAdapter;
import com.longlong.wifiscanner.model.ScanResult;

public class WifiScanner implements WifiScannerAdapter {

    @Override
    public List<ScanResult> getScanResults() {
        List<ScanResult> scanResults = new LinkedList<>();
        scanResults.add(new ScanResult("BSSID_1", "SSID_1", -30));
        scanResults.add(new ScanResult("BSSID_2", "SSID_2", 30));
        return scanResults;
    }

}
