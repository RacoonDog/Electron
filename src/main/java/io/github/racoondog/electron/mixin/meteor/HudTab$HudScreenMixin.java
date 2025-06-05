package io.github.racoondog.electron.mixin.meteor;

import io.github.racoondog.electron.mixininterface.IHud;
import meteordevelopment.meteorclient.gui.tabs.builtin.HudTab;
import meteordevelopment.meteorclient.gui.widgets.pressable.WCheckbox;
import meteordevelopment.meteorclient.systems.hud.Hud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = HudTab.HudScreen.class, remap = false)
public class HudTab$HudScreenMixin {
    @Shadow @Final private Hud hud;

    @Inject(method = "lambda$initWidgets$1", at = @At("TAIL"))
    private void callback(WCheckbox active, CallbackInfo ci) {
        ((IHud) hud).electron$syncState();
    }
}
