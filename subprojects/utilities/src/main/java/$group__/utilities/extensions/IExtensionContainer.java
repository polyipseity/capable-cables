package $group__.utilities.extensions;

import $group__.utilities.CastUtilities;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public interface IExtensionContainer<K, V extends IExtension<? extends K, ?>> {
	Logger LOGGER = LogManager.getLogger();

	@SuppressWarnings("UnusedReturnValue")
	static <K, V extends IExtension<? extends K, ? super C>, C extends IExtensionContainer<K, ? super V>> Optional<? super V> addExtensionSafe(C container, V extension) { return container.addExtension(extension); }

	Optional<V> addExtension(V extension);

	static <K, V extends IExtension<? extends K, ? super C>, C extends IExtensionContainer<K, ? super V>> Optional<? super V> addExtensionDecorated(C container, V extension, BiFunction<? super C, ? super Optional<? super V>, ? extends V> function) {
		Optional<? super V> o = container.getExtension(extension.getType().getKey());
		return container.addExtension(function.apply(container, o));
	}

	static <K, V extends IExtension<? super K, ?>> Optional<V> getExtension(Map<K, V> extensions, K key) { return Optional.ofNullable(extensions.get(key)); }

	@SuppressWarnings("unchecked")
	static <K, V extends IExtension<? extends K, ?>> Optional<V> addExtension(IExtensionContainer<K, V> self, Map<K, V> extensions, K key, V extension) {
		if (extension.getType().getKey() instanceof ResourceLocation)
			IExtension.RegExtension.checkExtensionRegistered((IExtension<? extends ResourceLocation, ?>) extension); // COMMENT checked
		if (!extension.getGenericClass().isInstance(self))
			throw BecauseOf.illegalArgument("Self is not an instance of extension's container class",
					"self.getClass()", self.getClass(),
					"extension.getGenericClass()", extension.getGenericClass(),
					"extension", extension,
					"self", self);

		Optional<V> ret = self.removeExtension(key);
		extensions.put(key, extension);
		extension.onExtensionAdded(CastUtilities.castUnchecked(self)); // COMMENT type checked above
		return ret;
	}

	Optional<V> removeExtension(K key);

	static <K, V extends IExtension<? extends K, ?>> Optional<V> removeExtension(Map<K, V> extensions, K key) {
		return Optional.ofNullable(extensions.remove(key)).filter(eo -> {
			eo.onExtensionRemoved();
			return true;
		});
	}

	Optional<V> getExtension(K key);

	Map<K, V> getExtensionsView();
}