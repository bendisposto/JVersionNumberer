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

import de.hhu.jdelta.tree.FieldNode;
import de.hhu.jdelta.tree.Visibility;

/**
 * A field delta.
 * 
 * @author Gian Perrone
 * 
 */
public class FieldDelta extends EntityDelta<FieldNode> {

	private ShallowDelta<String> descDelta;
	private ShallowDelta<String> signatureDelta;
	private ShallowDelta<Object> valueDelta;

	private ShallowDelta<Visibility> visibilityDelta;
	private BooleanDelta staticDelta;
	private BooleanDelta finalDelta;
	private BooleanDelta volatileDelta;
	private BooleanDelta transientDelta;
	private BooleanDelta syntheticDelta;
	private BooleanDelta enumDelta;

	FieldDelta(FieldNode from, FieldNode to) {

		super(from, to);

		if (from == null)
			from = FieldNode.EMPTY_FIELD_NODE;

		if (to == null)
			to = FieldNode.EMPTY_FIELD_NODE;

		this.descDelta =
				createDelta(from.getDesc(), to.getDesc(), String.class);
		this.signatureDelta =
				createDelta(from.getSignature(), to.getSignature(),
						String.class);
		this.valueDelta =
				createDelta(from.getValue(), to.getValue(), Object.class);

		this.visibilityDelta =
				createDelta(from.getVisibility(), to.getVisibility(),
						Visibility.class);
		this.staticDelta = createDelta(from.isStatic(), to.isStatic());
		this.finalDelta = createDelta(from.isFinal(), to.isFinal());
		this.volatileDelta = createDelta(from.isVolatile(), to.isVolatile());
		this.transientDelta = createDelta(from.isTransient(), to.isTransient());
		this.syntheticDelta = createDelta(from.isSynthetic(), to.isSynthetic());
		this.enumDelta = createDelta(from.isEnum(), to.isEnum());

	}

	static class Factory extends DeltaFactory<FieldNode, FieldDelta> {

		private static Factory instance;

		Factory() {

		}

		static Factory getInstance() {

			if (instance == null)
				instance = new Factory();

			return instance;

		}

		@Override
		FieldDelta createDelta(FieldNode from, FieldNode to) {

			return new FieldDelta(from, to);

		}

	}

	/**
	 * Returns the description delta.
	 * 
	 * @return the description delta
	 */
	public ShallowDelta<String> getDescDelta() {
		return descDelta;
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
	 * Returns the value delta.
	 * 
	 * @return the value delta
	 */
	public ShallowDelta<Object> getValueDelta() {
		return valueDelta;
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
	 * Returns the static delta.
	 * 
	 * @return the static delta
	 */
	public BooleanDelta getStaticDelta() {
		return staticDelta;
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
	 * Returns the volatile delta.
	 * 
	 * @return the volatile delta
	 */
	public BooleanDelta getVolatileDelta() {
		return volatileDelta;
	}

	/**
	 * Returns the transient delta.
	 * 
	 * @return the transient delta
	 */
	public BooleanDelta getTransientDelta() {
		return transientDelta;
	}

	/**
	 * Returns the synthetic delta.
	 * 
	 * @return the synthetic delta
	 */
	public BooleanDelta getSyntheticDelta() {
		return syntheticDelta;
	}

	/**
	 * Returns the enum delta.
	 * 
	 * @return the enum delta
	 */
	public BooleanDelta getEnumDelta() {
		return enumDelta;
	}

}
