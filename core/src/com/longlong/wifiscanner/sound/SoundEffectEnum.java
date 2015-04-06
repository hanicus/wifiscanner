package com.longlong.wifiscanner.sound;

public enum SoundEffectEnum {
    COMPLETE("tada.mp3");

    private String fileName;

    private SoundEffectEnum(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return fileName;
    }
}
