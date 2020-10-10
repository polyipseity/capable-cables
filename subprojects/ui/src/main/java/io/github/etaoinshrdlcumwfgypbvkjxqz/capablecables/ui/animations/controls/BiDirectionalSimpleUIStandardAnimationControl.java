package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.animations.controls;

import com.google.common.collect.ImmutableSet;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.time.ITicker;

public class BiDirectionalSimpleUIStandardAnimationControl
		extends BiDirectionalUIStandardAnimationControl {
	protected BiDirectionalSimpleUIStandardAnimationControl(IUIAnimationTarget target,
	                                                        ITicker ticker,
	                                                        boolean autoPlay,
	                                                        long duration,
	                                                        long startDelay,
	                                                        long endDelay,
	                                                        long loop) {
		super(ImmutableSet.of(target), ticker, autoPlay,
				(target2, index, size) -> duration,
				(target2, index, size) -> startDelay,
				(target2, index, size) -> endDelay,
				(target2, index, size) -> loop);
	}
}
