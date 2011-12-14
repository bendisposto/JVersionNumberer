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
 * A boolean delta.
 * 
 * @author Gian Perrone
 * 
 */
public class BooleanDelta implements IDelta {

	private boolean from;
	private boolean to;

	private boolean changed;
	private boolean added;
	private boolean deleted;

	BooleanDelta(boolean from, boolean to) {

		this.from = from;
		this.to = to;

		changed = (from != to);
		added = (!from && to);
		deleted = (from && !to);

	}

	/**
	 * Returns the from.
	 * 
	 * @return the from
	 */
	public boolean getFrom() {
		return from;
	}

	/**
	 * Returns the to.
	 * 
	 * @return the to
	 */
	public boolean getTo() {
		return to;
	}

	@Override
	public boolean isUnchanged() {
		return (from == to);
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
