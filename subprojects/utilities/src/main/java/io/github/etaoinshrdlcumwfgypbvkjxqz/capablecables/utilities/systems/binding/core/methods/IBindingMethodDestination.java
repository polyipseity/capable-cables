package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.methods;

import java.util.function.Consumer;

public interface IBindingMethodDestination<T>
		extends IBindingMethod<T>, Consumer<T> {
	@Override
	default EnumMethodType getMethodType() { return EnumMethodType.DESTINATION; }

	@Override
	void accept(T argument);
}