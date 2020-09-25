package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors;

import java.awt.*;

public interface IShapeDescriptorBuilderFactory {
	<S extends Shape> IShapeDescriptorBuilder<S> createBuilder(Class<S> clazz)
			throws NoSuchShapeDescriptorBuilderException;
}