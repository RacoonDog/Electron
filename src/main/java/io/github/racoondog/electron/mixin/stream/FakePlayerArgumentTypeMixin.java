package io.github.racoondog.electron.mixin.stream;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import meteordevelopment.meteorclient.systems.commands.arguments.FakePlayerArgumentType;
import meteordevelopment.meteorclient.utils.entity.fakeplayer.FakePlayerEntity;
import meteordevelopment.meteorclient.utils.entity.fakeplayer.FakePlayerManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.command.CommandSource.suggestMatching;

@Environment(EnvType.CLIENT)
@Mixin(value = FakePlayerArgumentType.class, remap = false)
public abstract class FakePlayerArgumentTypeMixin {
    /**
     * @author Crosby
     * @reason Replace stream with iterative approach, removes stream creation overhead.
     * @since 0.2.8
     */
    @Overwrite
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return suggestMatching(FakePlayerManager.getFakePlayers(), builder, FakePlayerEntity::getEntityName, o -> null);
    }
}
