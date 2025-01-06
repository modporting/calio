package io.github.apace100.calio.data;

import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.Profilers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class ExtendedSinglePreparationResourceReloader<T> extends SinglePreparationResourceReloader<T> {

    @Override
    public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Executor prepareExecutor, Executor applyExecutor) {
        return CompletableFuture.supplyAsync(() -> this.prepare(manager, Profilers.get()), prepareExecutor)
            .thenCompose(synchronizer::whenPrepared)
            .thenAcceptAsync(prepared -> this.processBeforeApply(prepared, manager, Profilers.get()), applyExecutor);
    }

    protected final void processBeforeApply(T prepared, ResourceManager manager, Profiler profiler) {
        this.preApply(prepared, manager, profiler);
        this.apply(prepared, manager, profiler);
    }

    protected void preApply(T prepared, ResourceManager manager, Profiler profiler) {

    }

}
