package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.binding.UIProperty;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.ui.UIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions.caches.UICacheExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.events.bus.UIComponentModifyShapeDescriptorBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.shapes.interactions.ProviderShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.ImmutableBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.fields.IField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.methods.IBindingMethodSource;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.methods.BindingMethodSource;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.EnumHookStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.EventBusUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.reactivex.rxjava3.observers.DisposableObserver;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

public class UIComponent
		extends UIEventTarget
		implements IUIComponent {
	@NonNls
	public static final String PROPERTY_VISIBLE = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "component.visible";
	@NonNls
	public static final String PROPERTY_ACTIVE = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "component.active";

	private static final INamespacePrefixedString PROPERTY_VISIBLE_LOCATION = ImmutableNamespacePrefixedString.of(PROPERTY_VISIBLE);
	private static final INamespacePrefixedString PROPERTY_ACTIVE_LOCATION = ImmutableNamespacePrefixedString.of(PROPERTY_ACTIVE);

	@Nullable
	private final String name;
	private final Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings;
	private final ConcurrentMap<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> extensions = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(INITIAL_CAPACITY_SMALL).makeMap();
	private final ConcurrentMap<INamespacePrefixedString, IBindingMethodSource<? extends IUIEvent>> eventTargetBindingMethods = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(INITIAL_CAPACITY_SMALL).makeMap();
	// todo add animation system
	// todo cache transform
	private final IShapeDescriptor<?> shapeDescriptor;
	@UIProperty(PROPERTY_VISIBLE)
	private final IBindingField<Boolean> visible;
	@UIProperty(PROPERTY_ACTIVE)
	private final IBindingField<Boolean> active;
	private final AtomicBoolean modifyingShape = new AtomicBoolean();
	private final List<IUIComponentModifier> modifiers = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);
	private OptionalWeakReference<IUIComponentContainer> parent = new OptionalWeakReference<>(null);

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	@UIComponentConstructor
	public UIComponent(UIComponentConstructor.IArguments arguments) {
		this.name = arguments.getName().orElse(null);

		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
		this.mappings = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(mappings.size()).makeMap();
		this.mappings.putAll(mappings);

		this.shapeDescriptor = new ProviderShapeDescriptor<>(this, arguments.getShapeDescriptor());

		this.visible = IUIPropertyMappingValue.createBindingField(Boolean.class, false, true,
				this.mappings.get(getPropertyVisibleLocation()));
		this.active = IUIPropertyMappingValue.createBindingField(Boolean.class, false, true,
				this.mappings.get(getPropertyActiveLocation()));

		IExtensionContainer.addExtensionChecked(this, new UICacheExtension());
	}

	public static INamespacePrefixedString getPropertyVisibleLocation() { return PROPERTY_VISIBLE_LOCATION; }

	public static INamespacePrefixedString getPropertyActiveLocation() { return PROPERTY_ACTIVE_LOCATION; }

	@Override
	public boolean modifyShape(BooleanSupplier action) throws ConcurrentModificationException {
		getModifyingShape().compareAndSet(false, true);
		boolean ret = EventBusUtilities.callWithPrePostHooks(UIEventBusEntryPoint.getEventBus(), () ->
						getShapeDescriptor().modify(action),
				new UIComponentModifyShapeDescriptorBusEvent(EnumHookStage.PRE, this),
				new UIComponentModifyShapeDescriptorBusEvent(EnumHookStage.POST, this));
		getModifyingShape().compareAndSet(true, false);
		return ret;
	}

	protected AtomicBoolean getModifyingShape() { return modifyingShape; }

	@Override
	public boolean isModifyingShape() { return getModifyingShape().get(); }

	@Override
	public Shape getAbsoluteShape()
			throws IllegalStateException {
		try (IUIComponentContext context = IUIComponent.getContext(this)) {
			return IUIComponent.getContextualShape(context, this);
		}
	}

	@Override
	public Optional<? extends String> getName() { return Optional.ofNullable(name); }

	@Override
	public Optional<? extends IUIComponentContainer> getParent() { return parent.getOptional(); }

	@Override
	public boolean dispatchEvent(IUIEvent event) {
		boolean ret = super.dispatchEvent(event);
		INamespacePrefixedString type = event.getType();
		@Nullable IBindingMethodSource<? extends IUIEvent> method = getEventTargetBindingMethods().get(type);
		if (method == null) {
			method = new BindingMethodSource<>(event.getClass(),
					Optional.ofNullable(getMappings().get(type)).flatMap(IUIPropertyMappingValue::getBindingKey).orElse(null));
			getEventTargetBindingMethods().put(type, method);
		}
		method.invoke(CastUtilities.castUnchecked(event)); // COMMENT should match
		return ret;
	}

	@Override
	public IShapeDescriptor<?> getShapeDescriptor() { return shapeDescriptor; }

	@Override
	public boolean isVisible() { return IField.getValueNonnull(getVisible()); }

	public IBindingField<Boolean> getVisible() { return visible; }

	@Override
	public void setVisible(boolean visible) { getVisible().setValue(visible); }

	@Override
	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	public void onParentChange(@Nullable IUIComponentContainer previous, @Nullable IUIComponentContainer next) {
		// COMMENT no need to concern that the view is not present yet as setting the manager of the view adds all components automatically
		getManager()
				.flatMap(IUIComponentManager::getView)
				.map(IUIViewComponent::getNamedTrackers)
				.ifPresent(trackers -> trackers.remove(IUIComponent.class, this));
		setParent(next);
		getManager()
				.flatMap(IUIComponentManager::getView)
				.map(IUIViewComponent::getNamedTrackers)
				.ifPresent(trackers -> trackers.add(IUIComponent.class, this));
	}

	protected void setParent(@Nullable IUIComponentContainer parent) { this.parent = new OptionalWeakReference<>(parent); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<INamespacePrefixedString, IBindingMethodSource<? extends IUIEvent>> getEventTargetBindingMethods() { return eventTargetBindingMethods; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappings() { return mappings; }

	@Override
	public boolean containsPoint(IUIComponentContext context, Point2D point) {
		return IUIComponent.getContextualShape(context, this).contains(point);
	}

	@Override
	@Deprecated
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> addExtension(IExtension<? extends INamespacePrefixedString, ?> extension) {
		UIExtensionRegistry.getInstance().checkExtensionRegistered(extension);
		return IExtensionContainer.addExtensionImpl(this, getExtensions(), extension);
	}

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> removeExtension(INamespacePrefixedString key) { return IExtensionContainer.removeExtensionImpl(getExtensions(), key); }

	@Nullable
	private Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier;

	protected Optional<? extends Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>>> getBinderObserverSupplier() { return Optional.ofNullable(binderObserverSupplier); }

	protected void setBinderObserverSupplier(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) { this.binderObserverSupplier = binderObserverSupplier; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initializeBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		setBinderObserverSupplier(binderObserverSupplier);
		BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier,
				() -> ImmutableBinderAction.bind(getActive(), getVisible()));
		BindingUtilities.findAndInitializeBindings(getExtensions().values(), binderObserverSupplier);
	}

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> getExtension(INamespacePrefixedString key) { return IExtensionContainer.getExtensionImpl(getExtensions(), key); }

	public IBindingField<Boolean> getActive() { return active; }

	@Override
	public void setActive(boolean active) { getActive().setValue(active); }

	@Override
	public List<? extends IUIComponentModifier> getModifiersView() { return ImmutableList.copyOf(getModifiers()); }

	@Override
	public boolean addModifier(IUIComponentModifier modifier) {
		boolean ret;
		if (modifier.getTargetComponent()
				.map(previousTargetComponent -> previousTargetComponent.removeModifier(modifier))
				.orElse(true)) {
			AssertionUtilities.assertTrue(getModifiers().add(modifier));
			modifier.setTargetComponent(this);
			ret = true;
		} else
			ret = false;
		assertModifierPresence(this, modifier, ret);
		return ret;
	}

	@Override
	public boolean removeModifier(IUIComponentModifier modifier) {
		boolean ret;
		assertModifierUnique(this, modifier);
		if (getModifiers().remove(modifier)) {
			modifier.setTargetComponent(null);
			ret = true;
		} else
			ret = false;
		assertModifierPresence(this, modifier, false);
		return ret;
	}

	@Override
	public boolean moveModifierToTop(IUIComponentModifier modifier) {
		boolean ret;
		assertModifierUnique(this, modifier);
		if (getModifiers().remove(modifier)) {
			AssertionUtilities.assertTrue(getModifiers().add(modifier));
			ret = true;
		} else
			ret = false;
		assertModifierPresence(this, modifier, ret);
		return ret;
	}

	@Override
	public void initialize(IUIComponentContext context) {}

	@Override
	public void removed(IUIComponentContext context) {}

	@Override
	public boolean isActive() { return IField.getValueNonnull(getActive()); }

	protected static void assertModifierUnique(UIComponent self, IUIComponentModifier modifier) {
		assert self.getModifiers().indexOf(modifier) == self.getModifiers().lastIndexOf(modifier);
	}

	protected static void assertModifierPresence(UIComponent self, IUIComponentModifier modifier, boolean present) {
		assert self.getModifiers().contains(modifier) == present;
	}

	@Override
	public Map<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> getExtensionsView() { return ImmutableMap.copyOf(getExtensions()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> getExtensions() { return extensions; }

	@Override
	public Map<INamespacePrefixedString, IUIPropertyMappingValue> getMappingsView() { return ImmutableMap.copyOf(getMappings()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<IUIComponentModifier> getModifiers() { return modifiers; }
}
