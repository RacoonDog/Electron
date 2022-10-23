package io.github.racoondog.electron.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;

/**
 * @since 0.1.0
 * @author Crosby
 */
@SuppressWarnings("InstantiationOfUtilityClass")
@Environment(EnvType.CLIENT)
public final class PostAddonInitEvent {
    private static final PostAddonInitEvent INSTANCE = new PostAddonInitEvent();

    public static @NotNull PostAddonInitEvent get() {
        return INSTANCE;
    }
}
