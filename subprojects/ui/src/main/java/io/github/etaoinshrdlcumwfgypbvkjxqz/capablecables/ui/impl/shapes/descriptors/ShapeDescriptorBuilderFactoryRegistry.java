package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptorBuilderFactory;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ConcurrencyUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.AbstractRegistryWithDefaults;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.RegistryObject;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Supplier;

public final class ShapeDescriptorBuilderFactoryRegistry
		extends AbstractRegistryWithDefaults<INamespacePrefixedString, IShapeDescriptorBuilderFactory> {
	private static final Supplier<ShapeDescriptorBuilderFactoryRegistry> INSTANCE = Suppliers.memoize(ShapeDescriptorBuilderFactoryRegistry::new);
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());
	private static final ImmutableMap<INamespacePrefixedString, Supplier<RegistryObject<? extends IShapeDescriptorBuilderFactory>>> DEFAULTS_SUPPLIER = ImmutableMap.<INamespacePrefixedString, Supplier<RegistryObject<? extends IShapeDescriptorBuilderFactory>>>builder()
			.put(DefaultShapeDescriptorBuilderFactory.getDefaultFactoryKey(), () -> new RegistryObject<>(new DefaultShapeDescriptorBuilderFactory()))
			.build();
	private static final long serialVersionUID = 6379579158201410218L;

	private final Map<INamespacePrefixedString, RegistryObject<? extends IShapeDescriptorBuilderFactory>> defaults =
			ImmutableMap.copyOf(Maps.transformValues(getDefaultsSupplier(), Supplier::get));

	private ShapeDescriptorBuilderFactoryRegistry() {
		super(true, UIConfiguration.getInstance().getLogger(),
				builder -> builder
						.concurrencyLevel(ConcurrencyUtilities.getNormalThreadThreadCount())
						.initialCapacity(CapacityUtilities.getInitialCapacitySmall()));
	}

	public static DefaultShapeDescriptorBuilderFactory getDefaultFactory() {
		return (DefaultShapeDescriptorBuilderFactory) getInstance()
				.get(DefaultShapeDescriptorBuilderFactory.getDefaultFactoryKey())
				.orElseThrow(InternalError::new)
				.getValue();
	}

	public static ShapeDescriptorBuilderFactoryRegistry getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }

	protected static ImmutableMap<INamespacePrefixedString, Supplier<RegistryObject<? extends IShapeDescriptorBuilderFactory>>> getDefaultsSupplier() { return DEFAULTS_SUPPLIER; }

	@Override
	public <VL extends IShapeDescriptorBuilderFactory> RegistryObject<VL> register(INamespacePrefixedString key, VL value) {
		if (DefaultShapeDescriptorBuilderFactory.getDefaultFactoryKey().equals(key))
			throw new IllegalStateException(
					new LogMessageBuilder()
							.addMarkers(UIMarkers.getInstance()::getMarkerShape)
							.addKeyValue("key", key).addKeyValue("value", value)
							.addMessages(() -> getResourceBundle().getString("register.default.irreplaceable"))
							.build()
			);
		return super.register(key, value);
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	@Override
	protected Map<INamespacePrefixedString, RegistryObject<? extends IShapeDescriptorBuilderFactory>> getDefaults() { return defaults; }
}
