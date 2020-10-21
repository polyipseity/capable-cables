package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.extensions;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ConcurrencyUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.AbstractExtensionRegistry;

import java.util.function.Supplier;

public final class UIExtensionRegistry
		extends AbstractExtensionRegistry<INamespacePrefixedString, IExtensionType<? extends INamespacePrefixedString, ?, ?>> {
	private static final Supplier<UIExtensionRegistry> INSTANCE = Suppliers.memoize(UIExtensionRegistry::new);
	private static final long serialVersionUID = -7931184191525680470L;

	private UIExtensionRegistry() {
		super(true, UIConfiguration.getInstance().getLogger(),
				builder -> builder
						.concurrencyLevel(ConcurrencyUtilities.getNormalThreadThreadCount())
						.initialCapacity(CapacityUtilities.getInitialCapacityMedium()));
	}

	public static UIExtensionRegistry getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }
}
