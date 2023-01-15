package io.github.racoondog.electron.utils;

import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.game.GameJoinedEvent;
import meteordevelopment.meteorclient.utils.PreInit;
import meteordevelopment.orbit.EventHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.util.StringHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Crosby
 * @since 0.3.5
 */
@Environment(EnvType.CLIENT)
public final class NameUtils {
    private static final Map<Entity, String> entityNames = new HashMap<>(16);

    @PreInit
    public static void init() {
        MeteorClient.EVENT_BUS.subscribe(NameUtils.class);
    }

    @EventHandler
    private static void onGameJoined(GameJoinedEvent event) {
        entityNames.clear();
    }

    public static String get(Entity entity) {
        return entityNames.computeIfAbsent(entity, entity1 -> StringHelper.stripTextFormat(entity1.getName().getString()));
    }
}
