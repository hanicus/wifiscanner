package com.longlong.wifiscanner.stage;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.longlong.wifiscanner.util.Assets;
import com.longlong.wifiscanner.util.Utils;

public abstract class BaseStage extends Stage {
    protected String name;
    protected Assets assets;

    public BaseStage(String name, Assets assets, Viewport viewport) {
        super(viewport);
        this.name = name;
        this.assets = assets;
        getCamera().translate(-viewport.getWorldWidth() / 2, -viewport.getWorldHeight() / 2, 0);
    }

    public void resize(int width, int height) {
        getViewport().update(width, height, false);
    }

    public <V extends Actor> V addActorAndCenter(V actor) {
        Utils.center(actor);
        super.addActor(actor);
        return actor;
    }

    public <V extends Actor> V addActorAndCenterWithSize(V actor, int width, int height) {
        actor.setSize(width, height);
        Utils.center(actor);
        super.addActor(actor);
        return actor;
    }

    public void resume() {
    }
}
