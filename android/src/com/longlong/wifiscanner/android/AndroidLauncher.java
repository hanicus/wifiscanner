package com.longlong.wifiscanner.android;

import android.net.wifi.WifiManager;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.longlong.wifiscanner.AppMain;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(
            new AppMain(new WifiScanner((WifiManager) getSystemService(WIFI_SERVICE))),
            config);
    }
}
