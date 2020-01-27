package $group__.$modId__.utilities.constructs.interfaces;

import $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable;
import $group__.$modId__.utilities.constructs.interfaces.basic.IStrictEquality;
import $group__.$modId__.utilities.constructs.interfaces.basic.IStrictToString;
import $group__.$modId__.utilities.constructs.interfaces.basic.IStruct;

public interface IStructure<T extends IStructure<T>> extends IStruct, IStrictToString, IStrictEquality, IImmutablizable<T> {
	/* SECTION methods */

	@Override
	String toString();

	@Override
	int hashCode();

	@Override
	boolean equals(Object o);
}