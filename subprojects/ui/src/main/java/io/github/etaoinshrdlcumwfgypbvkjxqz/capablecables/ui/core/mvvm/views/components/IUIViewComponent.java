package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.traits.IHasBindingMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.paths.IUIComponentPathResolver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.TreeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.traits.IHasBinding;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IConsumer3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IThrowingBiFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public interface IUIViewComponent<S extends Shape, M extends IUIComponentManager<?>>
		extends IHasBinding, IHasBindingMap, IUIView<S> {
	Optional<? extends M> getManager();

	IUIComponentContext createComponentContext();

	List<IUIComponent> getChildrenFlatView();

	IUIComponentPathResolver<IUIComponent> getPathResolver();

	IUIComponentShapeAnchorController getShapeAnchorController();

	void setManager(@Nullable M manager);

	enum StaticHolder {
		;
		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());

		public static Optional<IUIComponentContext> createComponentContextWithManager(IUIViewComponent<?, ?> view) {
			return view.getManager()
					.map(manager -> {
						IUIComponentContext context = view.createComponentContext();
						context.push(manager);
						return context;
					});
		}

		public static <T extends Throwable> void traverseComponentTreeDefault(IUIComponentContext context,
		                                                                      IUIComponent root,
		                                                                      BiConsumer<? super IUIComponentContext, ? super IUIComponent> pre,
		                                                                      IConsumer3<? super IUIComponentContext, ? super IUIComponent, ? super Iterable<? super IUIComponent>, ? extends T> post) throws T {
			traverseComponentTreeDefault(context, root, pre, post, FunctionUtilities.alwaysTruePredicate());
		}

		public static <T extends Throwable> void traverseComponentTreeDefault(IUIComponentContext context,
		                                                                      IUIComponent root,
		                                                                      BiConsumer<? super IUIComponentContext, ? super IUIComponent> pre,
		                                                                      IConsumer3<? super IUIComponentContext, ? super IUIComponent, ? super Iterable<? super IUIComponent>, ? extends T> post,
		                                                                      Predicate<? super IUIComponent> predicate) throws T {
			TreeUtilities.visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, root,
					component -> {
						if (predicate.test(component)) {
							context.push(component);
							pre.accept(context, component);
						}
						return component;
					},
					component -> CastUtilities.castChecked(IUIComponentContainer.class, component)
							.filter(predicate)
							.map(IUIComponentContainer::getChildrenView)
							.orElseGet(ImmutableList::of),
					IThrowingBiFunction.execute((parent, children) -> {
						if (predicate.test(parent)) {
							post.accept(context, parent, children);
							context.pop();
						}
						return parent;
					}),
					repeated -> { throw new AssertionError(); });
		}

		public static IUIComponent getComponentByID(IUIViewComponent<?, ?> view, String id) {
			return findComponentByID(view, id)
					.orElseThrow(() ->
							new IllegalArgumentException(
									new LogMessageBuilder()
											.addMarkers(UIMarkers.getInstance()::getMarkerUIView)
											.addKeyValue("view", view).addKeyValue("id", id)
											.addMessages(() -> getResourceBundle().getString("component.get.id.fail"))
											.build()
							));
		}

		public static Optional<IUIComponent> findComponentByID(IUIViewComponent<?, ?> view, String id) {
			for (IUIComponent c : view.getChildrenFlatView()) {
				if (c.getID().filter(Predicate.isEqual(id)).isPresent())
					return Optional.of(c);
			}
			return Optional.empty();
		}

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
	}
}