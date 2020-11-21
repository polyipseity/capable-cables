package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import org.jetbrains.annotations.NonNls;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class ImmutableNamespacePrefixedString
		implements INamespacePrefixedString {
	private final String namespace;
	private final String string;

	private ImmutableNamespacePrefixedString(@NonNls CharSequence namespace, @NonNls CharSequence string) {
		this.namespace = namespace.toString();
		this.string = string.toString();
	}

	public static ImmutableNamespacePrefixedString of(@NonNls CharSequence string) {
		String[] decomposed = INamespacePrefixedString.decompose(string);
		return of(decomposed[0], decomposed[1]);
	}

	public static ImmutableNamespacePrefixedString of(@NonNls CharSequence namespace, @NonNls CharSequence string) {
		return new ImmutableNamespacePrefixedString(namespace, string);
	}

	@Override
	public @NonNls String getNamespace() { return namespace; }

	@Override
	public @NonNls String getString() { return string; }

	@Override
	public int hashCode() { return ObjectUtilities.hashCodeImpl(this, StaticHolder.getObjectVariableMap().values()); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) { return ObjectUtilities.equalsImpl(this, obj, INamespacePrefixedString.class, true, StaticHolder.getObjectVariableMap().values()); }

	@Override
	public String toString() { return ObjectUtilities.toStringImpl(this, StaticHolder.getObjectVariableMap()); }
}
