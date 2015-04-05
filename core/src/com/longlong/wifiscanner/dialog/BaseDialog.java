package com.longlong.wifiscanner.dialog;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.longlong.wifiscanner.component.BaseComponent;
import com.longlong.wifiscanner.util.Assets;

public class BaseDialog extends BaseComponent {
    private Image bg;
    private BaseComponent uiGroup;

    public BaseDialog(Assets assets) {
        super(assets);
        bg = super.addActorAndCenter(new Image(assets.getAtlas().findRegion("black_bg")));
        bg.getColor().a = 0.8f;

        uiGroup = super.addActorAndCenter(new BaseComponent(assets) {
        });
    }

    public void show() {
        final float duration = 0.5f;
        scaleBy(-0.5f);
        queueAction(sequence(run(new Runnable() {
            @Override
            public void run() {
                addAction(Actions.scaleBy(0.5f, 0.5f, duration, Interpolation.swingOut));
            }
        })));
    }

    public void dismiss() {
        final float duration = 0.5f;
        queueAction(sequence(parallel(fadeOut(duration), (run(new Runnable() {
            @Override
            public void run() {
                addAction(Actions.scaleBy(2f, 2f, duration, Interpolation.swingIn));
            }
        }))), Actions.removeActor()));
    }

    @Override
    public <V extends Actor> V addActorAndCenter(V actor) {
        return uiGroup.addActorAndCenter(actor);
    }

    @Override
    public <V extends Actor> V addActorAndCenterWithSize(V actor, float width, float height) {
        return uiGroup.addActorAndCenterWithSize(actor, width, height);
    }

    @Override
    public void addActor(Actor actor) {
        uiGroup.addActor(actor);
    }

    @Override
    public <V extends Actor> V addActorAndCenterWithSize(V actor, Vector2 size) {
        return uiGroup.addActorAndCenterWithSize(actor, size);
    }

    @Override
    public void scaleBy(float scale) {
        uiGroup.scaleBy(scale);
    }

    @Override
    public void scaleBy(float scaleX, float scaleY) {
        uiGroup.scaleBy(scaleX, scaleY);
    }

    @Override
    public void moveBy(float x, float y) {
        uiGroup.moveBy(x, y);
    }

    @Override
    public void act(float delta) {
        float height = getStage().getViewport().getWorldHeight();
        float width = getStage().getViewport().getWorldWidth();

        super.act(delta);

        bg.setBounds(-width / 2 - 50, -height / 2 - 50, width + 100, height + 100);
    }
}
