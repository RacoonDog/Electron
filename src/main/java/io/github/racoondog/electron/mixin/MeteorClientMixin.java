package io.github.racoondog.electron.mixin;

import io.github.racoondog.electron.utils.AddonInitThreading;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
@Mixin(value = MeteorClient.class, remap = false)
public abstract class MeteorClientMixin {
    /**
     * Replaces normal sequential addon initialization with multithreaded implementation.
     *
     * @author Crosby
     */
    @Redirect(method = "onInitializeClient", at = @At(value = "INVOKE", target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V", ordinal = 1))
    private void multithreadedAddonInit(List<MeteorAddon> instance, Consumer<MeteorAddon> consumer) {
        try {
            AddonInitThreading.initializeAddons();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
