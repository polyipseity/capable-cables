package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.EnumModifyStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIComponentRendererInvokerModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIViewComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.IUIViewComponentMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.components.modifiers.IUIComponentMinecraftLifecycleModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.rendering.IUIComponentRendererMinecraft.EnumCropStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.core.mvvm.views.rendering.IUIComponentRendererMinecraft.EnumRenderStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.events.bus.UIImmutableMinecraftRenderEventExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.mvvm.extensions.background.DefaultUIExtensionMinecraftBackground;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.minecraft.utilities.EnumCropMethod;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.UIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.events.bus.UIAbstractViewBusEvent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.MiscellaneousUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.EnumHookStage;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events.EventBusUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IConsumer3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class UIViewComponentMinecraft<S extends Shape, M extends IUIComponentManager<S>>
		extends UIViewComponent<S, M>
		implements IUIViewComponentMinecraft<S, M> {

	@SuppressWarnings("ThisEscapedInObjectConstruction")
	@UIViewComponentConstructor
	public UIViewComponentMinecraft(UIViewComponentConstructor.IArguments arguments) {
		super(arguments);

		IExtensionContainer.addExtensionExtendedChecked(this, new DefaultUIExtensionMinecraftBackground()); // COMMENT to ensure that 'GuiScreenEvent.BackgroundDrawnEvent' is fired
	}

	@Override
	public void render(IUIViewContext context, double partialTicks) {
		getAnimationController().update();
		getAnimationController().render();
		getManager()
				.ifPresent(manager ->
						EventBusUtilities.callWithPrePostHooks(UIEventBusEntryPoint.getEventBus(), () -> {
									EnumCropMethod cropMethod = EnumCropMethod.getBestMethod();
									cropMethod.enable();
									IUIViewComponent.traverseComponentTreeDefault(createComponentContext()
													.orElseThrow(IllegalStateException::new),
											manager,
											(componentContext, result) -> {
												assert result != null;
												assert componentContext != null;
												IUIComponentMinecraft component = (IUIComponentMinecraft) result.getComponent();
												IUIComponentModifier.streamSpecificModifiersUnion(result.getModifiersView(), IUIComponentRendererInvokerModifier.class)
														.forEachOrdered(modifier -> {
															assert modifier.getKey() != null;
															assert modifier.getValue() != null;
															EnumModifyStage.PRE.advanceModifyStage(modifier.getKey());
															modifier.getValue().invokeRenderer(componentContext); // COMMENT pre
															modifier.getKey().resetModifyStage();
														});
												component.getRendererContainer().getRenderer()
														.ifPresent(renderer -> {
															renderer.crop(componentContext, CastUtilities.castUnchecked(component),
																	EnumCropStage.CROP, cropMethod, partialTicks);
															renderer.render(componentContext, CastUtilities.castUnchecked(component),
																	EnumRenderStage.PRE_CHILDREN, partialTicks);
														});
											},
											(componentContext, result, children) -> {
												assert result != null;
												assert componentContext != null;
												IUIComponentMinecraft component = (IUIComponentMinecraft) result.getComponent();
												component.getRendererContainer().getRenderer().ifPresent(renderer -> {
													renderer.render(componentContext, CastUtilities.castUnchecked(component),
															EnumRenderStage.POST_CHILDREN, partialTicks);
													renderer.crop(componentContext, CastUtilities.castUnchecked(component),
															EnumCropStage.UN_CROP, cropMethod, partialTicks);
												});
												IUIComponentModifier.streamSpecificModifiersUnion(result.getModifiersView(), IUIComponentRendererInvokerModifier.class)
														.forEachOrdered(modifier -> {
															assert modifier.getKey() != null;
															assert modifier.getValue() != null;
															EnumModifyStage.POST.advanceModifyStage(modifier.getKey());
															modifier.getValue().invokeRenderer(componentContext); // COMMENT post
															modifier.getKey().resetModifyStage();
														});
											},
											component -> component.isVisible() && component instanceof IUIComponentMinecraft);
									cropMethod.disable();
									return true;
								},
								MiscellaneousUtilities.act(new UIAbstractViewBusEvent.Render(EnumHookStage.PRE, this),
										event -> IExtensionContainer.addExtensionChecked(event,
												UIImmutableMinecraftRenderEventExtension.of(partialTicks))),
								MiscellaneousUtilities.act(new UIAbstractViewBusEvent.Render(EnumHookStage.POST, this),
										event -> IExtensionContainer.addExtensionChecked(event,
												UIImmutableMinecraftRenderEventExtension.of(partialTicks))))
				);
	}

	@SuppressWarnings("RedundantTypeArguments")
	@Override
	public void tick() {
		getManager()
				.ifPresent(manager -> IUIViewComponent.<RuntimeException>traverseComponentTreeDefault(createComponentContext()
								.orElseThrow(IllegalStateException::new),
						manager,
						(componentContext, result) -> {
							assert componentContext != null;
							assert result != null;
							CastUtilities.castChecked(IUIComponentMinecraft.class, result.getComponent())
									.ifPresent(cc ->
											IUIComponentMinecraftLifecycleModifier.handleComponentModifiers(cc,
													result.getModifiersView(),
													componentContext,
													IUIComponentMinecraftLifecycleModifier::tick)
									);
						},
						IConsumer3.StaticHolder.getEmpty(),
						IUIEventTarget::isActive));
	}
}
