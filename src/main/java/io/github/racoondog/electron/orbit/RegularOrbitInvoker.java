package io.github.racoondog.electron.orbit;

import meteordevelopment.orbit.listeners.IListener;

import java.util.List;

public record RegularOrbitInvoker<T>(List<IListener> listeners) implements OrbitInvoker<T> {
    @Override
    public T post(T event) {
        for (IListener listener : this.listeners()) {
            listener.call(event);
        }

        return event;
    }
}
