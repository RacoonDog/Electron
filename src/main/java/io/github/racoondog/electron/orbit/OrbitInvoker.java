package io.github.racoondog.electron.orbit;

import meteordevelopment.orbit.EventBus;
import meteordevelopment.orbit.ICancellable;
import meteordevelopment.orbit.IEventBus;
import meteordevelopment.orbit.listeners.IListener;
import net.lenni0451.reflect.stream.RStream;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public sealed interface OrbitInvoker<T> permits CancellableOrbitInvoker, RegularOrbitInvoker {
    List<IListener> listeners();
    T post(T event);

    default boolean isListening() {
        return !this.listeners().isEmpty();
    }

    @SuppressWarnings({"unchecked", "RedundantCast"})
    static <T> OrbitInvoker<T> of(IEventBus eventBus, Class<T> eventClass) {
        if (!(eventBus instanceof EventBus)) {
            throw new IllegalArgumentException("OrbitInvoker only supports Orbit EventBus!");
        }

        Map<Class<?>, List<IListener>> listenerMap = RStream.of(eventBus).fields()
            .by("listenerMap").get();

        List<IListener> listeners = listenerMap.computeIfAbsent(eventClass, o -> new CopyOnWriteArrayList<>());

        if (ICancellable.class.isAssignableFrom(eventClass)) {
            return (OrbitInvoker<T>) new CancellableOrbitInvoker<>(listeners);
        } else {
            return new RegularOrbitInvoker<>(listeners);
        }
    }
}
