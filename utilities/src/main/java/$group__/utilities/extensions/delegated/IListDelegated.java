package $group__.utilities.extensions.delegated;

import java.util.*;
import java.util.function.UnaryOperator;

public interface IListDelegated<L extends List<E>, E> extends List<E>, ICollectionDelegated<L, E> {
	@Override
	@Deprecated
	default L getCollection() { return getList(); }

	@Override
	@Deprecated
	default void setCollection(L collection) { setList(collection); }

	L getList();

	void setList(L list);

	@SuppressWarnings("EmptyMethod")
	@Override
	default int size() { return ICollectionDelegated.super.size(); }

	@SuppressWarnings("EmptyMethod")
	@Override
	default boolean isEmpty() { return ICollectionDelegated.super.isEmpty(); }

	@Override
	default boolean contains(Object o) { return ICollectionDelegated.super.contains(o); }

	@Override
	default Iterator<E> iterator() { return ICollectionDelegated.super.iterator(); }

	@SuppressWarnings("EmptyMethod")
	@Override
	default Object[] toArray() { return ICollectionDelegated.super.toArray(); }

	@SuppressWarnings("SuspiciousToArrayCall")
	@Override
	default <A> A[] toArray(A[] a) { return ICollectionDelegated.super.toArray(a); }

	@Override
	default boolean add(E e) { return ICollectionDelegated.super.add(e); }

	@Override
	default boolean remove(Object o) { return ICollectionDelegated.super.remove(o); }


	@Override
	default boolean containsAll(Collection<?> c) { return ICollectionDelegated.super.containsAll(c); }

	@Override
	default boolean addAll(Collection<? extends E> c) { return ICollectionDelegated.super.addAll(c); }

	@Override
	default boolean addAll(int index, Collection<? extends E> c) { return getList().addAll(index, c); }

	@Override
	default boolean removeAll(Collection<?> c) { return ICollectionDelegated.super.removeAll(c); }

	@Override
	default boolean retainAll(Collection<?> c) { return ICollectionDelegated.super.retainAll(c); }

	@Override
	default void replaceAll(UnaryOperator<E> operator) { getList().replaceAll(operator); }

	@Override
	default void sort(Comparator<? super E> c) { getList().sort(c); }

	@SuppressWarnings("EmptyMethod")
	@Override
	default void clear() { ICollectionDelegated.super.clear(); }


	@Override
	boolean equals(Object o);

	@Override
	int hashCode();


	@Override
	default E get(int index) { return getList().get(index); }

	@Override
	default E set(int index, E element) { return getList().set(index, element); }

	@Override
	default void add(int index, E element) { getList().add(index, element); }

	@Override
	default E remove(int index) { return getList().remove(index); }


	@Override
	default int indexOf(Object o) { return getList().indexOf(o); }

	@Override
	default int lastIndexOf(Object o) { return getList().lastIndexOf(o); }


	@Override
	default ListIterator<E> listIterator() { return getList().listIterator(); }

	@Override
	default ListIterator<E> listIterator(int index) { return getList().listIterator(index); }


	@Override
	default List<E> subList(int fromIndex, int toIndex) { return getList().subList(fromIndex, toIndex); }

	@Override
	default Spliterator<E> spliterator() { return ICollectionDelegated.super.spliterator(); }
}
