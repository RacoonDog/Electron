package io.github.racoondog.electron.mixininterface;

import meteordevelopment.starscript.Script;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface IStarscript {
    String rawRun(Script script, StringBuilder sb);
}
