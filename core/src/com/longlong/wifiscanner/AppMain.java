package com.longlong.wifiscanner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.longlong.wifiscanner.model.ScanResult;
import com.longlong.wifiscanner.screen.MainScreen;
import com.longlong.wifiscanner.util.Assets;

public class AppMain extends Game {
    private Assets assets;
    private final WifiScannerAdapter wifiScannerAdapter;

    public AppMain() {
        this(null);
    }

    public AppMain(WifiScannerAdapter wifiScannerAdapter) {
        this.wifiScannerAdapter = wifiScannerAdapter;
    }

    @Override
    public void create() {
        assets = new Assets(wifiScannerAdapter);
        if (wifiScannerAdapter != null) {
            Gdx.app.log("AppMain", "Start Scanning WIFI!");
            for (ScanResult scanResult : wifiScannerAdapter.getScanResults()) {
                Gdx.app.log("AppMain", scanResult.toString());
            }
        }
        setScreen(new MainScreen(assets));
    }
}
