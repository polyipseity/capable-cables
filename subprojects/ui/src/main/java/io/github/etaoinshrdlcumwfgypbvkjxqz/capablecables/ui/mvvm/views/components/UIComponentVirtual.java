package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nullable;
import java.util.Optional;

public class UIComponentVirtual
		extends UIComponent {
	protected OptionalWeakReference<IUIComponent> relatedComponent = new OptionalWeakReference<>(null);

	public UIComponentVirtual(IShapeDescriptor<?> shapeDescriptor) {
		super(ImmutableMap.of(), null, shapeDescriptor);
	}

	@Override
	public Optional<? extends IUIComponentContainer> getParent() { return getRelatedComponent().flatMap(IUIComponent::getParent); }

	public Optional<? extends IUIComponent> getRelatedComponent() { return relatedComponent.getOptional(); }

	public void setRelatedComponent(@Nullable IUIComponent relatedComponent) { this.relatedComponent = new OptionalWeakReference<>(relatedComponent); }
}