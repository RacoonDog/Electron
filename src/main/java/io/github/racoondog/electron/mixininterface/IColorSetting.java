package io.github.racoondog.electron.mixininterface;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface IColorSetting {
    void setDoNotUpdate(boolean newValue);
    boolean getDoNotUpdate();
}
