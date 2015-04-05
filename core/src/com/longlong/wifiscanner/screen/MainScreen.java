package com.longlong.wifiscanner.screen;

import com.longlong.wifiscanner.stage.MainStage;
import com.longlong.wifiscanner.util.Assets;

public class MainScreen extends BaseScreen {
    public MainScreen(Assets assets) {
        super(assets);
        addStage(new MainStage(assets));
    }
}
