package com.longlong.wifiscanner.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
    private final static String ATLAS_ASSET_FILE_NAME = "atlas/pack.atlas";
    private final static String SKIN_ASSET_FILE_NAME = "skin/skin.json";
    private AssetManager assetManager;
    private TextureAtlas atlas;
    private Skin skin;

    public Assets() {
        this.assetManager = new AssetManager();
        assetManager.load(ATLAS_ASSET_FILE_NAME, TextureAtlas.class);
        assetManager.finishLoading();
        atlas = assetManager.get(ATLAS_ASSET_FILE_NAME, TextureAtlas.class);
        skin = new Skin(Gdx.files.internal(SKIN_ASSET_FILE_NAME), atlas);
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public Skin getSkin() {
        return skin;
    }

    public AssetManager getAssets() {
        return assetManager;
    }
}
