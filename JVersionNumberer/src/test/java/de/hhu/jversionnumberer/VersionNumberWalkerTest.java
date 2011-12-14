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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;

import de.hhu.jdelta.delta.ClassDelta;
import de.hhu.jdelta.tree.ClassNode;
import de.hhu.jdelta.tree.FieldNode;
import de.hhu.jdelta.tree.MethodNode;
import de.hhu.jdelta.tree.Visibility;
import de.hhu.jdelta.tree.ClassNode.ClassType;

public class VersionNumberWalkerTest {

	private final static ImmutableSortedMap<String, MethodNode> NOMETHODS =
			ImmutableSortedMap.<String, MethodNode> of();
	private final static ImmutableSortedMap<String, FieldNode> NOFIELDS =
			ImmutableSortedMap.<String, FieldNode> of();

	private final static ImmutableSortedSet<String> NOSTRINGS =
			ImmutableSortedSet.<String> of();

	private final static ImmutableSortedSet<String> REIMPLEMENTABLE =
			ImmutableSortedSet
					.<String> of("Lde/hhu/jversionnumberer/annotations/ExtentableByClient;");
	private final static ImmutableSortedSet<String> THROWSEXCEPTION =
			ImmutableSortedSet.<String> of("Ljava/lang/Exception;");

	private final static String CNAME = "clazz";
	private final static int CVERSION = 49;
	private final static String CSUPERNAME = "java/lang/Object";

	private final static String FNAME = "field";
	private final static String FDESC1 = "I";
	private final static String FDESC2 = "J";
	private final static Object FVALUE1 = new Integer(1);
	private final static Object FVALUE2 = new Integer(2);

	private final static String MNAME = "method";
	private final static String MDESC1 = "(Ljava/lang/Object;)V";
	private final static String MSIGNATURE1 = "<T:Ljava/lang/Object;>(TT;)V";
	private final static String MSIGNATURE11 = "<S:Ljava/lang/Object;>(TS;)V";

	private static ClassNode generateClassNode(MethodNode method,
			boolean reimplementable) {

		ImmutableSortedMap<String, MethodNode> methods =
				method != null ? ImmutableSortedMap
						.of(method.getName(), method) : ImmutableSortedMap
						.<String, MethodNode> of();

		if (!reimplementable)
			return new ClassNode(NOFIELDS, methods, NOSTRINGS, NOSTRINGS,
					CVERSION, CNAME, null, CSUPERNAME, Visibility.PUBLIC,
					ClassType.CLASS, false, true);
		else
			return new ClassNode(NOFIELDS, methods, NOSTRINGS, REIMPLEMENTABLE,
					CVERSION, CNAME, null, CSUPERNAME, Visibility.PUBLIC,
					ClassType.CLASS, false, true);

	}

	private static ClassNode generateClassNode(FieldNode field,
			boolean reimplementable) {

		ImmutableSortedMap<String, FieldNode> fields =
				field != null ? ImmutableSortedMap.of(field.getName(), field)
						: ImmutableSortedMap.<String, FieldNode> of();

		if (!reimplementable)
			return new ClassNode(fields, NOMETHODS, NOSTRINGS, NOSTRINGS,
					CVERSION, CNAME, null, CSUPERNAME, Visibility.PUBLIC,
					ClassType.CLASS, false, true);
		else
			return new ClassNode(fields, NOMETHODS, NOSTRINGS, REIMPLEMENTABLE,
					CVERSION, CNAME, null, CSUPERNAME, Visibility.PUBLIC,
					ClassType.CLASS, false, true);

	}

	private static ClassDelta generateClassDelta(FieldNode f1, FieldNode f2,
			boolean reimplementable) {

		return new ClassDelta(generateClassNode(f1, reimplementable),
				generateClassNode(f2, reimplementable));

	}

	private static ClassDelta generateClassDelta(MethodNode m1, MethodNode m2,
			boolean reimplementable) {

		return new ClassDelta(generateClassNode(m1, reimplementable),
				generateClassNode(m2, reimplementable));

	}

	private static void assertMinor(ClassDelta cd) {

		VersionNumberWalker vnw = new VersionNumberWalker(cd);

		assertFalse(vnw.isMajor());
		assertTrue(vnw.isMinor());

	}

	private static void assertMajor(ClassDelta cd) {

		VersionNumberWalker vnw = new VersionNumberWalker(cd);

		assertTrue(vnw.isMajor());

	}

	private static void assertNeither(ClassDelta cd) {

		VersionNumberWalker vnw = new VersionNumberWalker(cd);

		assertFalse(vnw.isMajor());
		assertFalse(vnw.isMinor());
	}

	@Test
	public void fieldAddedTest() {

		FieldNode f2 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						false, false, false, false, false, false);

