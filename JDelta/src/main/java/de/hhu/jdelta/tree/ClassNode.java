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

import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;

/**
 * A node that represents a class.
 * 
 * Contains FieldNodes and MethodNodes and is contained by JarNodes.
 * 
 * @author Gian Perrone
 */
public class ClassNode extends AbstractNode implements ClassVisitor {

	/**
	 * The type of a class.
	 * 
	 * @author Gian Perrone
	 */
	public enum ClassType {

		/**
		 * A regular class, toString() returns "class".
		 */
		CLASS("class"),
		/**
		 * An abstract class, toString() returns "abstract class".
		 */
		ABSTRACT_CLASS("abstract class"),
		/**
		 * An interface, toString() returns "interface".
		 */
		INTERFACE("interface"),
		/**
		 * An annotation, toString() returns "@interface".
		 */
		ANNOTATION("@interface"),
		/**
		 * An enumeration, toString() returns "enum".
		 */
		ENUM("enum");

		private String str;

		private ClassType(String str) {

			this.str = str;

		}

		/**
		 * Returns a string description of the class type. It is formatted like
		 * a declaration which the exception of the name which is
		 * fully-qualified.
		 */
		@Override
		public String toString() {

			return str;

		}

	}

	private SortedMap<String, FieldNode> fields =
			new TreeMap<String, FieldNode>();
	private SortedMap<String, MethodNode> methods =
			new TreeMap<String, MethodNode>();
	// private SortedMap<String, ClassNode> innerClasses =
	// new TreeMap<String, ClassNode>();

	private SortedSet<String> interfaces = new TreeSet<String>();

	private SortedSet<String> annotations = new TreeSet<String>();

	private int version;
	private String name;
	private String signature;
	private String superName;

	private Visibility visibility;
	private ClassType classType;
	private boolean final_ = false;
	private boolean super_ = false;

	/**
	 * The empty class.
	 */
	public static final ClassNode EMPTY_CLASS_NODE = new ClassNode(
			ImmutableSortedMap.<String, FieldNode> of(),
			ImmutableSortedMap.<String, MethodNode> of(),
			ImmutableSortedSet.<String> of(), ImmutableSortedSet.<String> of(),
			-1, null, null, null, null, null, false, false);

	/**
	 * Generates a ClassNode and children reading from a ClassReader.
	 * 
	 * @param cr
	 *            the ClassReader
	 */
	public ClassNode(ClassReader cr) {

		super();

		cr.accept(this, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG
				| ClassReader.SKIP_FRAMES);

	}

	/**
	 * Generates a ClassNode with the given values.
	 * 
	 * @param fields
	 *            the fields
	 * @param methods
	 *            the methods
	 * @param interfaces
	 *            the interfaces
	 * @param annotations
	 *            the annotations
	 * @param version
	 *            the version
	 * @param name
	 *            the name
	 * @param signature
	 *            the signature
	 * @param superName
	 *            the name of the super class
	 * @param visibility
	 *            the visibility
	 * @param classType
	 *            the type of the class
	 * @param final_
	 *            if the class is final
	 * @param super_
	 *            if the class has the super flag set
	 */
	public ClassNode(ImmutableSortedMap<String, FieldNode> fields,
			ImmutableSortedMap<String, MethodNode> methods,
			ImmutableSortedSet<String> interfaces,
			ImmutableSortedSet<String> annotations, int version, String name,
			String signature, String superName, Visibility visibility,
			ClassType classType, boolean final_, boolean super_) {

		super();

		this.fields = fields;
		this.methods = methods;
		this.interfaces = interfaces;
		this.annotations = annotations;
		this.version = version;
		this.name = name;
		this.signature = signature;
		this.superName = superName;
		this.visibility = visibility;
		this.classType = classType;
		this.final_ = final_;
		this.super_ = super_;

	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder()
				.append(name)
				.append(fields)
				.append(methods)
				// .append(innerClasses)
				.append(interfaces)
				.append(version)
				.append(signature)
				.append(superName)
				.append(visibility)
				.append(classType)
				.append(final_)
				.append(super_)
				.toHashCode();

	}

	@Override
	public boolean equals(Object o) {

		if (o == null)
			return false;

		if (o == this)
			return true;

		if (!(o instanceof ClassNode))
			return false;

		ClassNode other = (ClassNode) o;

		return new EqualsBuilder()
				.append(name, other.name)
				.append(fields, other.fields)
				.append(methods, other.methods)
				// .append(innerClasses, other.innerClasses)
				.append(interfaces, other.interfaces)
				.append(version, other.version)
				.append(signature, other.signature)
				.append(superName, other.superName)
				.append(visibility, other.visibility)
				.append(classType, other.classType)
				.append(final_, other.final_)
				.append(super_, other.super_)
				.isEquals();

	}

	/**
	 * Returns a immutable sorted map of FieldNodes for the fields declared in
	 * this class. The names are used as keys.
	 * 
	 * @return a sorted map of the FieldNodes
	 */
	public ImmutableSortedMap<String, FieldNode> getFields() {
		return ImmutableSortedMap.copyOf(fields);
	}

