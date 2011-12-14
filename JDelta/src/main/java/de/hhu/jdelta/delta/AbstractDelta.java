/*
 *   Copyright 2011 Gian Perrone
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.hhu.jdelta.delta;

import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;

import de.hhu.jdelta.tree.ClassNode;
import de.hhu.jdelta.tree.FieldNode;
import de.hhu.jdelta.tree.MethodNode;

/**
 * An abstract implementation of all object deltas.
 * 
 * This does not include BooleanDelta.
 * 
 * @author Gian Perrone
 * 
 * @param <T>
 *            the type of the delta
 */
public abstract class AbstractDelta<T> implements IDelta {

	private T from;
	private T to;

	private boolean changed = false;
	private boolean added = false;
	private boolean deleted = false;

	AbstractDelta(T from, T to) {

		this.from = from;
		this.to = to;

		if (from == null && to == null)
			return;

		if (from == null) {

			added = true;

		} else if (to == null) {

			deleted = true;

		}

	}

	BooleanDelta createDelta(boolean from, boolean to) {

		BooleanDelta delta = new BooleanDelta(from, to);

		if (delta.isChanged())
			changed = true;

		return delta;

	}

	private <V, D extends AbstractDelta<V>> D createDelta(V from, V to,
			DeltaFactory<V, D> factory) {

		D delta = factory.createDelta(from, to);

		if (delta.isChanged())
			changed = true;

		return delta;

	}

	<V> ShallowDelta<V> createDelta(V from, V to, Class<V> clazz) {

		return createDelta(from, to, ShallowDelta.Factory.getInstance(clazz));

	}

	ClassDelta createClassDelta(ClassNode from, ClassNode to) {

		return createDelta(from, to, ClassDelta.Factory.getInstance());

	}

	MethodDelta createMethodDelta(MethodNode from, MethodNode to) {

		return createDelta(from, to, MethodDelta.Factory.getInstance());

	}

	FieldDelta createFieldDelta(FieldNode from, FieldNode to) {

		return createDelta(from, to, FieldDelta.Factory.getInstance());

	}

	private <V, D extends AbstractDelta<V>> ImmutableSortedMap<V, D> createDeltaMap(
			ImmutableSortedSet<V> from, ImmutableSortedSet<V> to,
			DeltaFactory<V, D> factory) {

		SortedSet<V> both = new TreeSet<V>();
		both.addAll(from);
		both.addAll(to);

		SortedMap<V, D> map = new TreeMap<V, D>();

		for (V value : both) {

			V fromValue = from.contains(value) ? value : null;
			V toValue = to.contains(value) ? value : null;

			D delta = factory.createDelta(fromValue, toValue);

			map.put(value, delta);

			if (delta.isChanged())
				changed = true;

		}

		return ImmutableSortedMap.copyOf(map);

	}

	<V> ImmutableSortedMap<V, ShallowDelta<V>> createDeltaMap(
			ImmutableSortedSet<V> from, ImmutableSortedSet<V> to, Class<V> clazz) {

		return createDeltaMap(from, to, ShallowDelta.Factory.getInstance(clazz));

	}

	private <K, V, D extends AbstractDelta<V>> ImmutableSortedMap<K, D> createDeltaMap(
			ImmutableSortedMap<K, V> from, ImmutableSortedMap<K, V> to,
			DeltaFactory<V, D> factory) {

		SortedSet<K> both = new TreeSet<K>();
		both.addAll(from.keySet());
		both.addAll(to.keySet());

		SortedMap<K, D> map = new TreeMap<K, D>();

		for (K key : both) {

			D delta = factory.createDelta(from.get(key), to.get(key));

			map.put(key, delta);

			if (delta.isChanged())
				changed = true;

		}

		return ImmutableSortedMap.copyOf(map);

	}

	ImmutableSortedMap<String, ClassDelta> createClassDeltaMap(
			ImmutableSortedMap<String, ClassNode> from,
			ImmutableSortedMap<String, ClassNode> to) {

		return createDeltaMap(from, to, ClassDelta.Factory.getInstance());

	}

	ImmutableSortedMap<String, MethodDelta> createMethodDeltaMap(
			ImmutableSortedMap<String, MethodNode> from,
			ImmutableSortedMap<String, MethodNode> to) {

		return createDeltaMap(from, to, MethodDelta.Factory.getInstance());

	}

	ImmutableSortedMap<String, FieldDelta> createFieldDeltaMap(
			ImmutableSortedMap<String, FieldNode> from,
			ImmutableSortedMap<String, FieldNode> to) {

		return createDeltaMap(from, to, FieldDelta.Factory.getInstance());

	}

	/**
	 * Returns the from.
	 * 
	 * @return the from
	 */
	public T getFrom() {
		return from;
	}

	/**
	 * Returns the to.
	 * 
	 * @return the to
	 */
	public T getTo() {
		return to;
	}

	void setChanged(boolean changed) {
		this.changed = changed;
	}

	@Override
	public boolean isUnchanged() {

		return !(changed || added || deleted);

	}

	@Override
	public boolean isChanged() {
		return changed;
	}

	@Override
	public boolean isAdded() {
		return added;
	}

	@Override
	public boolean isDeleted() {
		return deleted;
	}

}
