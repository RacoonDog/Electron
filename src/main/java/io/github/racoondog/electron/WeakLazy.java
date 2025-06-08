package io.github.racoondog.electron;

import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.function.Supplier;

/**
 * A weakly synchronized lazy initialization value that tries, but does not guarantee strict cross-thread
 * synchronization.
 * @param <T> value type
 */
public final class WeakLazy<T> {
    private static final VarHandle HANDLE;
    private final Supplier<T> supplier;
    @SuppressWarnings("FieldMayBeFinal")
    private @Nullable T value = null;

    static {
        try {
            HANDLE = MethodHandles.lookup().findVarHandle(WeakLazy.class, "value", Object.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public WeakLazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @SuppressWarnings("unchecked")
    public T get() {
        T local = (T) HANDLE.getOpaque(this);

        if (local == null) {
            T computed = supplier.get();

            HANDLE.setRelease(this, computed);

            return computed;
        }

        return local;
    }
}
