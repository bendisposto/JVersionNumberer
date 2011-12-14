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

import de.hhu.jdelta.tree.ClassNode;
import de.hhu.jdelta.tree.Visibility;
import de.hhu.jdelta.tree.ClassNode.ClassType;

/**
 * A class delta.
 * 
 * @author Gian Perrone
 * 
 */
public class ClassDelta extends EntityDelta<ClassNode> {

	private ImmutableSortedMap<String, FieldDelta> fieldDeltas;
	private ImmutableSortedMap<String, MethodDelta> methodDeltas;
	// private SortedMap<String, Boolean> innerClassesDelta;

	private ImmutableSortedMap<String, ShallowDelta<String>> interfaceDeltas;
	private ImmutableSortedMap<String, ShallowDelta<String>> annotationDeltas;

	private ShallowDelta<Integer> versionDelta;
	private ShallowDelta<String> signatureDelta;
	private ShallowDelta<String> superNameDelta;

	private ShallowDelta<Visibility> visibilityDelta;
	private ShallowDelta<ClassType> classTypeDelta;
	private BooleanDelta finalDelta;
	private BooleanDelta superDelta;

	/**
	 * Generates a class delta between two ClassNodes.
	 * 
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 */
	public ClassDelta(ClassNode from, ClassNode to) {

		super(from, to);

		if (from == null)
			from = ClassNode.EMPTY_CLASS_NODE;

		if (to == null)
			to = ClassNode.EMPTY_CLASS_NODE;

		this.fieldDeltas =
				createFieldDeltaMap(from.getFields(), to.getFields());
		this.methodDeltas =
				createMethodDeltaMap(from.getMethods(), to.getMethods());
		// this.innerClassesDelta =
		// changedMap(from.getInnerClasses(), to.getInnerClasses());

		this.interfaceDeltas =
				createDeltaMap(from.getInterfaces(), to.getInterfaces(),
						String.class);
		this.annotationDeltas =
				createDeltaMap(from.getAnnotations(), to.getAnnotations(),
						String.class);

		this.versionDelta =
				createDelta(from.getVersion(), to.getVersion(), Integer.class);
		this.signatureDelta =
				createDelta(from.getSignature(), to.getSignature(),
						String.class);
		this.superNameDelta =
				createDelta(from.getSuperName(), to.getSuperName(),
						String.class);

		this.visibilityDelta =
				createDelta(from.getVisibility(), to.getVisibility(),
						Visibility.class);
		this.classTypeDelta =
				createDelta(from.getClassType(), to.getClassType(),
						ClassType.class);
		this.finalDelta = createDelta(from.isFinal(), to.isFinal());
		this.superDelta = createDelta(from.isSuper(), to.isSuper());

	}

	static class Factory extends DeltaFactory<ClassNode, ClassDelta> {

		private static Factory instance;

		Factory() {

		}

		static Factory getInstance() {

			if (instance == null)
				instance = new Factory();

			return instance;

		}

		@Override
		ClassDelta createDelta(ClassNode from, ClassNode to) {

			return new ClassDelta(from, to);

		}

	}

	/**
	 * Returns a multi-line description of the class delta including its fields
	 * and methods.
	 * 
	 * @return the description
	 */
	public String getDeepDeclarationsDiff() {

		StringBuilder str = new StringBuilder();

		str.append(getDeclarationDiff()).append("\n");

		if (isAdded() || isDeleted())
			return str.toString();

		for (FieldDelta fieldDelta : fieldDeltas.values()) {

			str.append(" ")
					.append(fieldDelta
							.getDeclarationDiff()
							.replace("\n", "\n "))
					.append("\n");

		}

		for (MethodDelta methodDelta : methodDeltas.values()) {

			str.append(" ")
					.append(methodDelta.getDeclarationDiff().replace("\n",
							"\n "))
					.append("\n");

		}

		return str.toString();

	}

	/**
	 * Returns the field deltas.
	 * 
	 * @return the field deltas
	 */
	public ImmutableSortedMap<String, FieldDelta> getFieldDeltas() {
		return fieldDeltas;
	}

	/**
	 * Returns the method deltas.
	 * 
	 * @return the method deltas
	 */
	public ImmutableSortedMap<String, MethodDelta> getMethodDeltas() {
		return methodDeltas;
	}

	// /**
	// * @return the innerClassesDelta
	// */
	// public SortedMap<String, Boolean> getInnerClassesDelta() {
	// return innerClassesDelta;
	// }

	/**
	 * Returns the interface deltas.
	 * 
	 * @return the interfaceDeltas
	 */
	public ImmutableSortedMap<String, ShallowDelta<String>> getInterfaceDeltas() {
		return interfaceDeltas;
	}

	/**
	 * @return the annotationDeltas
	 */
	public ImmutableSortedMap<String, ShallowDelta<String>> getAnnotationDeltas() {
		return annotationDeltas;
	}

	/**
	 * Returns the version delta.
	 * 
	 * @return the version delta
	 */
	public ShallowDelta<Integer> getVersionDelta() {
		return versionDelta;
	}

	/**
	 * Returns the signature delta.
	 * 
	 * @return the signature delta
	 */
	public ShallowDelta<String> getSignatureDelta() {
		return signatureDelta;
	}

	/**
	 * Returns the super name delta.
	 * 
	 * @return the super name delta
	 */
	public ShallowDelta<String> getSuperNameDelta() {
		return superNameDelta;
	}

	/**
	 * Returns the visibility delta.
	 * 
	 * @return the visibility delta
	 */
	public ShallowDelta<Visibility> getVisibilityDelta() {
		return visibilityDelta;
	}

	/**
	 * Returns the class type delta.
	 * 
	 * @return the class type delta
	 */
	public ShallowDelta<ClassType> getClassTypeDelta() {
		return classTypeDelta;
	}

	/**
	 * Returns the final delta.
	 * 
	 * @return the final delta
	 */
	public BooleanDelta getFinalDelta() {
		return finalDelta;
	}

	/**
	 * Returns the super delta.
	 * 
	 * @return the super delta
	 */
	public BooleanDelta getSuperDelta() {
		return superDelta;
	}

}
