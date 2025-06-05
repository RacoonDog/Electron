package io.github.racoondog.electron.mixin.meteor;

import com.mojang.brigadier.context.CommandContext;
import io.github.racoondog.electron.mixininterface.IHud;
import meteordevelopment.meteorclient.commands.commands.ToggleCommand;
import meteordevelopment.meteorclient.systems.hud.Hud;
import net.minecraft.command.CommandSource;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ToggleCommand.class, remap = false)
public class ToggleCommandMixin {
    @SuppressWarnings("InvalidInjectorMethodSignature")
    @Inject(
        method = "/lambda\\$build\\$\\d+/",
        at = @At(
            value = "FIELD",
            target = "Lmeteordevelopment/meteorclient/systems/hud/Hud;active:Z",
            opcode = Opcodes.PUTFIELD,
            shift = At.Shift.AFTER
        ),
        require = 5
    )
    private static void inject(CommandContext<CommandSource> context, CallbackInfoReturnable<Integer> cir) {
        ((IHud) Hud.get()).electron$syncState();
    }
}
