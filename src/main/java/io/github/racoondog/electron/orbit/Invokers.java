package io.github.racoondog.electron.orbit;

import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.events.render.Render3DEvent;

public final class Invokers {
    public static final OrbitInvoker<Render2DEvent> RENDER_2D_INVOKER = OrbitInvoker.of(MeteorClient.EVENT_BUS, Render2DEvent.class);
    //public static final OrbitInvoker<Render3DEvent> RENDER_3D_INVOKER = OrbitInvoker.of(MeteorClient.EVENT_BUS, Render3DEvent.class);
}
