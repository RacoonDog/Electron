package io.github.racoondog.electron.mixin.stream;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import meteordevelopment.meteorclient.systems.commands.arguments.ProfileArgumentType;
import meteordevelopment.meteorclient.systems.profiles.Profiles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.command.CommandSource.suggestMatching;

@Environment(EnvType.CLIENT)
@Mixin(value = ProfileArgumentType.class, remap = false)
public abstract class ProfileArgumentTypeMixin {
    /**
     * @author Crosby
     * @reason Replace stream with iterative approach, removes stream creation overhead.
     * @since 0.2.8
     */
    @Overwrite
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return suggestMatching(Profiles.get(), builder, profile -> profile.name.get(), o -> null);
    }
}
