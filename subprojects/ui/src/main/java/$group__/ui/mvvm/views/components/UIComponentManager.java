package $group__.ui.mvvm.views.components;

import $group__.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentContainer;
import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.ui.core.mvvm.views.components.IUIComponentShapeAnchorController;
import $group__.ui.core.mvvm.views.components.extensions.caches.IUIExtensionCache;
import $group__.ui.core.mvvm.views.components.paths.IUIComponentPathResolver;
import $group__.ui.core.mvvm.views.events.IUIEventTarget;
import $group__.ui.core.parsers.components.UIComponentConstructor;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.mvvm.structures.AffineTransformStack;
import $group__.ui.mvvm.views.components.paths.UIComponentPathResolver;
import $group__.ui.mvvm.views.events.bus.EventUIComponentHierarchyChanged;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.TreeUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.reactive.DisposableObserverAuto;
import $group__.utilities.structures.NamespacePrefixedString;
import $group__.utilities.structures.Registry;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.awt.*;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UIComponentManager<S extends Shape>
		extends UIComponentContainer
		implements IUIComponentManager<S> {
	protected final IUIComponentPathResolver<IUIComponent> pathResolver = new PathResolver();
	protected final IUIComponentShapeAnchorController shapeAnchorController = new UIComponentShapeAnchorController();

	@UIComponentConstructor(type = UIComponentConstructor.ConstructorType.MAPPING__SHAPE_DESCRIPTOR)
	public UIComponentManager(Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping, IShapeDescriptor<S> shapeDescriptor) { super(mapping, shapeDescriptor); }

	@Override
	public IUIComponentPathResolver<IUIComponent> getPathResolver() { return pathResolver; }

	@Override
	public boolean reshape(Function<? super IShapeDescriptor<? super S>, ? extends Boolean> action) throws ConcurrentModificationException { return IUIComponent.reshapeComponent(this, getShapeDescriptor(), action); }

	@SuppressWarnings("unchecked")
	@Override
	public IShapeDescriptor<S> getShapeDescriptor() {
		return (IShapeDescriptor<S>) super.getShapeDescriptor(); // COMMENT should be safe
	}

	@Override
	public Optional<IUIEventTarget> changeFocus(@Nullable IUIEventTarget currentFocus, boolean next) {
		return CastUtilities.castChecked(IUIComponent.class, currentFocus)
				.<Optional<IUIEventTarget>>map(cf ->
						CacheManager.CHILDREN_FLAT_FOCUSABLE.getValue().get(this)
								.filter(f -> !f.isEmpty())
								.map(f -> f.get(Math.floorMod(
										Math.max(f.indexOf(cf), 0) + (next ? 1 : -1), f.size()))))
				.orElseGet(() -> Optional.of(this));
	}

	@Override
	public IAffineTransformStack getCleanTransformStack() { return new AffineTransformStack(); }

	@Override
	public List<IUIComponent> getChildrenFlatView() { return ImmutableList.copyOf(CacheManager.CHILDREN_FLAT.getValue().get(this).orElseThrow(InternalError::new)); }

	public enum CacheManager {
		;

		private static final Logger LOGGER = LogManager.getLogger();

		@SuppressWarnings("UnstableApiUsage")
		public static final Registry.RegistryObject<IUIExtensionCache.IType<List<IUIComponent>, IUIComponentManager<?>>> CHILDREN_FLAT =
				IUIExtensionCache.RegUICache.INSTANCE.registerApply(generateKey("children_flat"),
						k -> new IUIExtensionCache.IType.Impl<>(k,
								(t, i) -> IUIExtensionCache.TYPE.getValue().get(i).flatMap(cache -> ThrowableUtilities.Try.call(() -> {
									@SuppressWarnings("unchecked") @Nullable List<WeakReference<IUIComponent>> cv =
											(List<WeakReference<IUIComponent>>) cache.getDelegated().getIfPresent(t.getKey());
									List<IUIComponent> ret;
									if (cv == null) {
										ret = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_LARGE);
										TreeUtilities.<IUIComponent, Object>visitNodesDepthFirst(i,
												ret::add,
												p -> CastUtilities.castChecked(IUIComponentContainer.class, p)
														.<Iterable<IUIComponent>>map(IUIComponentContainer::getChildrenView)
														.orElseGet(ImmutableSet::of), null, null);
										cache.getDelegated().put(t.getKey(),
												ret.stream().sequential()
														.map(WeakReference::new)
														.collect(Collectors.toList()));
									} else {
										cv.removeIf(wr ->
												wr.get() == null);
										ret = cv.stream().sequential()
												.map(Reference::get)
												.filter(Objects::nonNull)
												.collect(ImmutableList.toImmutableList());
									}
									return ret;
								}, LOGGER).map(CastUtilities::castUnchecked)),
								(t, i) -> IUIExtensionCache.IType.invalidate(i, t.getKey()),
								t -> ImmutableList.of(new DisposableObserverAuto<EventUIComponentHierarchyChanged.Parent>() {
									@Override
									@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
									public void onNext(EventUIComponentHierarchyChanged.Parent event) {
										if (event.getStage() == EnumEventHookStage.POST)
											CastUtilities.castChecked(IUIComponentManager.class, event.getComponent())
													.ifPresent(t::invalidate);
									}
								})));
		@SuppressWarnings("UnstableApiUsage")
		public static final Registry.RegistryObject<IUIExtensionCache.IType<List<IUIComponent>, IUIComponentManager<?>>> CHILDREN_FLAT_FOCUSABLE =
				IUIExtensionCache.RegUICache.INSTANCE.registerApply(generateKey("children_flat.focusable"),
						k -> new IUIExtensionCache.IType.Impl<>(k,
								(t, i) -> IUIExtensionCache.TYPE.getValue().get(i).flatMap(cache -> ThrowableUtilities.Try.call(() -> {
									@SuppressWarnings("unchecked") @Nullable List<WeakReference<IUIComponent>> cv =
											(List<WeakReference<IUIComponent>>) cache.getDelegated().getIfPresent(t.getKey());
									List<IUIComponent> ret;
									if (cv == null) {
										ret = i.getChildrenFlatView().stream().sequential()
												.filter(IUIComponent::isFocusable)
												.collect(ImmutableList.toImmutableList());
										cache.getDelegated().put(t.getKey(),
												ret.stream().sequential()
														.map(WeakReference::new)
														.collect(Collectors.toList()));
									} else {
										cv.removeIf(wr ->
												wr.get() == null);
										ret = cv.stream().sequential()
												.map(Reference::get)
												.filter(Objects::nonNull)
												.collect(ImmutableList.toImmutableList());
									}
									return ret;
								}, LOGGER).map(CastUtilities::castUnchecked)),
								(t, i) -> IUIExtensionCache.IType.invalidate(i, t.getKey()),
								t -> ImmutableList.of(new DisposableObserverAuto<EventUIComponentHierarchyChanged.Parent>() {
									@Override
									@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
									public void onNext(EventUIComponentHierarchyChanged.Parent event) {
										if (event.getStage() == EnumEventHookStage.POST)
											CastUtilities.castChecked(IUIComponentManager.class, event.getComponent())
													.ifPresent(t::invalidate);
									}
								})));

		private static INamespacePrefixedString generateKey(@SuppressWarnings("SameParameterValue") String name) { return new NamespacePrefixedString(INamespacePrefixedString.DEFAULT_NAMESPACE, "manager." + name); }
	}

	@Override
	public IUIComponentShapeAnchorController getShapeAnchorController() { return shapeAnchorController; }

	public class PathResolver extends UIComponentPathResolver {
		protected PathResolver() { super(UIComponentManager.this); }
	}
}
