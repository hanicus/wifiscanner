package com.longlong.wifiscanner.component;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AfterAction;
import com.longlong.wifiscanner.util.Assets;
import com.longlong.wifiscanner.util.Utils;

public abstract class BaseComponent extends Group {
    protected final Assets assets;

    public BaseComponent(Assets assets) {
        this.assets = assets;
    }

    public <V extends Actor> V addActorAndCenter(V actor) {
        Utils.center(actor);
        super.addActor(actor);
        return actor;
    }

    public <V extends Actor> V addActorAndCenterWithSize(V actor, Vector2 size) {
        return addActorAndCenterWithSize(actor, size.x, size.y);
    }

    public <V extends Actor> V addActorAndCenterWithSize(V actor, float width, float height) {
        actor.setSize(width, height);
        Utils.center(actor);
        super.addActor(actor);
        return actor;
    }

    /**
     * Queue an action to be executed after all currently actions are complete.
     * 
     * @param action
     *            perform action after all currently existed actions are
     *            complete.
     */
    protected void queueAction(Action action) {
        AfterAction afterAction = Actions.after(action);
        // must call setTarget BEFORE addAction
        // AfterAction: Executes an action only after all other actions on the
        // actor *at the time this action's target was set* have finished.
        afterAction.setTarget(this);
        addAction(afterAction);
    }
}
