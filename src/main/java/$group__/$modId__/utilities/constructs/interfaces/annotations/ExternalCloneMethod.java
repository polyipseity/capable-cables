package $group__.$modId__.utilities.constructs.interfaces.annotations;

import $group__.$modId__.utilities.constructs.interfaces.basic.IAnnotationProcessor;
import $group__.$modId__.utilities.helpers.Reflections.Unsafe.AccessibleObjectAdapter.MethodAdapter;
import $group__.$modId__.utilities.helpers.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IAnnotationProcessor.getMessage;
import static $group__.$modId__.utilities.helpers.Reflections.Unsafe.getDeclaredMethod;
import static $group__.$modId__.utilities.helpers.Throwables.interrupt;
import static $group__.$modId__.utilities.variables.Globals.LOGGER;
import static $group__.$modId__.utilities.variables.Globals.getCaughtThrowableUnboxedStatic;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 */
@Documented
@Nonnull
@Retention(RUNTIME)
@Target(METHOD)
public @interface ExternalCloneMethod {
	/* SECTION methods */

	Class<?>[] value();

	@SuppressWarnings("SameReturnValue") boolean allowExtends() default false;


	/* SECTION static variables */

	MethodAdapter CLONE_METHOD_DEFAULT = MethodAdapter.of(getDeclaredMethod(Object.class, "clone").get().orElseGet(() -> {
		LOGGER.warn("Default clone method access failed", getCaughtThrowableUnboxedStatic());
		return null;
	}));
	@Nullable
	ExternalCloneMethod CLONE_METHOD_DEFAULT_ANNOTATION = CLONE_METHOD_DEFAULT.get().isPresent() ? new ExternalCloneMethod() {
		/* SECTION methods */

		@Override
		public Class<?>[] value() { return new Class<?>[0]; }

		@Override
		public boolean allowExtends() { return false; }


		@Override
		public Class<ExternalCloneMethod> annotationType() { return ExternalCloneMethod.class; }
	} : null;

	LoadingCache<Class<?>, ExternalCloneMethod> EXTERNAL_CLONE_METHOD_ANNOTATIONS_CACHE = CacheBuilder.newBuilder().maximumWeight(100).weigher((k, v) -> v == CLONE_METHOD_DEFAULT_ANNOTATION ? 1 : 0).build(new CacheLoader<Class<?>, ExternalCloneMethod>() {
		@Override
		public ExternalCloneMethod load(Class<?> key) throws InterruptedException {
			@Nullable ExternalCloneMethod r = null;

			long n = Long.MAX_VALUE;
			for (Map.Entry<Class<?>, ExternalCloneMethod> e : EXTERNAL_CLONE_METHOD_ANNOTATIONS_CACHE.asMap().entrySet()) {
				ExternalCloneMethod v = e.getValue();
				if (v.allowExtends()) {
					Class<?> k = e.getKey();
					if (k.isAssignableFrom(key)) {
						long l = 0;
						for (Class<?> s = key; s != null; l++) {
							if (s == k) {
								if (l < n) {
									n = l;
									r = v;
								}
								break;
							}
							s = s.getSuperclass();
						}
					}
				}
			}

			if (r != null)
				LOGGER.debug("Clone method '{}' with annotation '{}' auto-registered for class '{}'", EXTERNAL_CLONE_METHOD_MAP.get(r).get().orElseThrow(Throwables::unexpected).toGenericString(), r, key.toGenericString());
			else if ((r = CLONE_METHOD_DEFAULT_ANNOTATION) == null)
				throw interrupt("Default clone method inaccessible for class '" + key.toGenericString() + "'");
			else
				LOGGER.debug("Using default clone method '{}' for class '{}'", CLONE_METHOD_DEFAULT.get().orElseThrow(Throwables::unexpected).toGenericString(), key.toGenericString());

			return r;
		}
	});
	WeakHashMap<ExternalCloneMethod, MethodAdapter> EXTERNAL_CLONE_METHOD_MAP = new WeakHashMap<>();


	/* SECTION static classes */

	enum AnnotationProcessor implements IAnnotationProcessor.IClass.IElement.IMethod<ExternalCloneMethod> {
		/* SECTION enums */
		INSTANCE;


		/* SECTION variables */

		private volatile boolean processed = false;


		/* SECTION methods */

		@Override
		public Class<ExternalCloneMethod> annotationType() { return ExternalCloneMethod.class; }

		@Override
		public boolean isProcessed() { return processed; }


		@Override
		public void processMethod(Result<ExternalCloneMethod> result) {
			ExternalCloneMethod a = result.annotations[0];
			@Nullable ExternalCloneMethod ap;
			Method m = result.element;

			Class<?>[] ks = a.value();
			if (ks.length == 0) {
				LOGGER.warn(getMessage(this, "Method '{}' with annotation '{}' has no usage"), m.toGenericString(), a);
				return;
			}
			EXTERNAL_CLONE_METHOD_MAP.put(a, MethodAdapter.of(m));

			for (Class<?> k : ks) {
				ap = EXTERNAL_CLONE_METHOD_ANNOTATIONS_CACHE.getIfPresent(k);
				EXTERNAL_CLONE_METHOD_ANNOTATIONS_CACHE.put(k, a);
				if (ap == null)
					LOGGER.debug(getMessage(this, "Registered method '{}' with annotation '{}' for class '{}'"), m.toGenericString(), a, k.toGenericString());
				else
					LOGGER.warn(getMessage(this, "Replaced previous method '{}' with annotation '{}' with method '{}' with annotation '{}' for class '{}'"), EXTERNAL_CLONE_METHOD_MAP.get(ap).get().orElseThrow(Throwables::unexpected).toGenericString(), ap, m.toGenericString(), a, k.toGenericString());
			}
		}

		@Override
		public void process(ASMDataTable asm) {
			IMethod.super.process(asm);
			processed = true;
		}


		/* SECTION static initializer */

		static { EXTERNAL_CLONE_METHOD_MAP.put(CLONE_METHOD_DEFAULT_ANNOTATION, CLONE_METHOD_DEFAULT); }
	}
}