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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A node that represents a named entity.
 * 
 * @author Gian Perrone
 */
public abstract class AbstractNode implements IEntityNode {

	@Override
	public abstract String getName();

	@Override
	public abstract Visibility getVisibility();

	@Override
	public abstract String getDeclarationString();

	/**
	 * Returns a multi-line string containing all fields for debugging purposes.
	 * This includes private ones.
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {

		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);

	}

}
