package $group__.$modId__.annotations;

import $group__.$modId__.common.events.AnnotationProcessingEvent;
import $group__.$modId__.traits.basic.IAnnotationProcessor;
import $group__.$modId__.traits.extensions.ICloneable;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.invoke.MethodHandle;

import static $group__.$modId__.traits.basic.IAnnotationProcessor.getAnnotationProcessorMessage;
import static $group__.$modId__.traits.extensions.ICloneable.DEFAULT_ANNOTATION;
import static $group__.$modId__.traits.extensions.ICloneable.DEFAULT_METHOD;
import static $group__.$modId__.utilities.helpers.Dynamics.IMPL_LOOKUP;
import static $group__.$modId__.utilities.helpers.specific.Loggers.EnumMessages.*;
import static $group__.$modId__.utilities.helpers.specific.Throwables.consumeIfCaughtThrowable;
import static $group__.$modId__.utilities.helpers.specific.Throwables.tryCall;
import static $group__.$modId__.utilities.variables.Constants.MOD_ID;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Nonnull
@Retention(RUNTIME)
@Target(METHOD)
public @interface ExternalCloneMethod {
	/* SECTION methods */

	Class<?>[] value();

	@SuppressWarnings("SameReturnValue") boolean allowExtends() default false;


	/* SECTION static classes */

	@Mod.EventBusSubscriber(modid = MOD_ID)
	enum AnnotationProcessor implements IAnnotationProcessor.IClass.IElement.IMethod<ExternalCloneMethod> {
		/* SECTION enums */
		INSTANCE;


		/* SECTION variables */

		private volatile boolean processed = false;


		/* SECTION static methods */

		@SubscribeEvent(priority = EventPriority.HIGHEST)
		public static void process(AnnotationProcessingEvent event) { if (MOD_ID.equals(event.getModId())) INSTANCE.process(event.getAsm(), event.getLogger()); }


		/* SECTION methods */

		@Override
		public void process(ASMDataTable asm, Logger logger) {
			ICloneable.EXTERNAL_METHOD_MAP.put(DEFAULT_ANNOTATION, DEFAULT_METHOD);
			IMethod.super.process(asm, logger);
			processed = true;
		}

		@Override
		public Class<ExternalCloneMethod> annotationType() { return ExternalCloneMethod.class; }

		@Override
		public boolean isProcessed() { return processed; }

		@Override
		public void processMethod(Result<ExternalCloneMethod> result, Logger logger) {
			ExternalCloneMethod a = result.annotations[0];
			@Nullable ExternalCloneMethod ap;
			@Nullable MethodHandle m = tryCall(() -> IMPL_LOOKUP.unreflect(result.element), logger).orElseGet(() -> {
				consumeIfCaughtThrowable(t -> logger.warn(() -> SUFFIX_WITH_THROWABLE.makeMessage(INVOCATION_UNABLE_TO_UNREFLECT_MEMBER.makeMessage("to-immutable method", result.element, IMPL_LOOKUP), t)));
				return null;
			});

			Class<?>[] ks = a.value();
			if (ks.length == 0) {
				logger.warn(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage(getAnnotationProcessorMessage(this, "Method '{}' with annotation '{}' has no usage"), m, a));
				return;
			}
			ICloneable.EXTERNAL_METHOD_MAP.put(a, m);

			for (Class<?> k : ks) {
				ap = ICloneable.EXTERNAL_ANNOTATIONS_MAP.get(k);
				ICloneable.EXTERNAL_ANNOTATIONS_MAP.put(k, a);
				if (ap == null)
					logger.debug(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage(getAnnotationProcessorMessage(this, "Registered method '{}' with annotation '{}' for class '{}'"), m, a, k.toGenericString()));
				else {
					ExternalCloneMethod apf = ap;
					logger.warn(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage(getAnnotationProcessorMessage(this, "Replaced previous method '{}' with annotation '{}' with method '{}' with annotation '{}' for class '{}'"), ICloneable.EXTERNAL_METHOD_MAP.get(apf), apf, m, a, k.toGenericString()));
				}
			}
		}
	}
}
