package io.github.racoondog.electron.mixin.asyncaddoninit;

import io.github.racoondog.electron.utils.ThreadUtils;
import meteordevelopment.meteorclient.systems.commands.Command;
import meteordevelopment.meteorclient.systems.commands.Commands;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = Commands.class, remap = false)
public abstract class CommandsMixin {
    /**
     * Ensures thread-safety.
     *
     * @author Crosby
     */
    @Inject(method = "add", at = @At("HEAD"), cancellable = true)
    private void addCommandToQueue(Command command, CallbackInfo ci) {
        if (ThreadUtils.initLock) {
            ThreadUtils.COMMAND_QUEUE.add(command);
            ci.cancel();
        }
    }
}
