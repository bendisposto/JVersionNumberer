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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * A node that represents a field.
 * 
 * Contained by ClassNodes.
 * 
 * @author Gian Perrone
 */
public class FieldNode extends AbstractNode implements FieldVisitor {

	private String name;
	private String desc;
	private String signature;
	private Object value;

	private Visibility visibility;
	private boolean static_ = false;
	private boolean final_ = false;
	private boolean volatile_ = false;
	private boolean transient_ = false;
	private boolean synthetic = false;
	private boolean enum_ = false;

	/**
	 * The empty field.
	 */
	public static final FieldNode EMPTY_FIELD_NODE = new FieldNode(null, null,
			null, null, null, false, false, false, false, false, false);

	FieldNode(int access, String name, String desc, String signature,
			Object value) {

		super();

		this.name = name;
		this.desc = desc;
		this.signature = signature;
		this.value = value;

		/*
		 * private, protected, public are mutually exclusive
		 * 
		 * static, final, volatile, transient, synthetic, enum are independent
		 */

		if ((access & Opcodes.ACC_PRIVATE) != 0)
			visibility = Visibility.PRIVATE;
		else if ((access & Opcodes.ACC_PROTECTED) != 0)
			visibility = Visibility.PROTECTED;
		else if ((access & Opcodes.ACC_PUBLIC) != 0)
			visibility = Visibility.PUBLIC;
		else
			visibility = Visibility.PACKAGE;

		if ((access & Opcodes.ACC_STATIC) != 0)
			static_ = true;

		if ((access & Opcodes.ACC_FINAL) != 0)
			final_ = true;

		if ((access & Opcodes.ACC_VOLATILE) != 0)
			volatile_ = true;

		if ((access & Opcodes.ACC_TRANSIENT) != 0)
			transient_ = true;

		if ((access & Opcodes.ACC_SYNTHETIC) != 0)
			synthetic = true;

		if ((access & Opcodes.ACC_ENUM) != 0)
			enum_ = true;

	}

	/**
	 * Creates a FieldNode with the given values.
	 * 
	 * @param name
	 *            the name
	 * @param desc
	 *            the description
	 * @param signature
	 *            the signature
	 * @param value
	 *            the initial value
	 * @param visibility
	 *            the visibility
	 * @param static_
	 *            if the field is static
	 * @param final_
	 *            if the field is final
	 * @param volatile_
	 *            if the field is volatile
	 * @param transient_
	 *            if the field is transient
	 * @param synthetic
	 *            if the field is synthetic
	 * @param enum_
	 *            if the field is an enum constant
	 */
	public FieldNode(String name, String desc, String signature, Object value,
			Visibility visibility, boolean static_, boolean final_,
			boolean volatile_, boolean transient_, boolean synthetic,
			boolean enum_) {

		super();

		this.name = name;
		this.desc = desc;
		this.signature = signature;
		this.value = value;
		this.visibility = visibility;
		this.static_ = static_;
		this.final_ = final_;
		this.volatile_ = volatile_;
		this.transient_ = transient_;
		this.synthetic = synthetic;
		this.enum_ = enum_;
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder()
				.append(name)
				.append(desc)
				.append(signature)
				.append(value)
				.append(visibility)
				.append(static_)
				.append(final_)
				.append(volatile_)
				.append(transient_)
				.append(synthetic)
				.append(enum_)
				.toHashCode();

	}

	@Override
	public boolean equals(Object o) {

		if (o == null)
			return false;

		if (o == this)
			return true;

		if (!(o instanceof FieldNode))
			return false;

		FieldNode other = (FieldNode) o;

		return new EqualsBuilder()
				.append(name, other.name)
				.append(desc, other.desc)
				.append(signature, other.signature)
				.append(value, other.value)
				.append(visibility, other.visibility)
				.append(static_, other.static_)
				.append(final_, other.final_)
				.append(volatile_, other.volatile_)
				.append(transient_, other.transient_)
				.append(synthetic, other.synthetic)
				.append(enum_, other.enum_)
				.isEquals();

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
	 * Returns the description.
	 * 
	 * @return the description
	 */
	public String getDesc() {
		return desc;
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
	 * Returns the initial value. Can only be of the types Integer, Long, Float,
	 * Double, String or Boolean.
	 * 
	 * @return the initial value
	 */
	public Object getValue() {
		return value;
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
	 * Returns true if the field is static.
	 * 
	 * @return the static flag
	 */
	public boolean isStatic() {
		return static_;
	}

	/**
	 * Returns true if the field is final.
	 * 
	 * @return the final flag
	 */
	public boolean isFinal() {
		return final_;
	}

	/**
	 * Returns true if the field is volatile.
	 * 
	 * @return the volatile flag
	 */
	public boolean isVolatile() {
		return volatile_;
	}

	/**
	 * Returns true if the field is transient.
	 * 
	 * @return the transient flag
	 */
	public boolean isTransient() {
		return transient_;
	}

	/**
	 * Returns true if the field is synthetic.
	 * 
	 * @return the synthetic flag
	 */
	public boolean isSynthetic() {
		return synthetic;
	}

	/**
	 * Returns true if the field is an enumeration constant.
	 * 
	 * @return the enum flag
	 */

	public boolean isEnum() {
		return enum_;
	}

	private String getTypeString() {

		return Type.getType(desc).getClassName();

	}

	private String getValueString() {

		// TODO: Implement better formatting

		return value.toString();

	}

	@Override
	public String getDeclarationString() {

		StringBuilder str = new StringBuilder();

		if (visibility != Visibility.PACKAGE)
			str.append(visibility.toString()).append(" ");

		if (static_)
			str.append("static ");

		if (final_)
			str.append("final ");

		if (volatile_)
			str.append("volatile ");

		if (transient_)
			str.append("transient ");

		str.append(getTypeString()).append(" ");

		str.append(name);

		if (value != null)
			str.append(" = ").append(getValueString());

		return str.toString();

	}

	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		return null;
	}

	public void visitAttribute(Attribute attr) {
	}

	public void visitEnd() {
	}

}
