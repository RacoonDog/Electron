package io.github.racoondog.electron.utils;

import io.github.racoondog.electron.mixin.ISystems;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.gui.tabs.Tab;
import meteordevelopment.meteorclient.gui.tabs.Tabs;
import meteordevelopment.meteorclient.systems.System;
import meteordevelopment.meteorclient.systems.commands.Command;
import meteordevelopment.meteorclient.systems.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.List;

@Environment(EnvType.CLIENT)
public final class ThreadUtils {
    public static boolean initLock = false;
    public static final List<HudElementInfo<?>> HEI_QUEUE = ObjectLists.synchronize(new ObjectArrayList<>());
    public static final List<Module> MODULE_QUEUE = ObjectLists.synchronize(new ObjectArrayList<>());
    public static final List<Command> COMMAND_QUEUE = ObjectLists.synchronize(new ObjectArrayList<>());
    public static final List<System<?>> SYSTEM_QUEUE = ObjectLists.synchronize(new ObjectArrayList<>());
    public static final List<GuiTheme> THEME_QUEUE = ObjectLists.synchronize(new ObjectArrayList<>());
    public static final List<Tab> TAB_QUEUE = ObjectLists.synchronize(new ObjectArrayList<>());

    public static void registerObjects() {
        HEI_QUEUE.forEach(info -> Hud.get().register(info));
        HEI_QUEUE.clear();

        MODULE_QUEUE.forEach(module -> Modules.get().add(module));
        MODULE_QUEUE.clear();

        COMMAND_QUEUE.forEach(command -> Commands.get().add(command));
        COMMAND_QUEUE.clear();

        SYSTEM_QUEUE.forEach(ISystems::invokeAdd);
        SYSTEM_QUEUE.clear();

        THEME_QUEUE.forEach(GuiThemes::add);
        THEME_QUEUE.clear();

        TAB_QUEUE.forEach(Tabs::add);
        TAB_QUEUE.clear();
    }
}
