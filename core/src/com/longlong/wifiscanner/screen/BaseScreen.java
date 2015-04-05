package com.longlong.wifiscanner.screen;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.longlong.wifiscanner.stage.BaseStage;
import com.longlong.wifiscanner.util.Assets;

public abstract class BaseScreen extends ScreenAdapter {
    protected Assets assets;
    protected List<BaseStage> stages = new LinkedList<>();

    public BaseScreen(Assets assets) {
        this.assets = assets;
    }

    @Override
    public void show() {
        super.show();
        updateInputProcessor();
    }

    @Override
    public void resume() {
        super.resume();
        updateInputProcessor();
        for (BaseStage stage : stages) {
            stage.resume();
        }
    }

    public <T extends BaseStage> T addStage(T stage) {
        this.stages.add(stage);
        updateInputProcessor();
        return stage;
    }

    public <T extends BaseStage> void removeStage(T stage) {
        updateInputProcessor();
        this.stages.remove(stage);
    }

    private void updateInputProcessor() {
        if (!stages.isEmpty()) {
            Gdx.input.setInputProcessor(stages.get(stages.size() - 1));
        }
    }

    @Override
    public void render(float tpf) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        for (BaseStage stage : stages) {
            stage.act(Gdx.graphics.getDeltaTime());
            // must call viewport.update(). See
            // https://github.com/libgdx/libgdx/issues/1661
            stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        for (BaseStage stage : stages) {
            stage.resize(width, height);
        }
    }
}
