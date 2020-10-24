package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IKeyboardKeyPressData;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class ImmutableKeyboardKeyPressData
		extends AbstractTimestampedInputData
		implements IKeyboardKeyPressData, Cloneable {
	private final int key;
	private final int scanCode;
	private final int modifiers;

	public ImmutableKeyboardKeyPressData(int key, int scanCode, int modifiers) {
		this.key = key;
		this.scanCode = scanCode;
		this.modifiers = modifiers;
	}

	@Override
	public int hashCode() { return ObjectUtilities.hashCodeImpl(this, IKeyboardKeyPressData.StaticHolder.getObjectVariablesMap().values()); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) { return ObjectUtilities.equalsImpl(this, obj, IKeyboardKeyPressData.class, true, IKeyboardKeyPressData.StaticHolder.getObjectVariablesMap().values()); }

	@Override
	public String toString() { return ObjectUtilities.toStringImpl(this, IKeyboardKeyPressData.StaticHolder.getObjectVariablesMap()); }

	@Override
	@OverridingMethodsMustInvokeSuper
	public ImmutableKeyboardKeyPressData recreate() {
		return (ImmutableKeyboardKeyPressData) super.recreate();
	}

	@Override
	public int getKey() { return key; }

	@Override
	public int getScanCode() { return scanCode; }

	@Override
	public int getModifiers() { return modifiers; }

	@Override
	public ImmutableKeyboardKeyPressData clone() {
		try {
			return (ImmutableKeyboardKeyPressData) super.clone();
		} catch (CloneNotSupportedException e) {
			throw ThrowableUtilities.propagate(e);
		}
	}
}