package com.longlong.wifiscanner.tool;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class Packer {
    public static void main(String[] args) throws Exception {
        TexturePacker.process("../android/assets/raw", "../android/assets/atlas", "pack");
    }
}
