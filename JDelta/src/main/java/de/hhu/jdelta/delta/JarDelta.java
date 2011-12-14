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

import com.google.common.collect.ImmutableSortedMap;

import de.hhu.jdelta.tree.JarNode;

/**
 * A Jar delta.
 * 
 * Contains ClassDeltas.
 * 
 * @author Gian Perrone
 * 
 */
public class JarDelta extends AbstractDelta<JarNode> {

	private ImmutableSortedMap<String, ClassDelta> classDeltas;

	/**
	 * Generates a delta between two JarNodes
	 * 
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 */
	public JarDelta(JarNode from, JarNode to) {

		super(from, to);

		this.classDeltas =
				createClassDeltaMap(from.getClasses(), to.getClasses());

	}

	/**
	 * Returns a multi-line description of the jar delta including its classes
	 * and recursively its fields and methods.
	 * 
	 * @return the description
	 */
	public String getDeepDeclarationsDiff() {

		StringBuilder str = new StringBuilder();

		for (ClassDelta classDelta : classDeltas.values()) {

			str.append(classDelta.getDeepDeclarationsDiff()).append("\n");

		}

		return str.toString();

	}

	/**
	 * Returns the class deltas.
	 * 
	 * @return the classDeltas
	 */
	public ImmutableSortedMap<String, ClassDelta> getClassDeltas() {
		return classDeltas;
	}

}
