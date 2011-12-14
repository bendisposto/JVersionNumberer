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
 * A Delta.
 * 
 * @author Gian Perrone
 * 
 */
public interface IDelta {

	/**
	 * Returns true if the object is neither changed, nor added, nor deleted.
	 * 
	 * @return the unchanged flag
	 */
	public abstract boolean isUnchanged();

	/**
	 * Returns true if the object is changed.
	 * 
	 * @return the changed flag
	 */
	public abstract boolean isChanged();

	/**
	 * Returns true if the object is added.
	 * 
	 * @return the added flag
	 */
	public abstract boolean isAdded();

	/**
	 * Returns true if the object ist deleted.
	 * 
	 * @return the deleted flag
	 */
	public abstract boolean isDeleted();

}