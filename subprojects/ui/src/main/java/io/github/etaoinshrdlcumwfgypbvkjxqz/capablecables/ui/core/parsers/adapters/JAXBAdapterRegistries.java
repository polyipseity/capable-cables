package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IDuplexFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.Registry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.StandardDuplexFunctionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import jakarta.xml.bind.JAXBElement;

import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;

public enum JAXBAdapterRegistries {
	;

	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());

	public static <L> IDuplexFunction<L, ?> getAdapter(L jaxbObj) {
		return findAdapter(jaxbObj)
				.orElseThrow(() ->
						new IllegalArgumentException(
								new LogMessageBuilder()
										.addMarkers(UIMarkers.getInstance()::getMarkerJAXB)
										.addKeyValue("jaxbObj", jaxbObj)
										.addMessages(() -> getResourceBundle().getString("adapter.find.not_found"))
										.build()
						));
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	@SuppressWarnings({"unchecked", "rawtypes", "RedundantSuppression"})
	public static <L> Optional<? extends IDuplexFunction<L, ?>> findAdapter(L jaxbObj) {
		if (jaxbObj instanceof JAXBElement)
			return (Optional<? extends IDuplexFunction<L, ?>>) // COMMENT should be safe
					Element.INSTANCE.getSafe(((JAXBElement<?>) jaxbObj).getDeclaredType())
							.map(Registry.RegistryObject::getValue);
		return (Optional<? extends IDuplexFunction<L, ?>>) // COMMENT should be safe
				Object.INSTANCE.getSafe(jaxbObj.getClass())
						.map(Registry.RegistryObject::getValue);
	}

	public static final class Object
			extends StandardDuplexFunctionRegistry {
		public static final Object INSTANCE = new Object();

		protected Object() {
			super(true, UIConfiguration.getInstance().getLogger());
		}
	}

	public static final class Element
			extends Registry<Class<?>, IDuplexFunction<JAXBElement<?>, ?>> {
		public static final Element INSTANCE = new Element();

		protected Element() {
			super(true, UIConfiguration.getInstance().getLogger());
		}

		public static Element getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }

		public <L, VL extends IDuplexFunction<JAXBElement<L>, ?>> RegistryObject<VL> registerSafe(Class<L> key, VL value) {
			return CastUtilities.castUnchecked(register(key, CastUtilities.castUnchecked(value))); // COMMENT should be safe
		}

		public <L, VL extends IDuplexFunction<JAXBElement<L>, ?>> RegistryObject<VL> registerApplySafe(Class<L> key, Function<? super Class<L>, ? extends VL> value) {
			return CastUtilities.castUnchecked(registerApply(key, CastUtilities.castUnchecked(value))); // COMMENT should be safe
		}

		@Override
		@Deprecated
		public <VL extends IDuplexFunction<JAXBElement<?>, ?>> RegistryObject<VL> registerApply(Class<?> key, Function<? super Class<?>, ? extends VL> value) { return super.registerApply(key, value); }

		@Override
		@Deprecated
		public <VL extends IDuplexFunction<JAXBElement<?>, ?>> RegistryObject<VL> register(Class<?> key, VL value) { return super.register(key, value); }

		@Override
		@Deprecated
		public Optional<? extends RegistryObject<? extends IDuplexFunction<JAXBElement<?>, ?>>> get(Class<?> key) { return super.get(key); }

		public <L> Optional<? extends RegistryObject<? extends IDuplexFunction<JAXBElement<L>, ?>>> getSafe(Class<L> key) {
			return CastUtilities.castUnchecked(get(key)); // COMMENT should be safe
		}
	}
}
