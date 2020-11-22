package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.relocatable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.UIProperty;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIExtensionArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.UIExtensionConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIReshapeExplicitly;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIComponentUserRelocatableExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentCursorHandleProviderModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventMouse;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types.EnumUIEventDOMType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIComponentRendererInvokerModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.ui.UIFunctionalEventListener;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.modifiers.UIAbstractVirtualComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.DelayedFieldInitializer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderObserverSupplierHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.DefaultBinderObserverSupplierHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.AbstractContainerAwareExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.impl.Optional2;
import io.reactivex.rxjava3.observers.DisposableObserver;
import org.jetbrains.annotations.NonNls;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressThisEscapedWarning;

public class UITeleportingComponentUserRelocatableExtension<C extends IUIComponent & IUIReshapeExplicitly<? extends IShapeDescriptor<? extends RectangularShape>>>
		extends AbstractContainerAwareExtension<INamespacePrefixedString, IUIComponent, C>
		implements IUIComponentUserRelocatableExtension<C> {
	@NonNls
	public static final String PROPERTY_ACTIVATION_MOUSE_BUTTONS = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.teleporting.activation.mouse";
	@NonNls
	public static final String PROPERTY_RELOCATE_BORDERS = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.teleporting.relocate_borders";
	private static final INamespacePrefixedString PROPERTY_ACTIVATION_MOUSE_BUTTONS_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyActivationMouseButtons());
	private static final INamespacePrefixedString PROPERTY_RELOCATE_BORDERS_LOCATION = ImmutableNamespacePrefixedString.of(getPropertyRelocateBorders());

	@UIProperty(PROPERTY_ACTIVATION_MOUSE_BUTTONS)
	private final IBindingField<Set<Integer>> activationMouseButtons;
	@UIProperty(PROPERTY_ACTIVATION_MOUSE_BUTTONS)
	private final IBindingField<Map<EnumUISide, Double>> relocateBorders;

	private final Modifier modifier = new Modifier(suppressThisEscapedWarning(() -> this));
	private final IUIRendererContainerContainer<IRelocatingRenderer> rendererContainerContainer;
	private final IBinderObserverSupplierHolder binderObserverSupplierHolder = new DefaultBinderObserverSupplierHolder();
	@Nullable
	private IRelocateData relocateData;

	@SuppressWarnings("unchecked")
	@UIExtensionConstructor
	public UITeleportingComponentUserRelocatableExtension(IUIExtensionArguments arguments) {
		super((Class<C>) arguments.getContainerClass());

		this.rendererContainerContainer =
				UIDefaultRendererContainerContainer.ofDefault(arguments.getRendererName().orElse(null), suppressThisEscapedWarning(() -> this), UIComponentUserRelocatableExtensionEmptyRelocatingRenderer.class);

		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.activationMouseButtons = IUIPropertyMappingValue.<Set<Integer>>createBindingField(CastUtilities.castUnchecked(Set.class),
				() -> ImmutableSet.of(GLFW.GLFW_MOUSE_BUTTON_LEFT),
				mappings.get(getPropertyActivationMouseButtonsLocation()));
		this.relocateBorders = IUIPropertyMappingValue.<Map<EnumUISide, Double>>createBindingField(CastUtilities.castUnchecked(Map.class),
				() -> ImmutableMap.of(EnumUISide.UP, 10D),
				mappings.get(getPropertyRelocateBordersLocation()));
	}

	public static INamespacePrefixedString getPropertyRelocateBordersLocation() {
		return PROPERTY_RELOCATE_BORDERS_LOCATION;
	}

	public static INamespacePrefixedString getPropertyActivationMouseButtonsLocation() {
		return PROPERTY_ACTIVATION_MOUSE_BUTTONS_LOCATION;
	}

	public static String getPropertyRelocateBorders() {
		return PROPERTY_RELOCATE_BORDERS;
	}

	public static String getPropertyActivationMouseButtons() {
		return PROPERTY_ACTIVATION_MOUSE_BUTTONS;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		IUIComponentUserRelocatableExtension.super.initializeBindings(binderObserverSupplier);
		getBinderObserverSupplierHolder().setValue(binderObserverSupplier);
		BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
				() -> ImmutableBinderAction.bind(
						getActivationMouseButtons(),
						getRelocateBorders()
				));
		BindingUtilities.initializeBindings(
				binderObserverSupplier, ImmutableList.of(getRendererContainerContainer())
		);
	}

	protected IBinderObserverSupplierHolder getBinderObserverSupplierHolder() {
		return binderObserverSupplierHolder;
	}

	protected IBindingField<Set<Integer>> getActivationMouseButtons() {
		return activationMouseButtons;
	}

	protected IUIRendererContainerContainer<IRelocatingRenderer> getRendererContainerContainer() {
		return rendererContainerContainer;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void cleanupBindings() {
		getBinderObserverSupplierHolder().getValue().ifPresent(binderObserverSupplier -> {
			BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
					() -> ImmutableBinderAction.unbind(
							getActivationMouseButtons(),
							getRelocateBorders()
					));
			BindingUtilities.cleanupBindings(
					ImmutableList.of(getRendererContainerContainer())
			);
		});
		getBinderObserverSupplierHolder().setValue(null);
		IUIComponentUserRelocatableExtension.super.cleanupBindings();
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	public void onExtensionAdded(IUIComponent container) {
		super.onExtensionAdded(container);
		container.addModifier(getModifier());
		container.getManager().flatMap(IUIComponentManager::getView)
				.ifPresent(view -> IUIView.registerRendererContainers(view, ImmutableSet.of(getRendererContainer())));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	public void onExtensionRemoved() {
		getContainer().ifPresent(container -> {
			container.removeModifier(getModifier());
			container.getManager().flatMap(IUIComponentManager::getView)
					.ifPresent(view -> IUIView.unregisterRendererContainers(view, ImmutableSet.of(getRendererContainer())));
		});
		super.onExtensionRemoved();
	}

	protected Modifier getModifier() { return modifier; }

	@Override
	public IUIRendererContainer<? extends IRelocatingRenderer> getRendererContainer() {
		return getRendererContainerContainer().getRendererContainer();
	}

	@Override
	public IExtensionType<INamespacePrefixedString, ?, IUIComponent> getType() { return StaticHolder.getTYPE().getValue(); }

	@Override
	public Optional<? extends Shape> getRelocateShape() {
		return getContainer()
				.map(IUIComponent::getShape)
				.map(Shape::getBounds2D)
				.map(containerShapeBounds ->
						getRelocateBorders().getValue().entrySet().stream().unordered()
								.map(entry -> {
									EnumUISide side = AssertionUtilities.assertNonnull(entry.getKey());
									double thickness = AssertionUtilities.assertNonnull(entry.getValue());

									EnumUISide oppositeSide = side.getOpposite().orElseThrow(IllegalStateException::new);
									Rectangle2D border = (Rectangle2D) containerShapeBounds.clone();

									// COMMENT we drag the opposite side to a given offset from our side, ignoring other sides
									oppositeSide.setValue(border,
											side.getValue(border) + side.inwardsBy(thickness).orElseThrow(IllegalStateException::new));

									return border;
								})
								.map(Area::new)
								.reduce(new Area(), (area1, area2) -> {
									Area area3 = (Area) area1.clone();
									area3.add(area2);
									return area3;
								})
				);
	}

	@Override
	public boolean isRelocating() { return getRelocateData().isPresent(); }

	@Override
	public Optional<? extends IRelocateData> getRelocateData() { return Optional.ofNullable(relocateData); }

	protected void setRelocateData(@Nullable IRelocateData relocateData) { this.relocateData = relocateData; }

	protected IBindingField<Map<EnumUISide, Double>> getRelocateBorders() {
		return relocateBorders;
	}

	public static class Modifier
			extends UIAbstractVirtualComponent
			implements IUIComponentCursorHandleProviderModifier, IUIComponentRendererInvokerModifier {
		private final OptionalWeakReference<UITeleportingComponentUserRelocatableExtension<?>> owner;
		private final Object lockObject = new Object();

		@Nullable
		private Integer activeMouseButton;

		private final DelayedFieldInitializer<SetMultimap<INamespacePrefixedString, UIEventListenerWithParameters>> eventTargetListenersInitializer;

		@SuppressWarnings({"rawtypes", "RedundantSuppression"})
		protected Modifier(UITeleportingComponentUserRelocatableExtension<?> owner) {
			super(IShapeDescriptor.StaticHolder.getShapeDescriptorPlaceholder());
			this.owner = new OptionalWeakReference<>(owner);

			this.eventTargetListenersInitializer = new DelayedFieldInitializer<>(field -> {
				addEventListener(EnumUIEventDOMType.MOUSE_DOWN.getEventType(), new UIFunctionalEventListener<IUIEventMouse>(evt -> {
					if (!getActiveMouseButton().isPresent()) {
						int button = evt.getData().getButton();
						getOwner()
								.filter(owner2 -> owner2.getActivationMouseButtons().getValue().contains(button))
								.filter(owner2 -> startRelocateMaybe(evt.getViewContext(), evt.getData().getCursorPositionView()))
								.flatMap(owner2 -> owner2.getContainer()) // TODO Java 9 - IllegalAccessError now, make method ref
								.ifPresent(c -> {
									c.getParent().ifPresent(p ->
											p.moveChildToTop(c));
									setActiveMouseButton(button);
									evt.stopPropagation();
								});
					}
				}), false);
				addEventListener(EnumUIEventDOMType.MOUSE_UP.getEventType(), new UIFunctionalEventListener<IUIEventMouse>(evt -> {
					if (getActiveMouseButton().filter(Predicate.isEqual(evt.getData().getButton())).isPresent()
							&& finishRelocateMaybe(evt.getViewContext(), evt.getData().getCursorPositionView())) {
						evt.stopPropagation();
						setActiveMouseButton(null);
					}
				}), false);
			});
		}

		@Override
		protected SetMultimap<INamespacePrefixedString, UIEventListenerWithParameters> getEventTargetListeners() {
			return this.eventTargetListenersInitializer.apply(super.getEventTargetListeners());
		}

		protected Optional<Integer> getActiveMouseButton() {
			return Optional.ofNullable(activeMouseButton);
		}

		protected void setActiveMouseButton(@Nullable Integer activeMouseButton) {
			this.activeMouseButton = activeMouseButton;
		}

		protected boolean startRelocateMaybe(@SuppressWarnings("unused") IUIViewContext viewContext, Point2D point) {
			return getOwner().map(owner -> {
				IRelocateData d = new ImmutableRelocateData(point);
				synchronized (getLockObject()) {
					if (owner.getRelocateData().isPresent())
						return false;
					owner.setRelocateData(d);
				}
				return true;
			}).orElse(false);
		}

		public Optional<? extends UITeleportingComponentUserRelocatableExtension<?>> getOwner() { return owner.getOptional(); }

		protected boolean finishRelocateMaybe(@SuppressWarnings("unused") IUIViewContext viewContext, Point2D point) {
			return getOwner()
					.flatMap(owner -> owner.getContainer().flatMap(container -> owner.getRelocateData().map(data -> {
						Rectangle2D resultRectangle = container.getShapeDescriptor().getShapeOutput().getBounds2D();
						data.handle((Point2D) point.clone(), resultRectangle, resultRectangle);
						boolean ret;
						synchronized (getLockObject()) {
							if (!owner.getRelocateData().isPresent())
								return false;
							ret = container.reshape(s -> s.adapt(resultRectangle));
							owner.setRelocateData(null);
						}
						return ret;
					})))
					.orElse(false);
		}

		protected Object getLockObject() { return lockObject; }

		@Override
		@SuppressWarnings({"rawtypes", "RedundantSuppression"})
		public Optional<Long> getCursorHandle(IUIComponentContext context) {
			return getModifyStage().isPre()
					&& getOwner()
					.filter(IUIComponentUserRelocatableExtension::isRelocating)
					.isPresent()
					? Optional.of(MemoryUtil.NULL)
					: Optional.empty();
		}

		@Override
		public boolean containsPoint(IUIComponentContext context, Point2D point) {
			return getOwner()
					.flatMap(owner -> owner.isRelocating() ?
							Optional.of(true) :
							owner.getRelocateShape()
									.map(shape -> IUIComponentContext.createContextualShape(context, shape))
									.map(shape -> shape.contains(point)))
					.orElse(false);
		}

		@Override
		public void invokeRenderer(IUIComponentContext context) {
			if (getModifyStage().isPost()) {
				getOwner().ifPresent(owner ->
						Optional2.of(
								() -> owner.getRendererContainer().getRenderer().orElse(null),
								() -> owner.getRelocateData().orElse(null))
								.ifPresent(values -> {
									IRelocatingRenderer renderer = values.getValue1Nonnull();
									IRelocateData data = values.getValue2Nonnull();
									renderer.render(context, data);
								}));
			}
		}
	}
}
