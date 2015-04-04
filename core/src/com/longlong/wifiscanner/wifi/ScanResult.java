package com.longlong.wifiscanner.wifi;

public class ScanResult {

    public String BSSID;
    public String SSID;
    public int RSSI;

    public ScanResult(String BSSID, String SSID, int RSSI) {
        this.BSSID = BSSID;
        this.SSID = SSID;
        this.RSSI = RSSI;
    }

    @Override
    public String toString() {
        return "ScanResult [BSSID=" + BSSID + ", SSID=" + SSID + ", RSSI=" + RSSI + "]";
    }
}
