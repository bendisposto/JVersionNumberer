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

package de.hhu.jversionnumberer;

import de.hhu.jdelta.delta.ClassDelta;
import de.hhu.jdelta.delta.EntityDelta;
import de.hhu.jdelta.delta.FieldDelta;
import de.hhu.jdelta.delta.JarDelta;
import de.hhu.jdelta.delta.MethodDelta;
import de.hhu.jdelta.delta.ShallowDelta;
import de.hhu.jdelta.tree.Visibility;
import de.hhu.jdelta.tree.ClassNode.ClassType;

/**
 * A tree walker which decides which segment of a version number has to be
 * incremented.
 * 
 * @author Gian Perrone
 * 
 */
public class VersionNumberWalker {

	private boolean minor = false;
	private boolean major = false;

	private boolean clientExtendable = false;

	/**
	 * Examine a JarDelta.
	 * 
	 * @param delta
	 *            the jar delta
	 */
	public VersionNumberWalker(JarDelta delta) {

		processJar(delta);

	}

	/**
	 * Examine a ClassDelta.
	 * 
	 * @param delta
	 *            the class delta
	 */
	public VersionNumberWalker(ClassDelta delta) {

		processClass(delta);

	}

	/**
	 * Returns true if the minor segment has to be incremented.
	 * 
	 * @return the minor flag
	 */
	public boolean isMinor() {
		return minor;
	}

	/**
	 * Returns true if the major segment has to be incremented.
	 * 
	 * @return the major flag
	 */
	public boolean isMajor() {
		return major;
	}

	public void processJar(JarDelta delta) {

		for (ClassDelta classDelta : delta.getClassDeltas().values()) {

			processClass(classDelta);

		}

	}

	public void processClass(ClassDelta delta) {

		if (isEffectivelyIrrelevant(delta))
			return;

		if (isEffectivelyAdded(delta)) {
			minor = true;
			return;
		}

		if (isEffectivelyDeleted(delta)) {
			major = true;
			return;
		}

		if (delta
				.getTo()
				.getAnnotations()
				.contains(
						"Lde/hhu/jversionnumberer/annotations/ExtentableByClient;")
				|| delta.getTo()
						.getAnnotations()
						.contains(
								"Lde/hhu/jversionnumberer/annotations/ImplementableByClient;"))
			clientExtendable = true;

		if (delta.getClassTypeDelta().isChanged()) {
			if (delta.getClassTypeDelta().getFrom() == ClassType.ABSTRACT_CLASS
					&& delta.getClassTypeDelta().getTo() == ClassType.CLASS)
				minor = true;
			else
				major = true;
		}

		if (delta.getFinalDelta().isAdded())
			major = true;

		if (!delta.getSignatureDelta().isUnchanged())
			major = true;

		if (delta.getSuperNameDelta().isChanged())
			major = true;

		for (ShallowDelta<String> interfaceDelta : delta
				.getInterfaceDeltas()
				.values()) {

			if (interfaceDelta.isDeleted())
				major = true;

			if (interfaceDelta.isAdded()) {
				if (!clientExtendable)
					minor = true;
				else
					major = true;
			}

		}

		for (FieldDelta fieldDelta : delta.getFieldDeltas().values()) {

			processField(fieldDelta);

		}

		for (MethodDelta methodDelta : delta.getMethodDeltas().values()) {

			processMethod(methodDelta);

		}

	}

	private void processField(FieldDelta delta) {

		if (isEffectivelyIrrelevant(delta))
			return;

		if (isEffectivelyAdded(delta)) {
			if (!clientExtendable)
				minor = true;
			else
				major = true;
			return;
		}

		if (isEffectivelyDeleted(delta)) {
			major = true;
			return;
		}

		if (delta.getDescDelta().isChanged())
			major = true;

		if (!delta.getValueDelta().isUnchanged())
			major = true;

		if (delta.getStaticDelta().isChanged())
			major = true;

		if (delta.getFinalDelta().isAdded())
			major = true;

		if (delta.getFinalDelta().isDeleted()) {
			if (delta.getStaticDelta().getTo())
				major = true;
			else
				minor = true;
		}

		if (delta.getEnumDelta().isAdded())
			minor = true;

		if (delta.getEnumDelta().isDeleted())
			major = true;

	}

	private void processMethod(MethodDelta delta) {

		if (isEffectivelyIrrelevant(delta))
			return;

		if (isEffectivelyAdded(delta)) {
			minor = true;
			return;
		}

		if (isEffectivelyDeleted(delta)) {
			major = true;
			return;
		}

		for (ShallowDelta<String> exceptionDelta : delta
				.getExceptionDeltas()
				.values()) {

			if (exceptionDelta.isAdded())
				major = true;

		}

		if (!delta.getSignatureDelta().isUnchanged())
			major = true;

		if (delta.getStaticDelta().isChanged())
			major = true;

		if (delta.getFinalDelta().isAdded() && clientExtendable)
			major = true;

		if (delta.getVarargsDelta().isAdded())
			minor = true;

		if (delta.getVarargsDelta().isDeleted())
			major = true;

		if (delta.getAbstractDelta().isAdded())
			major = true;

	}

	private boolean isEffectivelyIrrelevant(EntityDelta<?> delta) {

		if (delta.getVisibilityDelta().getFrom() == Visibility.PUBLIC
				|| delta.getVisibilityDelta().getTo() == Visibility.PUBLIC)
			return false;

		if (clientExtendable)
			if (delta.getVisibilityDelta().getFrom() == Visibility.PROTECTED
					|| delta.getVisibilityDelta().getTo() == Visibility.PROTECTED)
				return false;

		return true;

	}

	private boolean isEffectivelyAdded(EntityDelta<?> delta) {

		if (delta.isUnchanged())
			return false;

		ShallowDelta<Visibility> visibilityDelta = delta.getVisibilityDelta();

		if ((delta.isAdded() || visibilityDelta.getFrom() != Visibility.PUBLIC)
				&& visibilityDelta.getTo() == Visibility.PUBLIC)
			return true;

		if (clientExtendable) {

			if ((delta.isAdded() || visibilityDelta.getFrom() != Visibility.PROTECTED)
					&& visibilityDelta.getTo() == Visibility.PROTECTED)
				return true;

		}

		return false;

	}

	private boolean isEffectivelyDeleted(EntityDelta<?> delta) {

		if (delta.isUnchanged())
			return false;

		ShallowDelta<Visibility> visibilityDelta = delta.getVisibilityDelta();

		if ((delta.isDeleted() || visibilityDelta.getTo() != Visibility.PUBLIC)
				&& visibilityDelta.getFrom() == Visibility.PUBLIC)
			return true;

		if (clientExtendable) {

			if ((delta.isDeleted() || visibilityDelta.getTo() != Visibility.PROTECTED)
					&& visibilityDelta.getFrom() == Visibility.PROTECTED)
				return true;

		}

		return false;

	}

}
