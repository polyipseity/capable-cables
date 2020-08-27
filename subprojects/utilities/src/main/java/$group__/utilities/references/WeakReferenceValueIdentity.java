package $group__.utilities.references;

import $group__.utilities.CastUtilities;

import javax.annotation.Nullable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Objects;

public class WeakReferenceValueIdentity<T>
		extends WeakReference<T> {
	public WeakReferenceValueIdentity(T referent) { super(referent); }

	public WeakReferenceValueIdentity(T referent, ReferenceQueue<? super T> queue) { super(referent, queue); }

	@Override
	public int hashCode() { return Objects.hashCode(get()); }

	@SuppressWarnings({"ObjectEquality", "EqualsWhichDoesntCheckParameterClass"})
	@Override
	public boolean equals(@Nullable Object obj) {
		return CastUtilities.castChecked(WeakReference.class, obj)
				.map(Reference::get)
				.filter(oc -> get() == oc)
				.isPresent();
	}
}
