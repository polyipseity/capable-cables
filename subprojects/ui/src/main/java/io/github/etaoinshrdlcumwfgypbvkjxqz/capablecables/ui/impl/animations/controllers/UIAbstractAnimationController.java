package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.animations.controllers;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationControllable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationController;

public abstract class UIAbstractAnimationController
		implements IUIAnimationController {
	@Override
	public boolean add(IUIAnimationControllable... elements) {
		return add(ImmutableList.copyOf(elements));
	}

	@Override
	public boolean remove(IUIAnimationControllable... elements) {
		return remove(ImmutableList.copyOf(elements));
	}
}