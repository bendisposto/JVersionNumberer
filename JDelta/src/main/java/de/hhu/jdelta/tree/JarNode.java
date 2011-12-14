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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.objectweb.asm.ClassReader;

import com.google.common.collect.ImmutableSortedMap;

/**
 * A node that represents a jar file.
 * 
 * Contains ClassNodes.
 * 
 * @author Gian Perrone
 */
public class JarNode {

	private JarFile jar;

	private SortedMap<String, ClassNode> classes =
			new TreeMap<String, ClassNode>();

	/**
	 * Creates a JarNode and children from the given jar file.
	 * 
	 * @param jar
	 *            the jar file
	 * @throws IOException
	 */
	public JarNode(File jar) throws IOException {

		this.jar = new JarFile(jar);

		for (Enumeration<JarEntry> e = this.jar.entries(); e.hasMoreElements();) {

			JarEntry entry = e.nextElement();

			String name = entry.getName();
			if (name.endsWith(".class") && !name.contains("$")) {

				InputStream is = this.jar.getInputStream(entry);

				classes.put(name, new ClassNode(new ClassReader(is)));

			}

		}

		classes = ImmutableSortedMap.copyOf(classes);

	}

	/**
	 * Returns a multi-line description of the jar class including its classes
	 * and recursively fields and methods.
	 * 
	 * @return the description
	 */
	public String getDeepDeclarationsString() {

		StringBuilder str = new StringBuilder();

		for (ClassNode classNode : classes.values()) {

			str.append(classNode.getDeepDeclarationsString()).append("\n");

		}

		return str.toString();

	}

	/**
	 * Returns the classes.
	 * 
	 * @return the classes
	 */
	public ImmutableSortedMap<String, ClassNode> getClasses() {
		return ImmutableSortedMap.copyOf(classes);
	}

}
