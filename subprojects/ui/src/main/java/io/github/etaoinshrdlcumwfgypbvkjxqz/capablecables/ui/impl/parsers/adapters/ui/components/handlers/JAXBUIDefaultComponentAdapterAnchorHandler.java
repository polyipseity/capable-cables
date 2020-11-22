package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components.handlers;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.Anchor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming.INamed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.registries.IJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.ui.components.contexts.IJAXBUIComponentAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.JAXBUIUtilities.ObjectFactories;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions.ImmutableShapeAnchor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CacheUtilities;

import java.util.Map;

public class JAXBUIDefaultComponentAdapterAnchorHandler
		extends JAXBUIAbstractSubContextualAdapterHandler<Anchor, IJAXBUIComponentAdapterContext> {
	private final LoadingCache<IUIViewComponent<?, ?>, Map<String, IUIComponent>> viewComponentNamedMaps =
			CacheUtilities.newCacheBuilderSingleThreaded().initialCapacity(CapacityUtilities.getInitialCapacityTiny()).weakKeys()
					.build(CacheLoader.from(view -> INamed.toNamedMap(AssertionUtilities.assertNonnull(view).getChildrenFlatView())));

	public JAXBUIDefaultComponentAdapterAnchorHandler() {
		super(IJAXBUIComponentAdapterContext.class);
	}

	@Override
	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	protected void accept0(IJAXBAdapterContext context, IJAXBUIComponentAdapterContext subContext, Anchor left) {
		subContext.getContainer()
				.flatMap(container -> CastUtilities.castChecked(IUIComponent.class, container))
				.ifPresent(container ->
						container.getManager()
								.flatMap(IUIComponentManager::getView)
								.ifPresent(view ->
										IUIViewComponent.getShapeAnchorController(view).addAnchors(container,
												ImmutableList.of(new ImmutableShapeAnchor(
														AssertionUtilities.assertNonnull(getViewComponentNamedMaps().getUnchecked(view).get(left.getTarget())),
														(EnumUISide) IJAXBAdapterRegistry.adaptFromJAXB(context,
																ObjectFactories.getDefaultUIObjectFactory().createUiSide(left.getOriginSide())
														),
														(EnumUISide) IJAXBAdapterRegistry.adaptFromJAXB(context,
																ObjectFactories.getDefaultUIObjectFactory().createUiSide(left.getTargetSide())
														),
														left.getBorderThickness()))))
				);
	}

	protected LoadingCache<IUIViewComponent<?, ?>, Map<String, IUIComponent>> getViewComponentNamedMaps() {
		return viewComponentNamedMaps;
	}
}
