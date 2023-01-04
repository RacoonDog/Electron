package io.github.racoondog.electron;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ElectronMixinPlugin implements IMixinConfigPlugin {
    public static final List<String> SETTINGS = new ArrayList<>();

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        for (var pkg : SETTINGS) {
            if (mixinClassName.startsWith(pkg)) return false;
        }
        return true;
    }

    @Override public void onLoad(String mixinPackage) {}
    @Override public String getRefMapperConfig() {return null;}
    @Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}
    @Override public List<String> getMixins() {return null;}
    @Override public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
    @Override public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
