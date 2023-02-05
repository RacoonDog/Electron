package io.github.racoondog.electron.mixin.required;

import io.github.racoondog.electron.config.ElectronConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.crash.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(CrashReport.class)
public abstract class CrashReportMixin {
    /**
     * @author Crosby
     * @since 0.3.1
     */
    @Inject(method = "addStackTrace", at = @At("TAIL"))
    private void appendSettingsToStackTrace(StringBuilder sb, CallbackInfo ci) {
        if (ElectronConfig.SETTINGS.isEmpty()) return;

        sb.append("\n\n-- Electron --\n\n");
        sb.append("Disabled mixins:\n");

        for (var setting : ElectronConfig.SETTINGS) {
            sb.append("- ").append(setting).append('\n');
        }
    }
}
