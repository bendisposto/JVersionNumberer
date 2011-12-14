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
 * 
 * The visibility of a entity.
 * 
 * @author Gian Perrone
 * 
 */
public enum Visibility {

	/**
	 * Package visibility, toString() returns null.
	 */
	PACKAGE(null),
	/**
	 * Private visibility, toString() returns "private".
	 */
	PRIVATE("private"),
	/**
	 * Protected visibility, toString() returns "protected".
	 */
	PROTECTED("protected"),
	/**
	 * Public visibility, toString() returns "public".
	 */
	PUBLIC("public");

	private String str;

	private Visibility(String str) {

		this.str = str;

	}

	/**
	 * Returns a string description of the visibility. It is formatted like
	 * a visibility modifier in a declaration.
	 * 
	 * @return the description
	 */
	@Override
	public String toString() {

		return str;

	}

}