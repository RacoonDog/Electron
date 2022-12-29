package io.github.racoondog.electron.mixin.mixin.unstream;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import meteordevelopment.meteorclient.systems.commands.arguments.ModuleArgumentType;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.command.CommandSource.suggestMatching;

@Environment(EnvType.CLIENT)
@Mixin(value = ModuleArgumentType.class, remap = false)
public abstract class ModuleArgumentTypeMixin {
    /**
     * @author Crosby
     * @reason Replace stream with iterative approach, removes stream creation overhead.
     * @since 0.2.8
     */
    @Overwrite
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return suggestMatching(Modules.get().getAll(), builder, module -> module.name, o -> null);
    }
}