		assertMinor(generateClassDelta(null, f2, false));

	}

	@Test
	public void fieldDeletedTest() {

		FieldNode f1 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						false, false, false, false, false, false);

		assertMajor(generateClassDelta(f1, null, false));

	}

	@Test
	public void fieldDescChangedTest() {

		FieldNode f1 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						false, false, false, false, false, false);
		FieldNode f2 =
				new FieldNode(FNAME, FDESC2, null, null, Visibility.PUBLIC,
						false, false, false, false, false, false);

		assertMajor(generateClassDelta(f1, f2, false));

	}
	
	@Test
	public void fieldValueAddedTest() {

		FieldNode f1 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						false, false, false, false, false, false);
		FieldNode f2 =
				new FieldNode(FNAME, FDESC1, null, FVALUE2, Visibility.PUBLIC,
						false, false, false, false, false, false);

		assertMajor(generateClassDelta(f1, f2, false));

	}
	
	@Test
	public void fieldValueDeletedTest() {

		FieldNode f1 =
				new FieldNode(FNAME, FDESC1, null, FVALUE1, Visibility.PUBLIC,
						false, false, false, false, false, false);
		FieldNode f2 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						false, false, false, false, false, false);

		assertMajor(generateClassDelta(f1, f2, false));

	}
	
	@Test
	public void fieldValueChangedTest() {

		FieldNode f1 =
				new FieldNode(FNAME, FDESC1, null, FVALUE1, Visibility.PUBLIC,
						false, false, false, false, false, false);
		FieldNode f2 =
				new FieldNode(FNAME, FDESC1, null, FVALUE2, Visibility.PUBLIC,
						false, false, false, false, false, false);

		assertMajor(generateClassDelta(f1, f2, false));

	}

	@Test
	public void fieldStaticAddedTest() {

		FieldNode f1 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						false, false, false, false, false, false);
		FieldNode f2 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						true, false, false, false, false, false);

		assertMajor(generateClassDelta(f1, f2, false));

	}

	@Test
	public void fieldStaticDeletedTest() {

		FieldNode f1 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						true, false, false, false, false, false);
		FieldNode f2 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						false, false, false, false, false, false);

		assertMajor(generateClassDelta(f1, f2, false));

	}

	@Test
	public void fieldFinalAddedTest() {

		FieldNode f1 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						false, false, false, false, false, false);
		FieldNode f2 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						false, true, false, false, false, false);

		assertMajor(generateClassDelta(f1, f2, true));

	}

	@Test
	public void fieldFinalDeletedNonStaticTest() {

		FieldNode f1 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						false, true, false, false, false, false);
		FieldNode f2 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						false, false, false, false, false, false);

		assertMinor(generateClassDelta(f1, f2, false));

	}

	@Test
	public void fieldFinalDeletedStaticTest() {

		FieldNode f1 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						true, true, false, false, false, false);
		FieldNode f2 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						true, false, false, false, false, false);

		assertMajor(generateClassDelta(f1, f2, false));

	}

	@Test
	public void methodAddedTest() {

		MethodNode m2 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, false);

		assertMinor(generateClassDelta(null, m2, false));

	}

	@Test
	public void fieldVolatileAddedTest() {

		FieldNode f1 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						false, false, false, false, false, false);
		FieldNode f2 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						false, false, true, false, false, false);

		assertNeither(generateClassDelta(f1, f2, true));

	}

	@Test
	public void fieldVolatileDeletedTest() {

		FieldNode f1 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						false, false, true, false, false, false);
		FieldNode f2 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						false, false, false, false, false, false);

		assertNeither(generateClassDelta(f1, f2, false));

	}

	@Test
	public void fieldTransientAddedTest() {

		FieldNode f1 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						false, false, false, false, false, false);
		FieldNode f2 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						false, false, false, true, false, false);

		assertNeither(generateClassDelta(f1, f2, true));

	}

	@Test
	public void fieldTransientDeletedTest() {

		FieldNode f1 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						false, false, false, true, false, false);
		FieldNode f2 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						false, false, false, false, false, false);

		assertNeither(generateClassDelta(f1, f2, false));

	}

	@Test
	public void fieldSyntheticAddedTest() {

		FieldNode f1 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						false, false, false, false, false, false);
		FieldNode f2 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						false, false, false, false, true, false);

		assertNeither(generateClassDelta(f1, f2, true));

	}

	@Test
	public void fieldSyntheticDeletedTest() {

		FieldNode f1 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						false, false, false, false, true, false);
		FieldNode f2 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						false, false, false, false, false, false);

		assertNeither(generateClassDelta(f1, f2, false));

	}

	@Test
	public void fieldEnumAddedTest() {

		FieldNode f1 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						false, false, false, false, false, false);
		FieldNode f2 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						false, false, false, false, false, true);

		assertMinor(generateClassDelta(f1, f2, true));

	}

	@Test
	public void fieldEnumDeletedTest() {

		FieldNode f1 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						false, false, false, false, false, true);
		FieldNode f2 =
				new FieldNode(FNAME, FDESC1, null, null, Visibility.PUBLIC,
						false, false, false, false, false, false);

		assertMajor(generateClassDelta(f1, f2, false));

	}

	@Test
	public void methodDeletedTest() {

		MethodNode m1 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, false);

		assertMajor(generateClassDelta(m1, null, false));

	}

	@Test
	public void methodStaticAddedTest() {

		MethodNode m1 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, false);

		MethodNode m2 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, true, false, false, false, false,
						false, false, false);

		assertMajor(generateClassDelta(m1, m2, false));

	}

	@Test
	public void methodStaticDeletedTest() {

		MethodNode m1 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, true, false, false, false, false,
						false, false, false);

		MethodNode m2 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, false);

		assertMajor(generateClassDelta(m1, m2, false));

	}

	@Test
	public void methodFinalAddedNonReimplementableTest() {

		MethodNode m1 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, false);

		MethodNode m2 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, true, false, false, false,
						false, false, false);

		assertNeither(generateClassDelta(m1, m2, false));

	}

	@Test
	public void methodFinalAddedReimplementableTest() {

		MethodNode m1 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, false);

		MethodNode m2 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, true, false, false, false,
						false, false, false);

		assertMajor(generateClassDelta(m1, m2, true));

	}

	@Test
	public void methodFinalDeletedTest() {

		MethodNode m1 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, true, false, false, false,
						false, false, false);

		MethodNode m2 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, false);

		assertNeither(generateClassDelta(m1, m2, false));

	}

	@Test
	public void methodSynchronizedAddedTest() {

		MethodNode m1 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, false);

		MethodNode m2 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, true, false, false,
						false, false, false);

		assertNeither(generateClassDelta(m1, m2, false));

	}

	@Test
	public void methodSynchronizedDeletedTest() {

		MethodNode m1 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, true, false, false,
						false, false, false);

		MethodNode m2 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, false);

		assertNeither(generateClassDelta(m1, m2, false));

	}

	@Test
	public void methodBridgeAddedTest() {

		MethodNode m1 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, false);

		MethodNode m2 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, true, false,
						false, false, false);
		assertNeither(generateClassDelta(m1, m2, false));

	}

	@Test
	public void methodBridgeDeletedTest() {

		MethodNode m1 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, true, false,
						false, false, false);

		MethodNode m2 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, false);

		assertNeither(generateClassDelta(m1, m2, false));

	}

	@Test
	public void methodVarargsAddedTest() {

		MethodNode m1 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, false);

		MethodNode m2 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, true,
						false, false, false);

		assertMinor(generateClassDelta(m1, m2, false));

	}

	@Test
	public void methodVarargsDeletedTest() {

		MethodNode m1 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, true,
						false, false, false);

		MethodNode m2 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, false);

		assertMajor(generateClassDelta(m1, m2, false));

	}

	@Test
	public void methodNativeAddedTest() {

		MethodNode m1 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, false);

		MethodNode m2 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						true, false, false);

		assertNeither(generateClassDelta(m1, m2, false));

	}

	@Test
	public void methodNativeDeletedTest() {

		MethodNode m1 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						true, false, false);

		MethodNode m2 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, false);

		assertNeither(generateClassDelta(m1, m2, false));

	}

	@Test
	public void methodAbstractAddedTest() {

		MethodNode m1 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, false);

		MethodNode m2 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, true, false);

		assertMajor(generateClassDelta(m1, m2, false));

	}

	@Test
	public void methodAbstractDeletedTest() {

		MethodNode m1 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, true, false);

		MethodNode m2 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, false);

		assertNeither(generateClassDelta(m1, m2, false));

	}

	@Test
	public void methodStrictfpAddedTest() {

		MethodNode m1 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, false);

		MethodNode m2 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, true);

		assertNeither(generateClassDelta(m1, m2, false));

	}

	@Test
	public void methodStrictfpDeletedTest() {

		MethodNode m1 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, true);

		MethodNode m2 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, false);

		assertNeither(generateClassDelta(m1, m2, false));

	}

	@Test
	public void methodSignatureAddedTest() {

		MethodNode m1 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, false);

		MethodNode m2 =
				new MethodNode(MNAME, MDESC1, MSIGNATURE1, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, true);

		assertMajor(generateClassDelta(m1, m2, false));

	}

	@Test
	public void methodSignatureDeletedTest() {

		MethodNode m1 =
				new MethodNode(MNAME, MDESC1, MSIGNATURE1, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, true);

		MethodNode m2 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, false);

		assertMajor(generateClassDelta(m1, m2, false));

	}

	@Test
	public void methodSignatureChangedTest() {

		MethodNode m1 =
				new MethodNode(MNAME, MDESC1, MSIGNATURE1, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, true);

		MethodNode m2 =
				new MethodNode(MNAME, MDESC1, MSIGNATURE11, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, false);

		assertMajor(generateClassDelta(m1, m2, false));

	}
	
	@Test
	public void methodExceptionAddedTest() {

		MethodNode m1 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, false);

		MethodNode m2 =
				new MethodNode(MNAME, MDESC1, null, THROWSEXCEPTION,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, true);

		assertMajor(generateClassDelta(m1, m2, false));

	}

	@Test
	public void methodExceptionDeletedTest() {

		MethodNode m1 =
				new MethodNode(MNAME, MDESC1, null, THROWSEXCEPTION,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, true);

		MethodNode m2 =
				new MethodNode(MNAME, MDESC1, null, NOSTRINGS,
						Visibility.PUBLIC, false, false, false, false, false,
						false, false, false);

		assertNeither(generateClassDelta(m1, m2, false));

	}

}
