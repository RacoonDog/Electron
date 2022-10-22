package io.github.racoondog.electron.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@SuppressWarnings("InstantiationOfUtilityClass")
@Environment(EnvType.CLIENT)
public final class PostAddonInitEvent {
    private static final PostAddonInitEvent INSTANCE = new PostAddonInitEvent();

    public static PostAddonInitEvent get() {
        return INSTANCE;
    }
}
