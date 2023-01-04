package io.github.racoondog.electron.mixin.stream;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import meteordevelopment.meteorclient.systems.commands.arguments.PlayerArgumentType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.concurrent.CompletableFuture;

import static meteordevelopment.meteorclient.MeteorClient.mc;
import static net.minecraft.command.CommandSource.suggestMatching;

@Environment(EnvType.CLIENT)
@Mixin(value = PlayerArgumentType.class, remap = false)
public abstract class PlayerArgumentTypeMixin {
    /**
     * @author Crosby
     * @reason Replace stream with iterative approach, removes stream creation overhead.
     * @since 0.2.8
     */
    @Overwrite
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return suggestMatching(mc.world.getPlayers(), builder, PlayerEntity::getEntityName, o -> null);
    }
}
