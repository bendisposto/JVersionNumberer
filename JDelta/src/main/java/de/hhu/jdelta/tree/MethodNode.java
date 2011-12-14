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

import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import com.google.common.collect.ImmutableSortedSet;

/**
 * A node that represents a method.
 * 
 * Contained by ClassNodes.
 * 
 * @author Gian Perrone
 */
public class MethodNode extends AbstractNode implements MethodVisitor {

	private String name;
	private String desc;
	private String signature;
	private SortedSet<String> exceptions = new TreeSet<String>();

	private SortedSet<String> annotations = new TreeSet<String>();

	private Visibility visibility;
	private boolean static_ = false;
	private boolean final_ = false;
	private boolean synchronized_ = false;
	private boolean bridge = false;
	private boolean varargs = false;
	private boolean native_ = false;
	private boolean abstract_ = false;
	private boolean strictfp_ = false;

	/**
	 * The empty method.
	 */
	public static final MethodNode EMPTY_METHOD_NODE = new MethodNode(null,
			null, null, ImmutableSortedSet.<String> of(), null, false, false,
			false, false, false, false, false, false);

	MethodNode(int access, String name, String desc, String signature,
			String[] exceptions) {

		super();

		this.name = name;
		this.desc = desc;
		this.signature = signature;

		if (exceptions != null) {
			for (String exception : exceptions) {
				this.exceptions.add(exception.replace('/', '.'));
			}
			this.exceptions = ImmutableSortedSet.copyOf(this.exceptions);
		}

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

		if ((access & Opcodes.ACC_SYNCHRONIZED) != 0)
			synchronized_ = true;

		if ((access & Opcodes.ACC_BRIDGE) != 0)
			bridge = true;

		if ((access & Opcodes.ACC_VARARGS) != 0)
			varargs = true;

		if ((access & Opcodes.ACC_NATIVE) != 0)
			native_ = true;

		if ((access & Opcodes.ACC_ABSTRACT) != 0)
			abstract_ = true;

		if ((access & Opcodes.ACC_STRICT) != 0)
			strictfp_ = true;

	}

	/**
	 * Generates a MethodNode with the given values.
	 * 
	 * @param name
	 *            the name
	 * @param desc
	 *            the description
	 * @param signature
	 *            the signature
	 * @param exceptions
	 *            the thrown checked exceptions
	 * @param visibility
	 *            the visibility
	 * @param static_
	 *            if the method is static
	 * @param final_
	 *            if the method is final
	 * @param synchronized_
	 *            if the method is synchronized
	 * @param bridge
	 *            if the method is a bridge method
	 * @param varargs
	 *            if the method can handle variable argument count
	 * @param native_
	 *            if the method is native
	 * @param abstract_
	 *            if the method is abstract
	 * @param strictfp_
	 *            if the method is strictfp
	 */
	public MethodNode(String name, String desc, String signature,
			ImmutableSortedSet<String> exceptions, Visibility visibility,
			boolean static_, boolean final_, boolean synchronized_,
			boolean bridge, boolean varargs, boolean native_,
			boolean abstract_, boolean strictfp_) {

		super();

		this.name = name;
		this.desc = desc;
		this.signature = signature;
		this.exceptions = exceptions;
		this.visibility = visibility;
		this.static_ = static_;
		this.final_ = final_;
		this.synchronized_ = synchronized_;
		this.bridge = bridge;
		this.varargs = varargs;
		this.native_ = native_;
		this.abstract_ = abstract_;
		this.strictfp_ = strictfp_;

	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder()
				.append(name)
				.append(desc)
				.append(signature)
				.append(exceptions)
				.append(visibility)
				.append(static_)
				.append(final_)
				.append(synchronized_)
				.append(bridge)
				.append(varargs)
				.append(native_)
				.append(abstract_)
				.append(strictfp_)
				.toHashCode();

	}

