package com.longlong.wifiscanner;

import com.badlogic.gdx.Game;
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
        setScreen(new MainScreen(assets));
    }
}
