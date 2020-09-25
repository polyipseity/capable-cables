package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.adapters;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.adapters.IUIAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.cursors.IUIExtensionCursorHandleProvider;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventKeyboard;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventMouse;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIDataMouseButtonClick;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.ui.UIEventUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.IUIInfrastructureMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.extensions.IUIExtensionContainerProvider;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.extensions.IUIExtensionScreenProvider;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.utilities.UIDataKeyboardKeyPress;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.utilities.UIDataMouseButtonClick;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.utilities.UIInputUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.utilities.minecraft.MinecraftDrawingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.utilities.minecraft.UIBackgrounds;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AffineTransformUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.NoSuchBindingTransformerException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.IHasGenericClass;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftInputUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftOpenGLUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.ui.MinecraftTextComponentUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.ui.MinecraftTooltipUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.DoubleDimension2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.paths.INode;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThrowableUtilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

import javax.annotation.Nullable;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

@OnlyIn(Dist.CLIENT)
public class UIScreenAdapter
		<I extends IUIInfrastructureMinecraft<?, ?, ?>, C extends Container>
		extends AbstractContainerScreenAdapter<I, C>
		implements IUIAdapter<I> {
	protected final ConcurrentMap<Integer, IUIEventKeyboard>
			keyboardKeysBeingPressed = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(INITIAL_CAPACITY_SMALL).makeMap();
	protected final ConcurrentMap<Integer, IUIEventMouse>
			mouseButtonsBeingPressed = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(INITIAL_CAPACITY_SMALL).makeMap();

	protected final I infrastructure;
	@Nullable
	protected final C containerObject;
	protected final Set<Integer> closeKeys, changeFocusKeys;
	protected boolean paused = false;
	@Nullable
	protected IUIEventTarget targetBeingHoveredByMouse = null;
	protected ITextComponent title;

	@Nullable
	protected IUIEventTarget focus;

	protected UIScreenAdapter(ITextComponent title, I infrastructure, @Nullable C containerObject, Set<Integer> closeKeys, Set<Integer> changeFocusKeys) {
		super(title);
		this.title = title;
		this.infrastructure = infrastructure;
		this.containerObject = containerObject;
		this.closeKeys = Collections.newSetFromMap(MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(closeKeys.size()).makeMap());
		this.closeKeys.addAll(closeKeys);
		this.changeFocusKeys = Collections.newSetFromMap(MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(changeFocusKeys.size()).makeMap());
		this.changeFocusKeys.addAll(changeFocusKeys);
		IExtensionContainer.StaticHolder.addExtensionChecked(infrastructure, new UIExtensionScreen());

		if (containerObject != null)
			IExtensionContainer.StaticHolder.addExtensionChecked(infrastructure, new UIExtensionContainer(containerObject));
	}

	@Override
	public ITextComponent getTitle() { return title; }

	public void setTitle(ITextComponent title) { this.title = title; }

	@Override
	@Deprecated
	public void render(int mouseX, int mouseY, float partialTicks) {
		Point2D cp = new Point2D.Double(mouseX, mouseY);
		getInfrastructure().getView().render((Point2D) cp.clone(), partialTicks);
		IUIExtensionCursorHandleProvider.TYPE.getValue().find(getInfrastructure().getView()).ifPresent(ext ->
				GLFW.glfwSetCursor(MinecraftOpenGLUtilities.getWindowHandle(), ext.getCursorHandle((Point2D) cp.clone()).<Long>map(Function.identity()).orElse(MemoryUtil.NULL)));
	}

	public Set<Integer> getCloseKeysView() { return ImmutableSet.copyOf(getCloseKeys()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<Integer> getCloseKeys() { return closeKeys; }

	public boolean addCloseKeys(Iterable<Integer> keys) {
		boolean ret = false;
		for (Integer k : keys)
			ret |= getCloseKeys().add(k);
		return ret;
	}

	public boolean removeCloseKeys(Iterable<Integer> keys) {
		boolean ret = false;
		for (Integer k : keys)
			ret |= getCloseKeys().remove(k);
		return ret;
	}

	public Set<Integer> getChangeFocusKeysView() { return ImmutableSet.copyOf(getChangeFocusKeys()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<Integer> getChangeFocusKeys() { return changeFocusKeys; }

	public boolean addChangeFocusKeys(Iterable<Integer> keys) {
		boolean ret = false;
		for (Integer k : keys)
			ret |= getChangeFocusKeys().add(k);
		return ret;
	}

	public boolean removeChangeFocusKeys(Iterable<Integer> keys) {
		boolean ret = false;
		for (Integer k : keys)
			ret |= getChangeFocusKeys().remove(k);
		return ret;
	}

	@Override
	public I getInfrastructure() { return infrastructure; }

	@Nullable
	protected UIDataMouseButtonClick lastMouseClickData = null;

	protected static <E extends IUIEventKeyboard> E addEventKeyboard(UIScreenAdapter<?, ?> self, E event) {
		self.getKeyboardKeysBeingPressed().put(event.getData().getKey(), event);
		return event;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Map<Integer, IUIEventKeyboard> getKeyboardKeysBeingPressed() { return keyboardKeysBeingPressed; }

	@Override
	@Deprecated
	public boolean shouldCloseOnEsc() { return getCloseKeys().contains(GLFW.GLFW_KEY_ESCAPE); }

	@Override
	@Deprecated
	protected void renderTooltip(ItemStack item, int mouseX, int mouseY) { MinecraftTooltipUtilities.renderTooltip(getMinecraft(), width, height, font, itemRenderer, item, mouseX, mouseY); }

	@Override
	@Deprecated
	public List<String> getTooltipFromItem(ItemStack item) { return MinecraftTooltipUtilities.getTooltipFromItem(getMinecraft(), item); }

	@Override
	@Deprecated
	public void renderTooltip(String tooltip, int mouseX, int mouseY) { MinecraftTooltipUtilities.renderTooltip(width, height, font, itemRenderer, tooltip, mouseX, mouseY); }

	@Override
	@Deprecated
	public void renderTooltip(List<String> tooltip, int mouseX, int mouseY) { MinecraftTooltipUtilities.renderTooltip(width, height, font, itemRenderer, tooltip, mouseX, mouseY); }

	@Override
	@Deprecated
	public void renderTooltip(List<String> tooltip, int mouseX, int mouseY, FontRenderer font) { MinecraftTooltipUtilities.renderTooltip(width, height, itemRenderer, tooltip, mouseX, mouseY, font); }

	@Override
	@Deprecated
	protected void renderComponentHoverEffect(ITextComponent component, int mouseX, int mouseY) { MinecraftTextComponentUtilities.renderComponentHoverEffect(getMinecraft(), width, height, font, component, mouseX, mouseY); }

	@Override
	@Deprecated
	public boolean handleComponentClicked(ITextComponent component) { return MinecraftTextComponentUtilities.handleComponentClicked(this, component); }

	@Override
	@Deprecated
	protected void init() {
		try {
			IUIInfrastructure.StaticHolder.bindSafe(getInfrastructure());
		} catch (NoSuchBindingTransformerException e) {
			throw ThrowableUtilities.propagate(e);
		}
		setSize(width, height);
		getInfrastructure().initialize();
	}

	@Override
	@Deprecated
	public void setSize(int width, int height) {
		super.setSize(width, height);
		getInfrastructure().getView().reshape(s -> {
			s.bound(new Rectangle2D.Double(0, 0, width, height));
			return true;
		});
	}

	@Override
	@Deprecated
	public List<? extends IGuiEventListener> children() { return ImmutableList.of(); }

	@Override
	@Deprecated
	public void removed() {
		GLFW.glfwSetCursor(MinecraftOpenGLUtilities.getWindowHandle(), MemoryUtil.NULL);
		{
			// COMMENT generate opposite synthetic events
			// COMMENT NO default actions
			ImmutableSet.copyOf(getKeyboardKeysBeingPressed().keySet()).stream().unordered()
					.forEach(k ->
							removeEventKeyboard(this, k).ifPresent(e2 ->
									UIEventUtilities.dispatchEvent(UIEventUtilities.Factory.generateSyntheticEventKeyboardOpposite(e2))));
			Point2D cp = MinecraftInputUtilities.getScaledCursorPosition();
			ImmutableSet.copyOf(getMouseButtonsBeingPressed().keySet()).stream().unordered()
					.forEach(k ->
							removeEventMouse(this, AssertionUtilities.assertNonnull(k)).ifPresent(e2 ->
									UIEventUtilities.dispatchEvent(UIEventUtilities.Factory.generateSyntheticEventMouseOpposite(e2, cp))));
			setTargetBeingHoveredByMouse(null, new UIDataMouseButtonClick(cp));
			setLastMouseClickData(null, null);
			setFocus(null);
		}
		getInfrastructure().removed();
		IUIInfrastructure.StaticHolder.unbindSafe(getInfrastructure());
	}

	@Override
	@Deprecated
	public void renderBackground() { UIBackgrounds.renderDefaultBackgroundAndNotify(getMinecraft(), width, height); }

	@Override
	@Deprecated
	public void tick() { getInfrastructure().tick(); }

	protected static Optional<IUIEventKeyboard> removeEventKeyboard(UIScreenAdapter<?, ?> self, int key) { return Optional.ofNullable(self.getKeyboardKeysBeingPressed().remove(key)); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Map<Integer, IUIEventMouse> getMouseButtonsBeingPressed() { return mouseButtonsBeingPressed; }

	protected static Optional<IUIEventMouse> removeEventMouse(UIScreenAdapter<?, ?> self, int key) { return Optional.ofNullable(self.getMouseButtonsBeingPressed().remove(key)); }

	@Override
	@Deprecated
	public void renderBackground(int blitOffset) { UIBackgrounds.renderDefaultBackgroundAndNotify(getMinecraft(), width, height, blitOffset); }

	@Override
	@Deprecated
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		Point2D cp = new Point2D.Double(mouseX, mouseY);
		UIDataMouseButtonClick d = new UIDataMouseButtonClick(cp, button);
		IUIEventTarget t = getInfrastructure().getView().getTargetAtPoint((Point2D) cp.clone());
		if (UIEventUtilities.dispatchEvent(addEventMouse(this, UIEventUtilities.Factory.createEventMouseDown(t, d)))) {
			// TODO select
			// TODO drag or drop perhaps
			// TODO scroll/pan
		}
		return true;
	}

	@Override
	@Deprecated
	public boolean keyReleased(int key, int scanCode, int modifiers) {
		return removeEventKeyboard(this, key).filter(e -> {
			UIEventUtilities.dispatchEvent(
					UIEventUtilities.Factory.generateSyntheticEventKeyboardOpposite(e));
			return true;
		}).isPresent(); // COMMENT NO default action
	}

	@Override
	@Deprecated
	public boolean charTyped(char codePoint, int modifiers) {
		getFocus().ifPresent(f -> UIEventUtilities.dispatchEvent(
				UIEventUtilities.Factory.createEventChar(f, codePoint, modifiers)));
		return true;
	}

	protected void setLastMouseClickData(@Nullable UIDataMouseButtonClick newMouseClickData, @Nullable IUIEventTarget target) {
		boolean ret = getLastMouseClickData()
				.flatMap(dl -> Optional.ofNullable(newMouseClickData)
						.filter(d -> UIInputUtilities.isDoubleClick(dl, d)
								&& UIEventUtilities.dispatchEvent(UIEventUtilities.Factory.createEventClickDouble(AssertionUtilities.assertNonnull(target), d))))
				.isPresent();
		this.lastMouseClickData = newMouseClickData;
		if (ret)
			setFocus(target);
		// TODO select
	}

	@Override
	@Deprecated
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		// TODO test if release works when multiple buttons are clicked
		Point2D cp = new Point2D.Double(mouseX, mouseY);
		UIDataMouseButtonClick d = new UIDataMouseButtonClick(cp, button);
		IUIEventTarget t = getInfrastructure().getView().getTargetAtPoint((Point2D) cp.clone());
		removeEventMouse(this, button).ifPresent(e -> {
			if (UIEventUtilities.dispatchEvent(UIEventUtilities.Factory.generateSyntheticEventMouseOpposite(e, cp)))
				; // TODO context menu
			if (t.equals(e.getTarget())) {
				if (UIEventUtilities.dispatchEvent(UIEventUtilities.Factory.createEventClick(t, d)))
					setFocus(t);
				setLastMouseClickData(d, t);
			}
		});
		return true;
	}

	@Override
	@Deprecated
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		Point2D cp = new Point2D.Double(mouseX, mouseY);
		IUIEventTarget target = getInfrastructure().getView().getTargetAtPoint((Point2D) cp.clone());
		UIEventUtilities.dispatchEvent(
				UIEventUtilities.Factory.createEventWheel(false, target, new UIDataMouseButtonClick(cp), target, delta)); // COMMENT nothing to be scrolled
		return true;
	}

	@Override
	@Deprecated
	public void renderDirtBackground(int blitOffset) { UIBackgrounds.renderDirtBackgroundAndNotify(getMinecraft(), width, height, blitOffset); }

	@Override
	@Deprecated
	public boolean isPauseScreen() { return isPaused(); }

	public boolean isPaused() { return paused; }

	public void setPaused(boolean paused) { this.paused = paused; }

	@Override
	@Deprecated
	public void resize(Minecraft client, int width, int height) {
		super.resize(client, width, height);
		// COMMENT resize calls init
	}

	protected Optional<? extends IUIEventTarget> getTargetBeingHoveredByMouse() { return Optional.ofNullable(targetBeingHoveredByMouse); }

	protected Optional<? extends UIDataMouseButtonClick> getLastMouseClickData() { return Optional.ofNullable(lastMouseClickData); }

	protected Optional<? extends IUIEventTarget> getFocus() { return Optional.ofNullable(focus); }

	protected static <E extends IUIEventMouse> E addEventMouse(UIScreenAdapter<?, ?> self, E event) {
		self.getMouseButtonsBeingPressed().put(event.getData().getButton(), event);
		return event;
	}

	@Override
	public boolean hasContainer() { return getContainerObject().isPresent(); }

	protected Optional<? extends C> getContainerObject() { return Optional.ofNullable(containerObject); }

	@Override
	public C getContainer()
			throws UnsupportedOperationException {
		return getContainerObject()
				.orElseThrow(UnsupportedOperationException::new);
	}

	@Override
	@Deprecated
	public void mouseMoved(double mouseX, double mouseY) {
		Point2D cp = new Point2D.Double(mouseX, mouseY);
		UIDataMouseButtonClick d = new UIDataMouseButtonClick(cp);
		UIEventUtilities.dispatchEvent(
				UIEventUtilities.Factory.createEventMouseMove(getInfrastructure().getView().getTargetAtPoint((Point2D) cp.clone()), d));
		setTargetBeingHoveredByMouse(getInfrastructure().getView().getTargetAtPoint((Point2D) cp.clone()), d);
	}

	@SuppressWarnings("unused")
	@OnlyIn(Dist.CLIENT)
	public static class Builder<I extends IUIInfrastructureMinecraft<?, ?, ?>, C extends Container> {
		protected final ITextComponent title;
		protected final I infrastructure;
		protected Set<Integer> closeKeys = ImmutableSet.of(GLFW.GLFW_KEY_ESCAPE);
		protected Set<Integer> changeFocusKeys = ImmutableSet.of(GLFW.GLFW_KEY_TAB);

		public Builder(ITextComponent title, I infrastructure) {
			this.title = title;
			this.infrastructure = infrastructure;
		}

		public AbstractScreenAdapter<I> build() {
			return new UIScreenAdapter<>(
					getTitle(),
					getInfrastructure(),
					null,
					getCloseKeys(),
					getChangeFocusKeys());
		}

		protected ITextComponent getTitle() { return title; }

		protected I getInfrastructure() { return infrastructure; }

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected Set<Integer> getCloseKeys() { return closeKeys; }

		public Builder<I, C> setCloseKeys(Set<Integer> closeKeys) {
			this.closeKeys = ImmutableSet.copyOf(closeKeys);
			return this;
		}

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected Set<Integer> getChangeFocusKeys() { return changeFocusKeys; }

		public Builder<I, C> setChangeFocusKeys(Set<Integer> changeFocusKeys) {
			this.changeFocusKeys = ImmutableSet.copyOf(changeFocusKeys);
			return this;
		}

		@SuppressWarnings("unused")
		@OnlyIn(Dist.CLIENT)
		public static class WithChildren<I extends IUIInfrastructureMinecraft<?, ?, ?>, C extends Container>
				extends Builder<I, C> {
			protected final C containerObject;

			public WithChildren(ITextComponent titleIn, I infrastructure, C containerObject) {
				super(titleIn, infrastructure);
				this.containerObject = containerObject;
			}

			@Override
			public AbstractContainerScreenAdapter<I, C> build() {
				return new UIScreenAdapter<>(
						getTitle(),
						getInfrastructure(),
						getContainerObject(),
						getCloseKeys(),
						getChangeFocusKeys());
			}

			protected C getContainerObject() { return containerObject; }
		}
	}

	@Override
	@Deprecated
	public boolean mouseDragged(double mouseX, double mouseY, int button, double mouseXDiff, double mouseYDiff) { return getMouseButtonsBeingPressed().containsKey(button); }

	@Override
	@Deprecated
	public boolean keyPressed(int key, int scanCode, int modifiers) {
		if (getFocus().map(f -> UIEventUtilities.dispatchEvent(
				addEventKeyboard(this, UIEventUtilities.Factory.createEventKeyDown(f,
						new UIDataKeyboardKeyPress(key, scanCode, modifiers))))).orElse(true)) {
			if (getCloseKeys().contains(key))
				onClose();
			if (getChangeFocusKeys().contains(key))
				changeFocus((modifiers & GLFW.GLFW_MOD_SHIFT) == 0);
		}
		return true;
	}

	protected boolean setFocus(@Nullable IUIEventTarget focus) {
		Optional<? extends IUIEventTarget> p = getFocus(), n = Optional.ofNullable(focus);
		if (n.map(IUIEventTarget::isFocusable).orElse(true) && !n.equals(p)) {
			@Nullable IUIEventTarget pv = p.orElse(null);
			p.ifPresent(f -> UIEventUtilities.dispatchEvent(
					UIEventUtilities.Factory.createEventFocusOutPre(f, focus)));
			n.ifPresent(f -> UIEventUtilities.dispatchEvent(
					UIEventUtilities.Factory.createEventFocusInPre(f, pv)));
			this.focus = focus;
			p.ifPresent(f -> UIEventUtilities.dispatchEvent(
					UIEventUtilities.Factory.createEventFocusOutPost(f, focus)));
			n.ifPresent(f -> UIEventUtilities.dispatchEvent(
					UIEventUtilities.Factory.createEventFocusInPost(f, pv)));
			return true;
		}
		return false;
	}

	protected void setTargetBeingHoveredByMouse(@Nullable IUIEventTarget targetBeingHoveredByMouse, IUIDataMouseButtonClick data) {
		Optional<? extends IUIEventTarget> o = getTargetBeingHoveredByMouse();
		Optional<IUIEventTarget> n = Optional.ofNullable(targetBeingHoveredByMouse);
		if (!n.equals(o)) {
			List<?>
					op = o.map(t ->
					CastUtilities.castChecked(INode.class, t)
							.<List<?>>map(UIEventUtilities::computeNodePath)
							.orElseGet(() -> ImmutableList.of(t)))
					.orElseGet(ImmutableList::of),
					np = n.map(t ->
							CastUtilities.castChecked(INode.class, t)
									.<List<?>>map(UIEventUtilities::computeNodePath)
									.orElseGet(() -> ImmutableList.of(t)))
							.orElseGet(ImmutableList::of);
			int i = 0;
			for (int maxI = Math.min(op.size(), np.size()); i < maxI; ++i) {
				if (!Objects.equals(op.get(i), np.get(i)))
					break;
			}
			int iFinal = i; // COMMENT paths equal for [0,i)
			o.ifPresent(t -> {
				UIEventUtilities.dispatchEvent(
						UIEventUtilities.Factory.createEventMouseLeaveSelf(t, data, n.orElse(null))); // COMMENT consider bubbling
				Lists.reverse(op.subList(iFinal, op.size())).forEach(t2 ->
						CastUtilities.castChecked(IUIEventTarget.class, t2)
								.ifPresent(t3 -> UIEventUtilities.dispatchEvent(
										UIEventUtilities.Factory.createEventMouseLeave(t3, data, n.orElse(null)))));
			});
			this.targetBeingHoveredByMouse = targetBeingHoveredByMouse;
			n.ifPresent(t -> {
				UIEventUtilities.dispatchEvent(
						UIEventUtilities.Factory.createEventMouseEnterSelf(t, data, o.orElse(null))); // COMMENT consider bubbling
				np.subList(iFinal, np.size()).forEach(t2 ->
						CastUtilities.castChecked(IUIEventTarget.class, t2)
								.ifPresent(t3 -> UIEventUtilities.dispatchEvent(
										UIEventUtilities.Factory.createEventMouseEnter(t3, data, o.orElse(null)))));
			});
		}
	}

	@Override
	@Deprecated
	public boolean changeFocus(boolean next) { return setFocus(getInfrastructure().getView().changeFocus(getFocus().orElse(null), next).orElse(null)); }

	@Override
	@Deprecated
	protected void hLine(int x1, int x2, int y, int color) { MinecraftDrawingUtilities.hLine(AffineTransformUtilities.getIdentity(), x1, x2, y, color, getBlitOffset()); }

	@Override
	@Deprecated
	protected void vLine(int x, int y1, int y2, int color) { MinecraftDrawingUtilities.vLine(AffineTransformUtilities.getIdentity(), x, y1, y2, color, getBlitOffset()); }

	@Override
	@Deprecated
	protected void fillGradient(int x1, int y1, int x2, int y2, int colorTop, int colorBottom) {
		Rectangle2D shape = new Rectangle2D.Double();
		shape.setFrameFromDiagonal(x1, y1, x2, y2);
		MinecraftDrawingUtilities.fillGradient(AffineTransformUtilities.getIdentity(), shape, colorTop, colorBottom, getBlitOffset());
	}

	@Override
	@Deprecated
	public void drawCenteredString(FontRenderer font, String string, int x, int y, int color) { MinecraftDrawingUtilities.drawCenteredString(AffineTransformUtilities.getIdentity(), font, string, new Point2D.Double(x, y), color); }

	@Override
	@Deprecated
	public void drawRightAlignedString(FontRenderer font, String string, int x, int y, int color) { MinecraftDrawingUtilities.drawRightAlignedString(AffineTransformUtilities.getIdentity(), font, string, new Point2D.Double(x, y), color); }

	@Override
	@Deprecated
	public void drawString(FontRenderer font, String string, int x, int y, int color) { MinecraftDrawingUtilities.drawString(AffineTransformUtilities.getIdentity(), font, string, new Point2D.Double(x, y), color); }

	@SuppressWarnings("MagicNumber")
	@Override
	@Deprecated
	public void blit(int x, int y, int u, int v, int w, int h) { MinecraftDrawingUtilities.blit(AffineTransformUtilities.getIdentity(), new Rectangle2D.Double(x, y, w, h), new Point2D.Double(u, v), new DoubleDimension2D(256, 256), getBlitOffset()); }

	@OnlyIn(Dist.CLIENT)
	public static class UIExtensionContainer
			extends IHasGenericClass.Impl<IUIInfrastructure<?, ?, ?>>
			implements IUIExtensionContainerProvider {
		protected final Container container;

		public UIExtensionContainer(Container container) {
			super(CastUtilities.castUnchecked(IUIInfrastructure.class)); // COMMENT class should not care about it
			this.container = container;
		}

		@Override
		public Container getContainer() { return container; }

		@Override
		public IExtensionType<INamespacePrefixedString, ?, IUIInfrastructure<?, ?, ?>> getType() { return IUIExtensionContainerProvider.TYPE.getValue(); }
	}

	@OnlyIn(Dist.CLIENT)
	public class UIExtensionScreen
			extends IHasGenericClass.Impl<IUIInfrastructure<?, ?, ?>>
			implements IUIExtensionScreenProvider {
		protected UIExtensionScreen() {
			super(CastUtilities.castUnchecked(IUIInfrastructure.class)); // COMMENT class should not care about it
		}

		@Override
		public Screen getScreen() { return UIScreenAdapter.this; }

		@Override
		public Set<Integer> getCloseKeys() { return UIScreenAdapter.this.getCloseKeys(); }

		@Override
		public Set<Integer> getChangeFocusKeys() { return UIScreenAdapter.this.getChangeFocusKeys(); }

		@Override
		public void setPaused(boolean paused) { UIScreenAdapter.this.setPaused(paused); }

		@Override
		public void setTitle(ITextComponent title) { UIScreenAdapter.this.setTitle(title); }

		@Override
		public IExtensionType<INamespacePrefixedString, ?, IUIInfrastructure<?, ?, ?>> getType() { return IUIExtensionScreenProvider.TYPE.getValue(); }
	}
}