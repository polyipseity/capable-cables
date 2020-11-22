package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views;

import com.google.common.cache.CacheLoader;
import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationController;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewCoordinator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming.INamedTrackers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.theming.IUIThemeStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.controllers.UIDefaultAnimationController;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.UIAbstractSubInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.UIDefaultingTheme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.bus.UIAbstractViewBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.naming.ConcurrentConfigurableNamedTracker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.naming.LoadingNamedTrackers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.theming.UIArrayThemeStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ConcurrencyUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EnumHookStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EventBusUtilities;

import java.awt.*;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

public abstract class UIAbstractView<S extends Shape>
		extends UIAbstractSubInfrastructure<IUIViewContext>
		implements IUIView<S> {
	private final ConcurrentMap<Class<?>, IUIViewCoordinator> coordinatorMap =
			MapBuilderUtilities.newMapMakerNormalThreaded().initialCapacity(CapacityUtilities.getInitialCapacitySmall()).makeMap();

	public UIAbstractView() {
		UIArrayThemeStack themeStack = new UIArrayThemeStack(FunctionUtilities.getEmptyConsumer(), CapacityUtilities.getInitialCapacitySmall());
		themeStack.push(new UIDefaultingTheme());

		this.coordinatorMap.put(IUIAnimationController.class, new UIDefaultAnimationController());
		this.coordinatorMap.put(INamedTrackers.class,
				new LoadingNamedTrackers(CacheLoader.from(() ->
						new ConcurrentConfigurableNamedTracker<>(builder ->
								builder.weakValues() // COMMENT use weak values - the trackers do not own OUR objects
										.concurrencyLevel(ConcurrencyUtilities.getNormalThreadThreadCount())
										.initialCapacity(CapacityUtilities.getInitialCapacityLarge()))))
		);
		this.coordinatorMap.put(IUIThemeStack.class, themeStack);
	}

	@Override
	public final void render() {
		EventBusUtilities.callWithPrePostHooks(UIEventBusEntryPoint.getEventBus(), () -> {
					render0();
					return true;
				},
				FunctionUtilities.accept(new UIAbstractViewBusEvent.Render(EnumHookStage.PRE, this), this::acceptRenderEvent),
				FunctionUtilities.accept(new UIAbstractViewBusEvent.Render(EnumHookStage.POST, this), this::acceptRenderEvent));
	}

	protected void render0() {
		IUIAnimationController animationController = IUIView.getAnimationController(this);
		animationController.update();
		animationController.render();
	}

	protected void acceptRenderEvent(UIAbstractViewBusEvent.Render event) {}

	@SuppressWarnings("unchecked")
	@Override
	public <C extends IUIViewCoordinator> Optional<? extends C> getCoordinator(Class<C> key) {
		return (Optional<? extends C>) Optional.ofNullable(getCoordinatorMap().get(key));
	}

	@Override
	public @Immutable Map<Class<?>, IUIViewCoordinator> getCoordinatorMapView() {
		return ImmutableMap.copyOf(getCoordinatorMap());
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<Class<?>, IUIViewCoordinator> getCoordinatorMap() {
		return coordinatorMap;
	}
}
