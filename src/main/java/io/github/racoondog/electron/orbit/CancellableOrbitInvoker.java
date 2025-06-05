package io.github.racoondog.electron.orbit;

import meteordevelopment.orbit.ICancellable;
import meteordevelopment.orbit.listeners.IListener;

import java.util.List;

public record CancellableOrbitInvoker<T extends ICancellable>(List<IListener> listeners) implements OrbitInvoker<T> {
    @Override
    public T post(T event) {
        event.setCancelled(false);

        for (IListener listener : this.listeners()) {
            listener.call(event);
            if (event.isCancelled()) {
                break;
            }
        }

        return event;
    }
}
