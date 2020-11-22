package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.reactivex.rxjava3.observers.DisposableObserver;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Optional;
import java.util.function.Supplier;

public interface IHasBinding {
	@OverridingMethodsMustInvokeSuper
	default void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		// COMMENT for 'OverridingMethodsMustInvokeSuper'
	}

	@OverridingMethodsMustInvokeSuper
	default void cleanupBindings() {
		// COMMENT for 'OverridingMethodsMustInvokeSuper'
	}
}
