package com.aeternal.flowingtime.utils;

import org.lwjgl.input.Keyboard;

public enum FLKeybind
{
    CHARGE("fl.key.charge", Keyboard.KEY_V),
    MODE("fl.key.mode", Keyboard.KEY_G);


    public final String keyName;
    public final int defaultKeyCode;

    FLKeybind(String keyName, int defaultKeyCode)
    {
        this.keyName = keyName;
        this.defaultKeyCode = defaultKeyCode;
    }
}
