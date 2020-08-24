package $group__.ui.core.mvvm.structures;

import $group__.utilities.LoggerUtilities;
import $group__.utilities.MapUtilities;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.interfaces.ICopyable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.awt.geom.AffineTransform;
import java.util.Optional;
import java.util.Stack;
import java.util.function.Function;

public interface IAffineTransformStack
		extends ICopyable, AutoCloseable {
	Logger LOGGER = LogManager.getLogger();
	ImmutableList<Function<IAffineTransformStack, Object>> OBJECT_VARIABLES = ImmutableList.of(
			IAffineTransformStack::getDelegated);
	ImmutableMap<String, Function<IAffineTransformStack, Object>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.stitchIterables(OBJECT_VARIABLES.size(),
			ImmutableList.of("delegated"),
			OBJECT_VARIABLES));

	@Override
	IAffineTransformStack copy();

	Stack<AffineTransform> getDelegated();

	@SuppressWarnings("UnusedReturnValue")
	AffineTransform push();

	@Override
	default void close() { createCleaner().run(); }

	default boolean isClean() { return isClean(getDelegated()); }

	static boolean isClean(Stack<AffineTransform> delegated) {
		if (delegated.size() != 1)
			return false;
		@Nullable AffineTransform t = delegated.get(0);
		assert t != null;
		return t.isIdentity();
	}

	default Runnable createCleaner() {
		return () ->
				IAffineTransformStack.popMultiple(this, getDelegated().size() - 1);
	}

	static void popMultiple(IAffineTransformStack stack, int times) {
		for (; times > 0; --times)
			stack.getDelegated().pop();
	}

	class LeakNotifier
			implements Runnable {
		protected final Stack<AffineTransform> delegated;
		@Nullable
		protected final Throwable throwable;

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		public LeakNotifier(Stack<AffineTransform> delegated) {
			this.delegated = delegated;
			this.throwable = ThrowableUtilities.createIfDebug().orElse(null);
		}

		@Override
		public void run() {
			if (!isClean(getDelegated()))
				LOGGER.warn(() -> LoggerUtilities.EnumMessages.SUFFIX_WITH_THROWABLE.makeMessage(
						LoggerUtilities.EnumMessages.FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Stack not clean, content:{}{}", System.lineSeparator(), getDelegated()),
						getThrowable().orElse(null)
				));
		}

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected Stack<AffineTransform> getDelegated() { return delegated; }

		protected Optional<Throwable> getThrowable() { return Optional.ofNullable(throwable); }
	}
}