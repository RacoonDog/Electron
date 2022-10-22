package io.github.racoondog.electron.dev;

import io.github.racoondog.electron.Electron;
import io.github.racoondog.electron.blockiterator.ChunkBlockIterator;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.widgets.WLabel;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.gui.widgets.containers.WSection;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;
import meteordevelopment.meteorclient.gui.widgets.containers.WVerticalList;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import meteordevelopment.meteorclient.settings.BlockListSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.world.BlockIterator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.util.List;

@Environment(EnvType.CLIENT)
public class BlockIterators extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    private final Setting<Integer> hRadius = sgGeneral.add(new IntSetting.Builder()
        .name("hRadius")
        .description("Horizontal Radius")
        .range(1, 10)
        .defaultValue(3)
        .build()
    );

    private final Setting<Integer> vRadius = sgGeneral.add(new IntSetting.Builder()
        .name("vRadius")
        .description("Vertical Radius")
        .range(1, 10)
        .defaultValue(3)
        .build()
    );

    private final Setting<List<Block>> blocklist = sgGeneral.add(new BlockListSetting.Builder()
        .name("blocks")
        .description("Blocks to find")
        .defaultValue(Blocks.STONE)
        .build()
    );

    public WLabel bITime;
    private WLabel bIHitsL;
    private int bIHits = 0;
    public WLabel cBITime;
    private WLabel cBIHitsL;
    private int cBIHits = 0;

    public BlockIterators() {
        super(Electron.CATEGORY, "block-iterators", "Test block iterators");
    }

    @Override
    public WWidget getWidget(GuiTheme theme) {
        WVerticalList list = theme.verticalList();

        WSection blockIteratorSection = list.add(theme.section("Block Iterator")).expandX().widget();
        WTable blockIteratorTable = blockIteratorSection.add(theme.table()).expandX().widget();
        bITime = blockIteratorTable.add(theme.label("0 microseconds")).expandX().widget();
        bIHitsL = blockIteratorTable.add(theme.label("0 hits")).expandX().widget();
        WButton blockIteratorButton = blockIteratorTable.add(theme.button("Run Iterator")).expandX().widget();
        blockIteratorButton.action = this::runBlockIterator;

        WSection chunkBlockIteratorSection = list.add(theme.section("ChunkBlock Iterator")).expandX().widget();
        WTable chunkBlockIteratorTable = chunkBlockIteratorSection.add(theme.table()).expandX().widget();
        cBITime = chunkBlockIteratorTable.add(theme.label("0 microseconds")).expandX().widget();
        cBIHitsL = chunkBlockIteratorTable.add(theme.label("0 hits")).expandX().widget();
        WButton chunkBlockIteratorButton = chunkBlockIteratorTable.add(theme.button("Run Iterator")).expandX().widget();
        chunkBlockIteratorButton.action = this::runChunkBlockIterator;

        WButton runAll = list.add(theme.button("Run All")).expandX().widget();
        runAll.action = () -> {
            runBlockIterator();
            runChunkBlockIterator();
        };

        return list;
    }

    private void runBlockIterator() {
        bIHits = 0;
        BlockIterator.register(hRadius.get(), vRadius.get(), (blockPos, blockState) -> {
            if (blocklist.get().contains(blockState.getBlock())) bIHits++;
        });
        BlockIterator.after(() -> bIHitsL.set("%s hits".formatted(bIHits)));
    }

    private void runChunkBlockIterator() {
        cBIHits = 0;
        ChunkBlockIterator.register(hRadius.get(), vRadius.get(), (blockPos, blockState) -> {
            if (blocklist.get().contains(blockState.getBlock())) cBIHits++;
        });
        ChunkBlockIterator.after(() -> cBIHitsL.set("%s hits".formatted(cBIHits)));
    }
}
