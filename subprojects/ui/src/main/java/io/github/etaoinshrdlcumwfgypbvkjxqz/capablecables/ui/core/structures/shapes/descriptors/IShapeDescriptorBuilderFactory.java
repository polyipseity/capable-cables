package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import org.jetbrains.annotations.NonNls;

import java.awt.*;

public interface IShapeDescriptorBuilderFactory {
	<S extends Shape> IShapeDescriptorBuilder<S> createBuilder(Class<S> clazz)
			throws NoSuchShapeDescriptorBuilderException;

	enum StaticHolder {
		;

		public static final @NonNls String DEFAULT_NAMESPACE = "default";
		public static final @NonNls String DEFAULT_PREFIX = DEFAULT_NAMESPACE + INamespacePrefixedString.StaticHolder.SEPARATOR;

		public static @NonNls String getDefaultNamespace() { return DEFAULT_NAMESPACE; }

		public static @NonNls String getDefaultPrefix() { return DEFAULT_PREFIX; }
	}
}
