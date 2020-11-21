package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.minecraft.core.mvvm.extensions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.IUIExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.MinecraftUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.ImmutableExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IRegistryObject;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public interface IUIMinecraftScreenProviderExtension
		extends IUIExtension<INamespacePrefixedString, IUIInfrastructure<?, ?, ?>> {

	Optional<? extends Screen> getScreen();

	Optional<? extends Set<Integer>> getCloseKeys();

	Optional<? extends Set<Integer>> getChangeFocusKeys();

	boolean setPaused(boolean paused);

	boolean setTitle(ITextComponent title);

	@OnlyIn(Dist.CLIENT)
	enum StaticHolder {
		;

		private static final INamespacePrefixedString KEY = ImmutableNamespacePrefixedString.of(MinecraftUtilities.getNamespace(), "screen");
		@SuppressWarnings("unchecked")
		private static final IRegistryObject<IExtensionType<INamespacePrefixedString, IUIMinecraftScreenProviderExtension, IUIInfrastructure<?, ?, ?>>> TYPE =
				UIExtensionRegistry.getInstance().register(getKey(), new ImmutableExtensionType<>(getKey(), (t, i) -> (Optional<? extends IUIMinecraftScreenProviderExtension>) i.getExtension(t.getKey())));

		public static INamespacePrefixedString getKey() {
			return KEY;
		}

		public static IRegistryObject<IExtensionType<INamespacePrefixedString, IUIMinecraftScreenProviderExtension, IUIInfrastructure<?, ?, ?>>> getType() {
			return TYPE;
		}
	}
}