	/**
	 * Returns a immutable sorted map of MethodNodes for the methods declared in
	 * this class. The names and the description joined by "," are used as keys.
	 * 
	 * @return a sorted map of the MethodNodes
	 */
	public ImmutableSortedMap<String, MethodNode> getMethods() {
		return ImmutableSortedMap.copyOf(methods);
	}

	// /**
	// * @return the innerClasses
	// */
	// public SortedMap<String, ClassNode> getInnerClasses() {
	// return innerClasses;
	// }

	/**
	 * Returns the implemented interfaces.
	 * 
	 * @return the interfaces
	 */
	public ImmutableSortedSet<String> getInterfaces() {
		return ImmutableSortedSet.copyOf(interfaces);
	}

	/**
	 * Returns the annotations.
	 * 
	 * @return the annotations
	 */
	public ImmutableSortedSet<String> getAnnotations() {
		return ImmutableSortedSet.copyOf(annotations);
	}

	/**
	 * Returns the class file version.
	 * 
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Returns the name.
	 * 
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Returns the signature.
	 * 
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * Returns the name of the super class.
	 * 
	 * @return the superName
	 */
	public String getSuperName() {
		return superName;
	}

	/**
	 * Returns the visibility.
	 * 
	 * @return the visibility
	 */
	public Visibility getVisibility() {
		return visibility;
	}

	/**
	 * Returns the class type.
	 * 
	 * @return the class type
	 */
	public ClassType getClassType() {
		return classType;
	}

	/**
	 * Returns true if the class is final.
	 * 
	 * @return the final flag
	 */
	public boolean isFinal() {
		return final_;
	}

	/**
	 * Returns true if the super flag is set.
	 * 
	 * This flag changes the way the JVM handles invokestatic instructions and
	 * is set by modern compilers.
	 * 
	 * @return the super flag
	 */
	public boolean isSuper() {
		return super_;
	}

	/**
	 * Returns a multi-line description of the class including its fields and
	 * methods.
	 * 
	 * @return the description
	 */
	public String getDeepDeclarationsString() {

		StringBuilder str = new StringBuilder();

		str.append(getDeclarationString()).append("\n");

		for (FieldNode field : fields.values())
			str.append(" ").append(field.getDeclarationString()).append("\n");

		for (MethodNode method : methods.values())
			str.append(" ").append(method.getDeclarationString()).append("\n");

		return str.toString();

	}

	/**
	 * Returns a shallow description of the class.
	 * 
	 * @return the description
	 */
	@Override
	public String getDeclarationString() {

		// Scheme:
		// accessFlags* className

		StringBuilder str = new StringBuilder();

		// accessFlags*

		if (visibility != Visibility.PACKAGE)
			str.append(visibility.toString()).append(" ");

		if (final_)
			str.append("final ");

		str.append(classType.toString()).append(" ");

		// className

		str.append(name.replace('/', '.'));

		if (!interfaces.isEmpty()) {

			// TODO: Differentiate between classtypes
			str.append(" implements ").append(
					StringUtils.join(interfaces, ", "));

		}

		return str.toString();

	}

	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {

		this.version = version;
		this.name = name;
		this.signature = signature;
		this.superName = superName;

		for (String interface_ : interfaces)
			this.interfaces.add(interface_);
		this.interfaces = ImmutableSortedSet.copyOf(this.interfaces);

		if ((access & Opcodes.ACC_PUBLIC) != 0)
			visibility = Visibility.PUBLIC;
		else
			visibility = Visibility.PACKAGE;

		if ((access & Opcodes.ACC_FINAL) != 0)
			final_ = true;

		if ((access & Opcodes.ACC_SUPER) != 0)
			super_ = true;

		if ((access & Opcodes.ACC_ANNOTATION) != 0)
			classType = ClassType.ANNOTATION;
		else if ((access & Opcodes.ACC_INTERFACE) != 0)
			classType = ClassType.INTERFACE;
		else if ((access & Opcodes.ACC_ENUM) != 0)
			classType = ClassType.ENUM;
		else if ((access & Opcodes.ACC_ABSTRACT) != 0)
			classType = ClassType.ABSTRACT_CLASS;
		else
			classType = ClassType.CLASS;

	}

	public void visitSource(String source, String debug) {
	}

	public void visitOuterClass(String owner, String name, String desc) {
	}

	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {

		annotations.add(desc);

		return null;

	}

	public void visitAttribute(Attribute attr) {
	}

	public void visitInnerClass(String name, String outerName,
			String innerName, int access) {
	}

	public FieldVisitor visitField(int access, String name, String desc,
			String signature, Object value) {

		FieldNode field = new FieldNode(access, name, desc, signature, value);
		fields.put(field.getName(), field);

		return field;

	}

	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {

		MethodNode method =
				new MethodNode(access, name, desc, signature, exceptions);
		methods.put(method.getName() + "," + method.getDesc(), method);

		return method;

	}

	public void visitEnd() {

		fields = ImmutableSortedMap.copyOf(fields);
		methods = ImmutableSortedMap.copyOf(methods);
		// innerClasses = Collections.unmodifiableSortedMap(innerClasses);

	}

}
