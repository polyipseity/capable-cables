package $group__.client.gui.components;

import $group__.annotations.OverridingStatus;
import $group__.client.gui.traits.IColored;
import $group__.logging.ILogging;
import $group__.logging.ILoggingUser;
import $group__.utilities.Constants;
import $group__.utilities.concurrent.IMutatorImmutablizable;
import $group__.utilities.concurrent.IMutatorUser;
import $group__.utilities.extensions.ICloneable;
import $group__.utilities.extensions.IStructure;
import org.apache.logging.log4j.Logger;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.meta.When;

import static $group__.utilities.concurrent.IMutator.trySetNonnull;
import static $group__.utilities.extensions.IStrictEquals.areEqual;
import static $group__.utilities.extensions.IStrictHashCode.getHashCode;
import static $group__.utilities.extensions.IStrictToString.getToStringString;
import static $group__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;

public class GuiColor<T extends GuiColor<T, C>, C> implements IStructure<T, T>, ICloneable<T>,
		IMutatorUser<IMutatorImmutablizable<?, ?>>, IColored<C>, ILoggingUser<ILogging<Logger>, Logger> {
	protected C color;

	protected IMutatorImmutablizable<?, ?> mutator;
	protected ILogging<Logger> logging;


	public GuiColor(C color, IMutatorImmutablizable<?, ?> mutator, ILogging<Logger> logging) {
		this.mutator = trySetNonnull(mutator, mutator, true);
		this.logging = trySetNonnull(getMutator(), logging, true);
		this.color = trySetNonnull(getMutator(), color, true);
	}

	public GuiColor(GuiColor<?, C> copy) { this(copy, copy.getMutator()); }


	protected GuiColor(GuiColor<?, C> copy, IMutatorImmutablizable<?, ?> mutator) {
		this(copy.getColor(), mutator,
				copy.logging);
	}


	@Nonnull
	@Override
	public C getColor() { return color; }

	@Override
	public boolean trySetColor(@Nullable @CheckForNull C color) {
		return color != null && trySet(t -> this.color = t,
				color);
	}

	@Override
	public IMutatorImmutablizable<?, ?> getMutator() { return mutator; }

	@Override
	public boolean trySetMutator(IMutatorImmutablizable<?, ?> mutator) {
		return trySet(t -> this.mutator = t,
				mutator);
	}

	@Override
	public ILogging<Logger> getLogging() { return logging; }

	@Override
	public boolean trySetLogging(ILogging<Logger> logging) { return trySet(t -> this.logging = t, logging); }


	@Override
	public T toImmutable() {
		return castUncheckedUnboxedNonnull(isImmutable() ? this : new GuiColor<>(this,
				IMutatorImmutablizable.of(getMutator().toImmutable())));
	}

	@Override
	public boolean isImmutable() { return getMutator().isImmutable(); }


	@Override
	@OverridingStatus(group = Constants.PACKAGE)
	public final String toString() { return getToStringString(this, super.toString()); }

	@Override
	@OverridingStatus(group = Constants.PACKAGE)
	public final int hashCode() { return getHashCode(this, super::hashCode); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	@OverridingStatus(group = Constants.PACKAGE)
	public final boolean equals(Object obj) { return areEqual(this, obj, super::equals); }

	@SuppressWarnings("Convert2MethodRef")
	@Override
	@OverridingMethodsMustInvokeSuper
	@OverridingStatus(group = Constants.PACKAGE, when = When.MAYBE)
	public T clone() { return ICloneable.clone(() -> super.clone(), getLogger()); }
}
