package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.impl;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.core.ICompositeOptionalValues;

import java.util.Iterator;
import java.util.function.Supplier;

public final class Optional2<V1, V2>
		extends AbstractCompositeOptional<Optional2<V1, V2>, Optional2.Values<V1, V2>> {
	private static final Optional2<?, ?> EMPTY = new Optional2<>();
	private final Values<V1, V2> values;

	private Optional2(V1 value1, V2 value2) {
		this.values = new Values<>(value1, value2);
	}

	private Optional2() {
		this.values = new Values<>(null, null);
	}

	public static <V1, V2> Optional2<V1, V2> of(Supplier<@Nullable ? extends V1> value1Supplier,
	                                            Supplier<@Nullable ? extends V2> value2Supplier) {
		@Nullable V1 value1;
		@Nullable V2 value2;
		return (value1 = value1Supplier.get()) == null || (value2 = value2Supplier.get()) == null
				? getEmpty()
				: new Optional2<>(value1, value2);
	}

	@SuppressWarnings("unchecked")
	public static <V1, V2> Optional2<V1, V2> getEmpty() { return (Optional2<V1, V2>) EMPTY; }

	@Override
	protected Optional2<V1, V2> getThis() { return this; }

	@Override
	protected Optional2<V1, V2> getStaticEmpty() { return getEmpty(); }

	@Override
	protected Values<V1, V2> getValues() { return values; }

	public static final class Values<V1, V2>
			implements ICompositeOptionalValues {
		@Nullable
		private final V1 value1;
		@Nullable
		private final V2 value2;

		private Values(@Nullable V1 value1, @Nullable V2 value2) {
			this.value1 = value1;
			this.value2 = value2;
		}

		public V1 getValue1Nonnull() { return AssertionUtilities.assertNonnull(getValue1()); }

		@Nullable
		protected V1 getValue1() { return value1; }

		public V2 getValue2Nonnull() { return AssertionUtilities.assertNonnull(getValue2()); }

		@Nullable
		protected V2 getValue2() { return value2; }

		@Override
		public Iterator<? extends Supplier<@Nullable ?>> getSuppliers() {
			return ImmutableList.<Supplier<@Nullable ?>>of(this::getValue1, this::getValue2).iterator();
		}
	}
}
