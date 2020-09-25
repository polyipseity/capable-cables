package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.processors.utilities;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import org.slf4j.Marker;

import javax.annotation.Nullable;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public enum ProcessorUtilities {
	;

	private static final Marker CLASS_MARKER = UtilitiesMarkers.getInstance().getClassMarker();
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());

	public static <A extends Annotation> A getEffectiveAnnotationWithInheritingConsidered(Class<A> annotationType, ExecutableElement executable, Elements elements, Types types) throws IllegalArgumentException {
		A[] r = getEffectiveAnnotationsWithInheritingConsidered(annotationType, executable, elements, types);
		if (r.length != 1)
			throw new IllegalArgumentException(
					new LogMessageBuilder()
							.addMarkers(ProcessorUtilities::getClassMarker)
							.addKeyValue("annotationType", annotationType).addKeyValue("executable", executable).addKeyValue("elements", elements).addKeyValue("types", types)
							.addMessages(() -> getResourceBundle().getString("annotations.get.plural.fail"))
							.build()
			);
		return r[0];
	}

	public static Marker getClassMarker() { return CLASS_MARKER; }

	public static boolean isElementAbstract(Element element) { return element.getModifiers().contains(Modifier.ABSTRACT) || element.getKind().isInterface(); }

	public static boolean isElementFinal(Element element) { return element.getModifiers().contains(Modifier.FINAL); }

	public static ImmutableSet<TypeElement> getThisAndSuperclasses(TypeElement type, Types types) { return getLowerAndIntermediateSuperclasses(type, null, types); }

	public static ImmutableSet<TypeElement> getLowerAndIntermediateSuperclasses(@Nullable TypeElement lower, @Nullable TypeElement upper, Types types) {
		ImmutableSet.Builder<TypeElement> r = new ImmutableSet.Builder<>();
		for (@Nullable TypeElement i = lower; !Objects.equals(i, upper) && i != null; i =
				(TypeElement) types.asElement(i.getSuperclass()))
			r.add(i);
		return r.build();
	}

	public static ImmutableSet<ImmutableSet<TypeElement>> getThisAndSuperclassesAndInterfaces(TypeElement type, Types types) { return new ImmutableSet.Builder<ImmutableSet<TypeElement>>().add(ImmutableSet.of(type)).addAll(getSuperclassesAndInterfaces(type, types)).build(); }

	@SuppressWarnings({"UnstableApiUsage", "ObjectAllocationInLoop"})
	public static ImmutableSet<ImmutableSet<TypeElement>> getSuperclassesAndInterfaces(TypeElement type, Types types) {
		LinkedHashSet<ImmutableSet<TypeElement>> ret = new LinkedHashSet<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);

		ImmutableSet<TypeElement> scs = getSuperclasses(type, types);
		ret.add(scs);
		AtomicReference<List<TypeElement>> cur = new AtomicReference<>(type.getInterfaces().stream().sequential().map(m -> (TypeElement) types.asElement(m)).collect(ImmutableList.toImmutableList()));
		scs.forEach(sc -> {
			List<TypeElement> next = sc.getInterfaces().stream().sequential()
					.map(types::asElement)
					.map(TypeElement.class::cast)
					.collect(ImmutableList.toImmutableList());
			ret.add(ImmutableSet.copyOf(AssertionUtilities.assertNonnull(cur.getAndUpdate(c -> {
				List<TypeElement> n = new ArrayList<>(next.size() + c.size() * CapacityUtilities.INITIAL_CAPACITY_TINY);
				n.addAll(next);
				c.forEach(t ->
						n.addAll(t.getInterfaces().stream().sequential()
								.map(types::asElement)
								.map(TypeElement.class::cast)
								.collect(ImmutableList.toImmutableList())));
				return n;
			}))));
		});
		while (!AssertionUtilities.assertNonnull(cur.get()).isEmpty()) {
			ret.add(ImmutableSet.copyOf(AssertionUtilities.assertNonnull(cur.getAndUpdate(s -> {
				List<TypeElement> n = new ArrayList<>(s.size() * CapacityUtilities.INITIAL_CAPACITY_TINY);
				s.forEach(t ->
						n.addAll(t.getInterfaces().stream().sequential()
								.map(types::asElement)
								.map(TypeElement.class::cast)
								.collect(ImmutableList.toImmutableList())));
				return n;
			}))));
		}

		ret.removeIf(Collection::isEmpty);
		return ImmutableSet.copyOf(ret);
	}

	public static ImmutableSet<TypeElement> getSuperclasses(TypeElement type, Types types) { return getIntermediateSuperclasses(type, null, types); }

	public static ImmutableSet<TypeElement> getIntermediateSuperclasses(TypeElement lower, @Nullable TypeElement upper, Types types) { return getLowerAndIntermediateSuperclasses((TypeElement) types.asElement(lower.getSuperclass()), upper, types); }

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	public static <A extends Annotation> A[] getEffectiveAnnotationsWithInheritingConsidered(Class<A> annotationType, ExecutableElement executable, Elements elements, Types types) {
		A[] r = executable.getAnnotationsByType(annotationType);
		if (r.length != 0) return r;

		TypeElement type = (TypeElement) executable.getEnclosingElement();
		sss:
		for (ImmutableSet<TypeElement> ss : getSuperclassesAndInterfaces(type, types)) {
			for (TypeElement s : ss) {
				for (Element m : s.getEnclosedElements()) {
					if (m.getKind() == ElementKind.METHOD) {
						ExecutableElement m1 = (ExecutableElement) m;
						if (elements.overrides(executable, m1, type)) {
							r = m1.getAnnotationsByType(annotationType);
							break;
						}
					}
				}
				if (r.length != 0) break sss;
			}
		}

		return r;
	}
}