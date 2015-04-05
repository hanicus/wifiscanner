package com.longlong.wifiscanner.stage;

import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.longlong.wifiscanner.component.WifiScanner;
import com.longlong.wifiscanner.util.Assets;

public class MainStage extends BaseStage {
    public final static int STAGE_WIDTH = 800;
    public final static int STAGE_HEIGHT = 480;

    public MainStage(Assets assets) {
        super(MainStage.class.getSimpleName(), assets, new ExtendViewport(
            STAGE_WIDTH,
            STAGE_HEIGHT,
            0,
            0));
        addActorAndCenter(new WifiScanner(assets));
        setDebugAll(true);
    }
}
