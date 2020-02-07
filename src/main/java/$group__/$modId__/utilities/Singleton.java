package $group__.$modId__.utilities;

import $group__.$modId__.utilities.helpers.Casts;
import $group__.$modId__.utilities.helpers.specific.Throwables;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

import static $group__.$modId__.utilities.helpers.Dynamics.IMPL_LOOKUP;
import static $group__.$modId__.utilities.helpers.specific.Loggers.EnumMessages.FACTORY_PARAMETERIZED_MESSAGE;
import static $group__.$modId__.utilities.helpers.specific.MapsExtension.MAP_MAKER_MULTI_THREAD;
import static $group__.$modId__.utilities.helpers.specific.Throwables.*;
import static com.google.common.collect.Maps.immutableEntry;
import static java.lang.System.lineSeparator;
import static java.lang.invoke.MethodType.methodType;

public abstract class Singleton {
	/* SECTION static variables */

	protected static final ConcurrentMap<Class<?>, Map.Entry<? extends Singleton, String>> INSTANCES = MAP_MAKER_MULTI_THREAD.makeMap();


	/* SECTION constructors */

	protected Singleton(Logger logger) {
		Class<? extends Singleton> clazz = getClass();
		String classGS = clazz.toGenericString(),
				sts = getCurrentStackTraceString();

		Map.Entry<? extends Singleton, String> v = immutableEntry(this, sts);
		@Nullable Map.Entry<? extends Singleton, String> vo = INSTANCES.put(clazz, v);
		if (vo != null) {
			logger.error(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Singleton instance of '{}' already created, previous stacktrace:{}{}", classGS, lineSeparator(), vo.getValue()));
			throw rejectInstantiation();
		}

		logger.debug(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Singleton instance of '{}' created, stacktrace:{}{}", classGS, lineSeparator(), sts));
	}


	/* SECTION static methods */

	public static <T extends Singleton> T getSingletonInstance(Class<T> clazz, Logger logger) { return getSingletonInstance(clazz, true, t -> tryCallWithLogging(() -> IMPL_LOOKUP.findConstructor(t, methodType(void.class)).invokeExact(), logger).flatMap(Casts::castUnchecked)).orElseThrow(Throwables::rethrowCaughtThrowableStatic); }

	public static <T extends Singleton> Optional<T> getSingletonInstance(Class<T> clazz, boolean instantiate, Function<Class<T>, ? extends Optional<T>> instantiation) {
		Optional<T> r = Optional.ofNullable(INSTANCES.get(clazz)).map(Map.Entry::getKey).flatMap(Casts::castUnchecked);
		if (!r.isPresent() && instantiate) r = instantiation.apply(clazz);
		return r;
	}
}
