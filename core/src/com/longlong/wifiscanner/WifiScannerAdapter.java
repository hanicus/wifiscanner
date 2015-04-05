package com.longlong.wifiscanner;

import java.util.List;

import com.longlong.wifiscanner.model.ScanResult;

public interface WifiScannerAdapter {
    List<ScanResult> getScanResults();
}
