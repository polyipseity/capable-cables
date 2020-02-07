package $group__.$modId__.traits.extensions;

import $group__.$modId__.annotations.OverridingStatus;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import javax.annotation.Nullable;
import javax.annotation.meta.When;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static $group__.$modId__.utilities.helpers.Dynamics.*;
import static $group__.$modId__.utilities.helpers.specific.Loggers.EnumMessages.INVOCATION_UNABLE_TO_UNREFLECT_MEMBER;
import static $group__.$modId__.utilities.helpers.specific.Loggers.EnumMessages.SUFFIX_WITH_THROWABLE;
import static $group__.$modId__.utilities.helpers.specific.MapsExtension.CACHE_EXPIRATION_ACCESS_DURATION;
import static $group__.$modId__.utilities.helpers.specific.MapsExtension.CACHE_EXPIRATION_ACCESS_TIME_UNIT;
import static $group__.$modId__.utilities.helpers.specific.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.specific.Throwables.*;
import static $group__.$modId__.utilities.variables.Constants.*;
import static $group__.$modId__.utilities.variables.Globals.LOGGER;

public interface IStrictHashCode {
	/* SECTION static variables */

	LoadingCache<Class<?>, BiFunction<Object, Supplier<? extends Integer>, Integer>> FUNCTION_MAP = CacheBuilder.newBuilder().initialCapacity(INITIAL_SIZE_MEDIUM).expireAfterAccess(CACHE_EXPIRATION_ACCESS_DURATION, CACHE_EXPIRATION_ACCESS_TIME_UNIT).concurrencyLevel(MULTI_THREAD_THREAD_COUNT).build(CacheLoader.from(k -> {
		assert k != null;

		ArrayList<BiFunction<Object, Supplier<? extends Integer>, Object>> ffs = new ArrayList<>(INITIAL_SIZE_SMALL);
		if (k.getSuperclass() != Object.class) ffs.add((t, hcs) -> hcs.get());

		getThisAndSuperclasses(k).forEach(c -> {
			for (Field f : c.getDeclaredFields()) {
				if (isMemberStatic(f) || !isMemberFinal(f)) continue;
				@Nullable MethodHandle fm = unboxOptional(tryCall(() -> IMPL_LOOKUP.unreflectGetter(f), LOGGER));
				consumeIfCaughtThrowable(t -> LOGGER.warn(() -> SUFFIX_WITH_THROWABLE.makeMessage(INVOCATION_UNABLE_TO_UNREFLECT_MEMBER.makeMessage("field getter", f, IMPL_LOOKUP), t)));
				if (fm == null) continue;

				ffs.add((t, hcs) -> unboxOptional(tryCallWithLogging(() -> fm.invoke(t), LOGGER)));
			}
		});

		return (t, hcs) -> ffs.stream().map(ff -> ff.apply(t, hcs)).collect(Collectors.toList()).hashCode();
	}));


	/* SECTION static methods */

	static int getHashCode(Object thisObj, Supplier<? extends Integer> superHashCode) { return FUNCTION_MAP.getUnchecked(thisObj.getClass()).apply(thisObj, superHashCode); }


	/* SECTION methods */

	@Override
	@OverridingStatus(group = GROUP, when = When.ALWAYS)
	int hashCode();
}
