package com.longlong.wifiscanner.dao;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class DAO {
    private final Preferences preferences;

    public DAO() {
        preferences = Gdx.app.getPreferences("WiFiScanner");
    }

    public void put(String key, String value) {
        preferences.putString(key, value);
        preferences.flush();
    }

    public String get(String key) {
        return preferences.getString(key, "");
    }
}
