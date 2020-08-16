package $group__.utilities.automata.core;

import $group__.utilities.specific.MapUtilities;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;

// TODO based on https://stackoverflow.com/q/1647631/9341868
public interface ITransitionSystem<S extends IState<D>, E, D> {
	ImmutableList<Function<ITransitionSystem<?, ?, ?>, Object>> OBJECT_VARIABLES = ImmutableList.of(
			ITransitionSystem::getState, ITransitionSystem::getInput, ITransitionSystem::getTransitionsView);
	ImmutableMap<String, Function<ITransitionSystem<?, ?, ?>, Object>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.stitchIterables(OBJECT_VARIABLES.size(),
			ImmutableList.of("state", "input", "transitionsView"),
			OBJECT_VARIABLES));

	void step(@Nullable E input, D data);

	S getState();

	Optional<E> getInput();

	Map<? extends BiPredicate<? super ITransitionSystem<? extends S, ? extends E, ? extends D>, ? super D>, ? extends Function<? super D, ? extends S>> getTransitionsView();
}