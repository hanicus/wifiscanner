package com.longlong.wifiscanner;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.longlong.wifiscanner.util.Assets;
import com.longlong.wifiscanner.wifi.ScanResult;
import com.longlong.wifiscanner.wifi.WifiScannerAdapter;

public class AppMain extends ApplicationAdapter {
    private Assets assets;
    private final WifiScannerAdapter wifiScannerAdapter;
    SpriteBatch batch;
    Texture img;

    public AppMain() {
        this(null);
    }

    public AppMain(WifiScannerAdapter wifiScannerAdapter) {
        this.wifiScannerAdapter = wifiScannerAdapter;
    }

    @Override
    public void create() {
        assets = new Assets();
        batch = new SpriteBatch();
        Gdx.app.log("WifiScanner", "Start Scanning WIFI!!!!!!!!!!!!!!!");
        for (ScanResult scanResult : wifiScannerAdapter.getScanResults()) {
            Gdx.app.log("WifiScanner", scanResult.toString());
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
    }
}
