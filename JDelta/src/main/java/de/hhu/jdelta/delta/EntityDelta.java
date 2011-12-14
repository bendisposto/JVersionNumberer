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

import de.hhu.jdelta.tree.IEntityNode;
import de.hhu.jdelta.tree.Visibility;

/**
 * An abstract entity delta.
 * 
 * @author Gian Perrone
 * 
 * @param <T>
 *            the delta type
 */
public abstract class EntityDelta<T extends IEntityNode> extends
		AbstractDelta<T> {

	EntityDelta(T from, T to) {

		super(from, to);

	}

	/**
	 * Returns the visibility.
	 * 
	 * @return the visibility
	 */
	public abstract ShallowDelta<Visibility> getVisibilityDelta();

	/**
	 * Returns a string description of the differences between the entities
	 * which looks like a declaration with fully qualified names.
	 * 
	 * @return the description
	 */
	public String getDeclarationDiff() {

		StringBuilder str = new StringBuilder();

		if (isAdded())
			return str
					.append("+")
					.append(getTo().getDeclarationString())
					.toString();

		if (isDeleted())
			return str
					.append("-")
					.append(getFrom().getDeclarationString())
					.toString();

		if (isChanged())
			return str
					.append("-")
					.append(getFrom().getDeclarationString())
					.append("\n+")
					.append(getTo().getDeclarationString())
					.toString();

		return str
				.append(" ")
				.append(getFrom().getDeclarationString())
				.toString();

	}

}
