package com.aeternal.flowingtime.util;

import com.mojang.blaze3d.platform.InputConstants;

public enum FLKeybind {
    CHARGE("fl.key.charge", InputConstants.KEY_V),
    MODE("fl.key.mode", InputConstants.KEY_G);

    public final String keyName;
    public final int defaultKeyCode;

    FLKeybind(String keyName, int defaultKeyCode) {
        this.keyName = keyName;
        this.defaultKeyCode = defaultKeyCode;
    }
}