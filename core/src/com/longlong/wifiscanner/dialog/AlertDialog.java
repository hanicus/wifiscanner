package com.longlong.wifiscanner.dialog;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.longlong.wifiscanner.util.Assets;
import com.longlong.wifiscanner.util.Utils;

public class AlertDialog extends BaseDialog {
    private int dialogWidth = 500;
    private int dialogHeight = 300;
    private int titleHeight = 50;
    private Label titleLabel;
    private Label contentLabel;
    private TextButton okButton;
    private Runnable onCloseRunnable;

    public AlertDialog(Assets assets) {
        this(assets, null);
    }

    public AlertDialog(Assets assets, Runnable onCloseRunnable) {
        super(assets);
        this.onCloseRunnable = onCloseRunnable;
        contentLabel = addActorAndCenterWithSize(new Label(
            "",
            assets.getSkin(),
            "alert_dialog_content"), dialogWidth, dialogHeight);
        contentLabel.setAlignment(Align.center);
        contentLabel.moveBy(0, 0);

        titleLabel = addActorAndCenterWithSize(
            new Label("", assets.getSkin(), "alert_dialog_title"),
            dialogWidth,
            titleHeight);
        titleLabel.setAlignment(Align.center);
        titleLabel.setWrap(true);
        titleLabel.moveBy(0, dialogHeight / 2 + titleHeight / 2 - 10);

        okButton = addActorAndCenterWithSize(new TextButton(
            "Close",
            assets.getSkin(),
            "golden_ring_small"), 80, 80);
        okButton.getLabel().setFontScale(0.2f);
        okButton.moveBy(dialogWidth / 2 - 15, -dialogHeight / 2 + 15);
        okButton.addListener(changeListener);
    }

    public AlertDialog setTitle(String title) {
        Utils.updateLabelWithCenterText(titleLabel, title, 0.6f);
        return this;
    }

    public AlertDialog setContent(String content) {
        Utils.updateLabelWithCenterText(contentLabel, content, 0.2f);
        return this;
    }

    private ChangeListener changeListener = new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            if (okButton == actor) {
                dismiss();
                if (onCloseRunnable != null) {
                    onCloseRunnable.run();
                }
            }
        }
    };
}
