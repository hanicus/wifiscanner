package com.longlong.wifiscanner.sound;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

    private final Map<SoundEffectEnum, Sound> soundEffectMap = new HashMap<>();
    private boolean soundEffectOn = true;

    public SoundManager() {
        for (SoundEffectEnum soundEffectEnum : SoundEffectEnum.values()) {
            soundEffectMap.put(
                soundEffectEnum,
                Gdx.audio.newSound(Gdx.files.internal("sound/" + soundEffectEnum)));
        }
    }

    public void playSound(SoundEffectEnum soundEffectEnum) {
        if (!soundEffectOn) {
            return;
        }
        soundEffectMap.get(soundEffectEnum).play();
    }

    public void setSoundEffectOn(boolean isSoundEffectOn) {
        soundEffectOn = isSoundEffectOn;
    }
}
