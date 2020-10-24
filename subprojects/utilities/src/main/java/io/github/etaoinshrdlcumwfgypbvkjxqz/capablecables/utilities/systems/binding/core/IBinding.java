package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core;

import com.google.common.cache.Cache;
import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ITypeCapture;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.bindings.IBindings;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.bindings.FieldBindings;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.bindings.MethodBindings;

import java.util.function.Function;
import java.util.function.Supplier;

public interface IBinding<T> extends ITypeCapture, IHasBindingKey {
	EnumBindingType getBindingType();

	@SuppressWarnings("UnstableApiUsage")
	@Override
	TypeToken<T> getTypeToken();

	enum EnumBindingType {
		FIELD {
			@Override
			public IBindings<?> createBindings(INamespacePrefixedString bindingKey,
			                                   Supplier<? extends Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<?, ?>>>> transformersSupplier) {
				return new FieldBindings(bindingKey, transformersSupplier);
			}
		},
		METHOD {
			@Override
			public IBindings<?> createBindings(INamespacePrefixedString bindingKey,
			                                   Supplier<? extends Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<?, ?>>>> transformersSupplier) {
				return new MethodBindings(bindingKey, transformersSupplier);
			}
		},
		;

		public abstract IBindings<?> createBindings(INamespacePrefixedString bindingKey,
		                                            Supplier<? extends Cache<? super Class<?>, ? extends Cache<? super Class<?>, ? extends Function<?, ?>>>> transformersSupplier);
	}
}