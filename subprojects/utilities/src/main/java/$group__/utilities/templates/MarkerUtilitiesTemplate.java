package $group__.utilities.templates;

import $group__.utilities.CapacityUtilities;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.collections.MapUtilities;
import $group__.utilities.structures.Singleton;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.concurrent.ConcurrentMap;

public abstract class MarkerUtilitiesTemplate extends Singleton {
	public static final String SEPARATOR = "-";

	protected final String namespace;
	protected final Marker markerUnmarked = getMarker("unmarked");
	protected final Marker markerClass = getMarker("class");
	protected final Marker markerStructure = getMarker("structure");
	protected final ConcurrentMap<Class<?>, Marker> classMarkerConcurrentMap =
			MapUtilities.newMapMakerNormalThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_LARGE).weakKeys().makeMap();

	protected MarkerUtilitiesTemplate(String namespace, Logger logger) {
		super(logger);
		this.namespace = namespace;
	}

	public final Marker getMarker(String string) { return MarkerManager.getMarker(getNamespacePrefixedString(string)); }

	public String getNamespacePrefixedString(String string) { return NamespaceUtilities.getNamespacePrefixedString(SEPARATOR, getNamespace(), string); }

	protected String getNamespace() { return namespace; }

	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	public Marker getClassMarker(Class<?> clazz) {
		synchronized (clazz) {
			return getClassMarkerConcurrentMap().computeIfAbsent(clazz, key ->
					MarkerManager.getMarker(getNamespacePrefixedString(
							key.getSimpleName() + '@' + Integer.toHexString(key.getName().hashCode())))
							.addParents(getMarkerClass()));
		}
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<Class<?>, Marker> getClassMarkerConcurrentMap() { return classMarkerConcurrentMap; }

	public Marker getMarkerClass() { return markerClass; }

	public Marker getMarkerUnmarked() { return markerUnmarked; }

	public Marker getMarkerStructure() { return markerStructure; }
}
