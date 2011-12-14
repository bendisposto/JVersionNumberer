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

package de.hhu.jdelta.tree;

/**
 * A named entity.
 * 
 * @author Gian Perrone
 * 
 */
public interface IEntityNode {

	/**
	 * Returns the name of the entity.
	 * 
	 * @return the name
	 */
	public abstract String getName();

	/**
	 * Return the visibility of the entity.
	 * 
	 * @return the visibility
	 */
	public abstract Visibility getVisibility();

	/**
	 * Returns a string description of the entity which looks like a declaration
	 * with fully qualified names.
	 * 
	 * @return the description
	 */
	public abstract String getDeclarationString();

}
