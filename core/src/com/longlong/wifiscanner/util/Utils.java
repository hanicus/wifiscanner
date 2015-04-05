package com.longlong.wifiscanner.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class Utils {

    public static <T extends Actor> T center(T actor) {
        actor.setOrigin(Align.center);
        actor.setPosition(0, 0, Align.center);
        return actor;
    }

    public static void show(Actor... actors) {
        for (Actor actor : actors) {
            actor.setVisible(true);
        }
    }

    public static void hide(Actor... actors) {
        for (Actor actor : actors) {
            actor.setVisible(false);
        }
    }

    /**
     * Helper function to resize a label's background to fit the font size.
     * 
     * @param label
     * @param content
     * @param fontScale
     * @return
     */
    public static Label updateLabelWithCenterText(Label label, String content, float fontScale) {
        label.setFontScale(fontScale);
        label.setAlignment(Align.center);
        label.setText(content);
        label.setWrap(true);
        label.layout();
        return label;
    }
}