	@Override
	public boolean equals(Object o) {

		if (o == null)
			return false;

		if (o == this)
			return true;

		if (!(o instanceof MethodNode))
			return false;

		MethodNode other = (MethodNode) o;

		return new EqualsBuilder()
				.append(name, other.name)
				.append(desc, other.desc)
				.append(signature, other.signature)
				.append(exceptions, other.exceptions)
				.append(visibility, other.visibility)
				.append(static_, other.static_)
				.append(final_, other.final_)
				.append(synchronized_, other.synchronized_)
				.append(bridge, other.bridge)
				.append(varargs, other.varargs)
				.append(native_, other.native_)
				.append(abstract_, other.abstract_)
				.append(strictfp_, other.strictfp_)
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
	 * @return the desc
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
	 * Returns the thrown checked exceptions.
	 * 
	 * @return the exceptions
	 */
	public ImmutableSortedSet<String> getExceptions() {
		return ImmutableSortedSet.copyOf(exceptions);
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
	 * Returns true if the method is static.
	 * 
	 * @return the static flag
	 */
	public boolean isStatic() {
		return static_;
	}

	/**
	 * Returns true if the method is final.
	 * 
	 * @return the final flag
	 */
	public boolean isFinal() {
		return final_;
	}

	/**
	 * Returns true if the method is synchronized.
	 * 
	 * @return the synchronized flag
	 */
	public boolean isSynchronized() {
		return synchronized_;
	}

	/**
	 * Returns true if the method is a bridge method.
	 * 
	 * @return the bridge flag
	 */
	public boolean isBridge() {
		return bridge;
	}

	/**
	 * Returns true if the method can handle variable argument count.
	 * 
	 * @return the varargs flags
	 */
	public boolean isVarargs() {
		return varargs;
	}

	/**
	 * Returns true if the method is native.
	 * 
	 * @return the native flag
	 */
	public boolean isNative() {
		return native_;
	}

	/**
	 * Returns true if the method is abstract.
	 * 
	 * @return the abstract flag
	 */
	public boolean isAbstract() {
		return abstract_;
	}

	/**
	 * Returns true if the method is strictfp.
	 * 
	 * @return the strictfp flag
	 */
	public boolean isStrictfp() {
		return strictfp_;
	}

	private String getParametersTypeString() {

		Type[] types = Type.getArgumentTypes(desc);

		String[] typeStrings = new String[types.length];

		for (int i = 0; i < types.length; i++)
			typeStrings[i] = types[i].getClassName();

		return StringUtils.join(typeStrings, ", ");

	}

	private String getExceptionsString() {

		return StringUtils.join(exceptions, ", ");

	}

	private String getReturnTypeString() {

		return Type.getReturnType(desc).getClassName();

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

		if (synchronized_)
			str.append("synchronized ");

		if (native_)
			str.append("native ");

		if (abstract_)
			str.append("abstract ");

		if (strictfp_)
			str.append("strictfp ");

		str.append(getReturnTypeString()).append(" ");
		str.append(name);
		str.append("(").append(getParametersTypeString()).append(")");

		if (exceptions.size() > 0)
			str.append(" throws ").append(getExceptionsString());

		return str.toString();

	}

	public AnnotationVisitor visitAnnotationDefault() {
		return null;
	}

	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {

		annotations.add(desc);

		return null;

	}

	public AnnotationVisitor visitParameterAnnotation(int parameter,
			String desc, boolean visible) {
		return null;
	}

	public void visitAttribute(Attribute attr) {
	}

	public void visitCode() {
	}

	public void visitFrame(int type, int nLocal, Object[] local, int nStack,
			Object[] stack) {
	}

	public void visitInsn(int opcode) {
	}

	public void visitIntInsn(int opcode, int operand) {
	}

	public void visitVarInsn(int opcode, int var) {
	}

	public void visitTypeInsn(int opcode, String type) {
	}

	public void visitFieldInsn(int opcode, String owner, String name,
			String desc) {
	}

	public void visitMethodInsn(int opcode, String owner, String name,
			String desc) {
	}

	public void visitJumpInsn(int opcode, Label label) {
	}

	public void visitLabel(Label label) {
	}

	public void visitLdcInsn(Object cst) {
	}

	public void visitIincInsn(int var, int increment) {
	}

	public void visitTableSwitchInsn(int min, int max, Label dflt,
			Label[] labels) {
	}

	public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
	}

	public void visitMultiANewArrayInsn(String desc, int dims) {
	}

	public void visitTryCatchBlock(Label start, Label end, Label handler,
			String type) {
	}

	public void visitLocalVariable(String name, String desc, String signature,
			Label start, Label end, int index) {
	}

	public void visitLineNumber(int line, Label start) {
	}

	public void visitMaxs(int maxStack, int maxLocals) {
	}

	public void visitEnd() {
	}

}
