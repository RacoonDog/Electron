package io.github.racoondog.electron;

import io.github.racoondog.electron.blockiterator.ChunkBlockIterator;
import io.github.racoondog.meteorsharedaddonutils.features.ColoredWindow;
import io.github.racoondog.meteorsharedaddonutils.mixin.ISystems;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.addons.AddonManager;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.tabs.Tab;
import meteordevelopment.meteorclient.gui.tabs.TabScreen;
import meteordevelopment.meteorclient.gui.tabs.Tabs;
import meteordevelopment.meteorclient.gui.utils.Cell;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.gui.widgets.containers.WWindow;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.System;
import meteordevelopment.meteorclient.systems.Systems;
import meteordevelopment.meteorclient.utils.PreInit;
import meteordevelopment.meteorclient.utils.misc.NbtUtils;
import meteordevelopment.meteorclient.utils.world.BlockIterator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.NbtCompound;

@Environment(EnvType.CLIENT)
public final class ElectronSystem extends System<ElectronSystem> {
    public final Settings settings = new Settings();

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgMultithreading = settings.createGroup("Multithreading");
    private final SettingGroup sgStarscript = settings.createGroup("Starscript");

    public final Setting<Boolean> chunkBlockIterator = sgGeneral.add(new BoolSetting.Builder()
        .name("chunk-block-iterator")
        .description("Replaces the BlockIterator with an optimized version.")
        .defaultValue(true)
        .onChanged(o -> {
            if (o) {
                MeteorClient.EVENT_BUS.unsubscribe(BlockIterator.class);
                MeteorClient.EVENT_BUS.subscribe(ChunkBlockIterator.class);
            } else {
                MeteorClient.EVENT_BUS.unsubscribe(ChunkBlockIterator.class);
                MeteorClient.EVENT_BUS.subscribe(BlockIterator.class);
            }
        })
        .build()
    );

    public final Setting<Boolean> asynchronousAddonInit = sgMultithreading.add(new BoolSetting.Builder()
        .name("async-addon-init")
        .description("Initializes multiple addons asynchronously. Requires restart")
        .defaultValue(true)
        .build()
    );

    public final Setting<Integer> addonInitThreads = sgMultithreading.add(new IntSetting.Builder()
        .name("addon-init-threads")
        .description("Amount of threads to use during asynchronous addon initializing.")
        .range(1, Math.min(Thread.activeCount(), AddonManager.ADDONS.size()))
        .defaultValue(Math.min(4, Math.min(Thread.activeCount(), AddonManager.ADDONS.size())))
        .visible(asynchronousAddonInit::get)
        .build()
    );

    public final Setting<Boolean> starscript = sgStarscript.add(new BoolSetting.Builder()
        .name("starscript-optimizations")
        .description("Enables starscript optimizations.")
        .defaultValue(true)
        .build()
    );

    public final Setting<Boolean> nullOnError = sgStarscript.add(new BoolSetting.Builder()
        .name("null-on-error")
        .description("Makes compiler turn errors into Nulls during script compilation.")
        .defaultValue(true)
        .visible(starscript::get)
        .build()
    );

    public final Setting<Boolean> ignoreSections = sgStarscript.add(new BoolSetting.Builder()
        .name("ignore-sections")
        .description("Makes compiler ignore sections.")
        .defaultValue(false)
        .visible(starscript::get)
        .build()
    );

    public final Setting<Boolean> nullPropagation = sgStarscript.add(new BoolSetting.Builder()
        .name("null-propagation")
        .description("Makes operations return null if they are given a null parameter.")
        .defaultValue(false)
        .visible(starscript::get)
        .build()
    );

    private ElectronSystem() {
        super("Electron");

        if (chunkBlockIterator.get()) {
            MeteorClient.EVENT_BUS.subscribe(ChunkBlockIterator.class);
            MeteorClient.EVENT_BUS.unsubscribe(BlockIterator.class);
        }
    }

    public static ElectronSystem get() {
        return Systems.get(ElectronSystem.class);
    }

    @PreInit
    public static void registerSystem() {
        ISystems.invokeAdd(new ElectronSystem());
        Tabs.add(new ElectronTab());
    }

    @Override
    public NbtCompound toTag() {
        NbtCompound tag = new NbtCompound();

        tag.put("settings", settings.toTag());

        return tag;
    }

    @Override
    public ElectronSystem fromTag(NbtCompound tag) {
        if (tag.contains("settings")) settings.fromTag(tag.getCompound("settings"));

        return this;
    }

    private static final class ElectronTab extends Tab {

        private ElectronTab() {
            super("Electron");
        }

        @Override
        public TabScreen createScreen(GuiTheme theme) {
            return new ElectronScreen(theme, this);
        }

        @Override
        public boolean isScreen(Screen screen) {
            return screen instanceof ElectronScreen;
        }
    }

    private static final class ElectronScreen extends TabScreen {
        private final Settings settings;
        private final WWindow window;

        private ElectronScreen(GuiTheme theme, Tab tab) {
            super(theme, tab);

            window = super.add(new ColoredWindow(null, tab.name, 255, 201, 58)).center().widget();

            settings = ElectronSystem.get().settings;
            settings.onActivated();
        }

        @Override
        public <W extends WWidget> Cell<W> add(W widget) {
            return window.add(widget);
        }

        @Override
        public void clear() {
            window.clear();
        }

        @Override
        public void initWidgets() {
            add(theme.settings(settings)).expandX();
        }

        @Override
        public boolean toClipboard() {
            return NbtUtils.toClipboard(ElectronSystem.get());
        }

        @Override
        public boolean fromClipboard() {
            return NbtUtils.fromClipboard(ElectronSystem.get());
        }
    }
}
