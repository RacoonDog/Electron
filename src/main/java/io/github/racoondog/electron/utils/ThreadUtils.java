package io.github.racoondog.electron.utils;

import io.github.racoondog.meteorsharedaddonutils.mixin.mixin.ISystems;
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

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Environment(EnvType.CLIENT)
public final class ThreadUtils {
    public static boolean initLock = false;
    public static final Queue<HudElementInfo<?>> HEI_QUEUE = new ConcurrentLinkedQueue<>();
    public static final Queue<Module> MODULE_QUEUE = new ConcurrentLinkedQueue<>();
    public static final Queue<Command> COMMAND_QUEUE = new ConcurrentLinkedQueue<>();
    public static final Queue<System<?>> SYSTEM_QUEUE = new ConcurrentLinkedQueue<>();
    public static final Queue<GuiTheme> THEME_QUEUE = new ConcurrentLinkedQueue<>();
    public static final Queue<Tab> TAB_QUEUE = new ConcurrentLinkedQueue<>();

    public static void registerObjects() {
        HEI_QUEUE.forEach(info -> Hud.get().register(info));
        HEI_QUEUE.clear();

        MODULE_QUEUE.forEach(module -> Modules.get().add(module));
        MODULE_QUEUE.clear();

        COMMAND_QUEUE.forEach(command -> Commands.get().add(command));
        COMMAND_QUEUE.clear();

        if (!SYSTEM_QUEUE.isEmpty()) {
            SYSTEM_QUEUE.forEach(ISystems::invokeAdd);
            SYSTEM_QUEUE.clear();
        }

        if (!THEME_QUEUE.isEmpty()) {
            THEME_QUEUE.forEach(GuiThemes::add);
            THEME_QUEUE.clear();
        }

        if (!TAB_QUEUE.isEmpty()) {
            TAB_QUEUE.forEach(Tabs::add);
            TAB_QUEUE.clear();
        }
    }
}
