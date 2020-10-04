package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.methods.IBindingMethod;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.traits.IHasBinding;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CacheUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.ClassUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IThrowingFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThrowableUtilities;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Modifier;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public enum BindingUtilities {
	;
	@SuppressWarnings("UnstableApiUsage")
	private static final LoadingCache<Class<?>, Function<?, ? extends Iterable<IHasBinding>>> HAS_BINDING_VARIABLES_MAP =
			CacheUtilities.newCacheBuilderNormalThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).weakKeys()
					.build(CacheLoader.from(key -> {
						assert key != null;
						ImmutableSet<MethodHandle> variableHandles = ClassUtilities.getAllFields(key).stream().unordered()
								.filter(f -> !Modifier.isStatic(f.getModifiers()) && IHasBinding.class.isAssignableFrom(f.getType()))
								.map(f -> {
									try {
										return InvokeUtilities.IMPL_LOOKUP.unreflectGetter(f);
									} catch (IllegalAccessException e) {
										throw ThrowableUtilities.propagate(e);
									}
								})
								.collect(ImmutableSet.toImmutableSet());
						return object -> {
							ImmutableSet.Builder<IHasBinding> builder = ImmutableSet.builder();
							try {
								variableHandles.stream().unordered()
										.map(IThrowingFunction.executeNow(variableHandle ->
												AssertionUtilities.assertNonnull(variableHandle).invoke(object)))
										.map(IHasBinding.class::cast)
										.forEach(builder::add);
							} catch (Throwable throwable) {
								throw ThrowableUtilities.propagate(throwable);
							}
							return builder.build();
						};
					}));
	@SuppressWarnings("UnstableApiUsage")
	private static final LoadingCache<Class<?>, BiFunction<? super Set<EnumScopeOptions>, ?, ? extends Iterable<IBindingField<?>>>> BINDING_FIELDS_MAP =
			CacheUtilities.newCacheBuilderNormalThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).weakKeys()
					.build(CacheLoader.from(key -> {
						assert key != null;
						ImmutableSet<MethodHandle> bindingHandles = ClassUtilities.getAllFields(key).stream().unordered()
								.filter(f -> !Modifier.isStatic(f.getModifiers()) && IBindingField.class.isAssignableFrom(f.getType()))
								.map(f -> {
									try {
										return InvokeUtilities.IMPL_LOOKUP.unreflectGetter(f);
									} catch (IllegalAccessException e) {
										throw ThrowableUtilities.propagate(e);
									}
								})
								.collect(ImmutableSet.toImmutableSet());
						Function<Object, ImmutableSet<IBindingField<?>>> bindings = object -> {
							ImmutableSet.Builder<IBindingField<?>> builder = ImmutableSet.builder();
							try {
								bindingHandles.stream().unordered()
										.map(IThrowingFunction.executeNow(bindingHandle ->
												AssertionUtilities.assertNonnull(bindingHandle).invoke(object)))
										.map(IBindingField.class::cast)
										.forEach(builder::add);
							} catch (Throwable throwable) {
								throw ThrowableUtilities.propagate(throwable);
							}
							return builder.build();
						};
						return (options, object) -> Streams.concat(
								options.contains(EnumScopeOptions.SELF)
										? bindings.apply(object).stream().unordered()
										: Stream.empty(),
								options.contains(EnumScopeOptions.VARIABLES)
										? Streams.stream(getHasBindingsVariablesFunction(key).apply(CastUtilities.castUnchecked(object))).unordered()
										.flatMap(hb -> Streams.stream(hb.getBindingFields()))
										: Stream.empty())
								.collect(ImmutableSet.toImmutableSet());
					}));
	@SuppressWarnings("UnstableApiUsage")
	private static final LoadingCache<Class<?>, BiFunction<? super Set<EnumScopeOptions>, ?, ? extends Iterable<IBindingMethod<?>>>> BINDING_METHODS_MAP =
			CacheUtilities.newCacheBuilderNormalThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).weakKeys()
					.build(CacheLoader.from(key -> {
						assert key != null;
						ImmutableSet<MethodHandle> bindingHandles = ClassUtilities.getAllFields(key).stream().unordered()
								.filter(f -> !Modifier.isStatic(f.getModifiers()) && IBindingMethod.class.isAssignableFrom(f.getType()))
								.map(f -> {
									try {
										return InvokeUtilities.IMPL_LOOKUP.unreflectGetter(f);
									} catch (IllegalAccessException e) {
										throw ThrowableUtilities.propagate(e);
									}
								})
								.collect(ImmutableSet.toImmutableSet());
						Function<Object, ImmutableSet<IBindingMethod<?>>> bindings = object -> {
							ImmutableSet.Builder<IBindingMethod<?>> builder = ImmutableSet.builder();
							try {
								bindingHandles.stream().unordered()
										.map(IThrowingFunction.executeNow(bindingHandle ->
												AssertionUtilities.assertNonnull(bindingHandle).invoke(object)))
										.map(IBindingMethod.class::cast)
										.forEach(builder::add);
							} catch (Throwable throwable) {
								throw ThrowableUtilities.propagate(throwable);
							}
							return builder.build();
						};
						return (options, object) -> Streams.concat(
								options.contains(EnumScopeOptions.SELF)
										? bindings.apply(object).stream().unordered()
										: Stream.empty(),
								options.contains(EnumScopeOptions.VARIABLES)
										? Streams.stream(getHasBindingsVariablesFunction(key).apply(CastUtilities.castUnchecked(object))).unordered()
										.flatMap(hb -> Streams.stream(hb.getBindingMethods()))
										: Stream.empty())
								.collect(ImmutableSet.toImmutableSet());
					}));

	public static Iterable<IBindingField<?>> getBindingFields(Object object, Set<EnumScopeOptions> options) { return getBindingFieldsFunction(object.getClass(), options).apply(CastUtilities.castUnchecked(object)); }

	public static <T> Function<T, Iterable<IBindingField<?>>> getBindingFieldsFunction(Class<T> clazz, Set<EnumScopeOptions> options) {
		ImmutableSet<EnumScopeOptions> optionsCopy = Sets.immutableEnumSet(options);
		return o -> getBindingFieldsMap().getUnchecked(clazz).apply(optionsCopy, CastUtilities.castUnchecked(o));
	}

	@SuppressWarnings("SameReturnValue")
	private static LoadingCache<Class<?>, BiFunction<? super Set<EnumScopeOptions>, ?, ? extends Iterable<IBindingField<?>>>> getBindingFieldsMap() { return BINDING_FIELDS_MAP; }

	public static Iterable<IBindingMethod<?>> getBindingMethods(Object object, Set<EnumScopeOptions> options) { return getBindingMethodsFunction(object.getClass(), options).apply(CastUtilities.castUnchecked(object)); }

	public static <T> Function<T, Iterable<IBindingMethod<?>>> getBindingMethodsFunction(Class<T> clazz, Set<EnumScopeOptions> options) {
		ImmutableSet<EnumScopeOptions> optionsCopy = Sets.immutableEnumSet(options);
		return o -> getBindingMethodsMap().getUnchecked(clazz).apply(optionsCopy, CastUtilities.castUnchecked(o));
	}

	@SuppressWarnings("SameReturnValue")
	private static LoadingCache<Class<?>, BiFunction<? super Set<EnumScopeOptions>, ?, ? extends Iterable<IBindingMethod<?>>>> getBindingMethodsMap() { return BINDING_METHODS_MAP; }

	public static Iterable<IHasBinding> getHasBindingsVariables(Object object) { return getHasBindingsVariablesFunction(object.getClass()).apply(CastUtilities.castUnchecked(object)); }

	@SuppressWarnings("unchecked")
	public static <T> Function<T, Iterable<IHasBinding>> getHasBindingsVariablesFunction(Class<T> clazz) { return (Function<T, Iterable<IHasBinding>>) getHasBindingVariablesMap().getUnchecked(clazz); }

	@SuppressWarnings("SameReturnValue")
	private static LoadingCache<Class<?>, Function<?, ? extends Iterable<IHasBinding>>> getHasBindingVariablesMap() { return HAS_BINDING_VARIABLES_MAP; }

	public enum EnumScopeOptions {
		SELF,
		VARIABLES,
		;

		private static final ImmutableSet<EnumScopeOptions> ALL = Sets.immutableEnumSet(EnumSet.allOf(EnumScopeOptions.class));

		public static ImmutableSet<EnumScopeOptions> getAll() { return ALL; }
	}
}
