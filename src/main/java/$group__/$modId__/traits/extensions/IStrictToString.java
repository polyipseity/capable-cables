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
import java.util.function.Function;

import static $group__.$modId__.utilities.helpers.Dynamics.*;
import static $group__.$modId__.utilities.helpers.specific.Loggers.EnumMessages.INVOCATION_UNABLE_TO_UNREFLECT_MEMBER;
import static $group__.$modId__.utilities.helpers.specific.Loggers.EnumMessages.SUFFIX_WITH_THROWABLE;
import static $group__.$modId__.utilities.helpers.specific.MapsExtension.CACHE_EXPIRATION_ACCESS_DURATION;
import static $group__.$modId__.utilities.helpers.specific.MapsExtension.CACHE_EXPIRATION_ACCESS_TIME_UNIT;
import static $group__.$modId__.utilities.helpers.specific.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.specific.Throwables.*;
import static $group__.$modId__.utilities.variables.Constants.*;
import static $group__.$modId__.utilities.variables.Globals.LOGGER;

public interface IStrictToString {
	/* SECTION static variables */
	
	LoadingCache<Class<?>, BiFunction<Object, String, String>> FUNCTION_MAP = CacheBuilder.newBuilder().initialCapacity(INITIAL_SIZE_MEDIUM).expireAfterAccess(CACHE_EXPIRATION_ACCESS_DURATION, CACHE_EXPIRATION_ACCESS_TIME_UNIT).concurrencyLevel(MULTI_THREAD_THREAD_COUNT).build(CacheLoader.from(k -> {
		assert k != null;

		final boolean[] first = {true};
		ArrayList<BiFunction<Object, String, String>> sfs = new ArrayList<>(INITIAL_SIZE_SMALL);
		getThisAndSuperclasses(k).forEach(c -> {
			for (Field f : c.getDeclaredFields()) {
				if (isMemberStatic(f)) continue;
				String fnp = f.getName() + '=';
				@Nullable MethodHandle fm = unboxOptional(tryCall(() -> IMPL_LOOKUP.unreflectGetter(f), LOGGER));
				consumeIfCaughtThrowable(t -> LOGGER.warn(() -> SUFFIX_WITH_THROWABLE.makeMessage(INVOCATION_UNABLE_TO_UNREFLECT_MEMBER.makeMessage("field getter", f, IMPL_LOOKUP), t)));

				Function<Object, String> ff;
				if (fm == null) {
					@Nullable Throwable cau = getCaughtThrowableUnboxedStatic();
					ff = t -> "!!!Thrown{throwable=" + cau + "}!!!";
				} else ff = t -> fnp + unboxOptional(tryCallWithLogging(() -> fm.invoke(t), LOGGER));

				if (!first[0]) sfs.add((t, ss) -> ", ");
				sfs.add((t, ss) -> fnp + ff.apply(t));
				first[0] = false;
			}
		});
		if (!first[0]) sfs.add((t, ss) -> ", ");
		sfs.add((t, ss) -> "super=" + ss + '}');

		String tcp = k.getSimpleName() + '{';
		return (t, ss) -> sfs.stream().map(sf -> sf.apply(t, ss)).reduce(tcp, String::concat);
	}));


	/* SECTION static methods */

	static String getToStringString(Object thisObj, String superString) { return FUNCTION_MAP.getUnchecked(thisObj.getClass()).apply(thisObj, superString); }


	/* SECTION methods */

	@Override
	@OverridingStatus(group = GROUP, when = When.ALWAYS)
	String toString();
}
