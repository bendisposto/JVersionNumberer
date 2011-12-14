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

/**
 * A shallow delta, e.g. of two Strings.
 * 
 * @author Gian Perrone
 * 
 * @param <T>
 *            the type of the delta
 */
public class ShallowDelta<T> extends AbstractDelta<T> {

	static class Factory<T> extends DeltaFactory<T, ShallowDelta<T>> {

		private static Factory<Object> instance;

		@SuppressWarnings("unchecked")
		static <T> Factory<T> getInstance(Class<T> clazz) {

			if (instance == null)
				instance = new ShallowDelta.Factory<Object>();

			return (Factory<T>) instance;

		}

		@Override
		ShallowDelta<T> createDelta(T from, T to) {

			return new ShallowDelta<T>(from, to);

		}

	}

	ShallowDelta(T from, T to) {

		super(from, to);

		if (from == null || to == null)
			return;

		if (!from.equals(to))
			setChanged(true);

	}

}
