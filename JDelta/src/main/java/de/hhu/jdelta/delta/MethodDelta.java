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

import de.hhu.jdelta.tree.MethodNode;
import de.hhu.jdelta.tree.Visibility;

/**
 * A method node.
 * 
 * @author Gian Perrone
 * 
 */
public class MethodDelta extends EntityDelta<MethodNode> {

	private ShallowDelta<String> descDelta;
	private ShallowDelta<String> signatureDelta;
	private ImmutableSortedMap<String, ShallowDelta<String>> exceptionDeltas;

	private ShallowDelta<Visibility> visibilityDelta;
	private BooleanDelta staticDelta;
	private BooleanDelta finalDelta;
	private BooleanDelta synchronizedDelta;
	private BooleanDelta bridgeDelta;
	private BooleanDelta varargsDelta;
	private BooleanDelta nativeDelta;
	private BooleanDelta abstractDelta;
	private BooleanDelta strictfpDelta;

	MethodDelta(MethodNode from, MethodNode to) {

		super(from, to);

		if (from == null)
			from = MethodNode.EMPTY_METHOD_NODE;

		if (to == null)
			to = MethodNode.EMPTY_METHOD_NODE;

		this.descDelta =
				createDelta(from.getDesc(), to.getDesc(), String.class);
		this.signatureDelta =
				createDelta(from.getSignature(), to.getSignature(),
						String.class);
		this.exceptionDeltas =
				createDeltaMap(from.getExceptions(), to.getExceptions(),
						String.class);

		this.visibilityDelta =
				createDelta(from.getVisibility(), to.getVisibility(),
						Visibility.class);
		this.staticDelta = createDelta(from.isStatic(), to.isStatic());
		this.finalDelta = createDelta(from.isFinal(), to.isFinal());
		this.synchronizedDelta =
				createDelta(from.isSynchronized(), to.isSynchronized());
		this.bridgeDelta = createDelta(from.isBridge(), to.isBridge());
		this.varargsDelta = createDelta(from.isVarargs(), to.isVarargs());
		this.nativeDelta = createDelta(from.isNative(), to.isNative());
		this.abstractDelta = createDelta(from.isAbstract(), to.isAbstract());
		this.strictfpDelta = createDelta(from.isStrictfp(), to.isStrictfp());

	}

	static class Factory extends DeltaFactory<MethodNode, MethodDelta> {

		private static Factory instance;

		Factory() {

		}

		static Factory getInstance() {

			if (instance == null)
				instance = new Factory();

			return instance;

		}

		@Override
		MethodDelta createDelta(MethodNode from, MethodNode to) {

			return new MethodDelta(from, to);

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
	 * Returns the exception deltas.
	 * 
	 * @return the exception deltas
	 */
	public ImmutableSortedMap<String, ShallowDelta<String>> getExceptionDeltas() {
		return exceptionDeltas;
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
	 * Returns the synchronized delta.
	 * 
	 * @return the synchronized delta
	 */
	public BooleanDelta getSynchronizedDelta() {
		return synchronizedDelta;
	}

	/**
	 * Returns the bridge delta.
	 * 
	 * @return the bridge delta
	 */
	public BooleanDelta getBridgeDelta() {
		return bridgeDelta;
	}

	/**
	 * Returns the varargs delta.
	 * 
	 * @return the varargs delta
	 */
	public BooleanDelta getVarargsDelta() {
		return varargsDelta;
	}

	/**
	 * Returns the native delta.
	 * 
	 * @return the native delta
	 */
	public BooleanDelta getNativeDelta() {
		return nativeDelta;
	}

	/**
	 * Returns the abstract delta.
	 * 
	 * @return the abstract delta
	 */
	public BooleanDelta getAbstractDelta() {
		return abstractDelta;
	}

	/**
	 * Returns the strictfp delta.
	 * 
	 * @return the strictfp delta
	 */
	public BooleanDelta getStrictfpDelta() {
		return strictfpDelta;
	}

}
