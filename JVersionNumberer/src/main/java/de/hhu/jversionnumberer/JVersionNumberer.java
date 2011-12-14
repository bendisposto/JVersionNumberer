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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.ClassReader;

import de.hhu.jdelta.delta.ClassDelta;
import de.hhu.jdelta.delta.JarDelta;
import de.hhu.jdelta.tree.ClassNode;
import de.hhu.jdelta.tree.JarNode;

/**
 * 
 * Implements a CLI to display the contents of class or jar files or the
 * differences between two classes or jar files
 * 
 * @author Gian Perrone
 * 
 */
public class JVersionNumberer {

	public static void main(String[] args) throws Exception {

		if (args.length < 1)
			printUsageAndExit();

		if (args[0].equals("versionnumber")) {

			if (args.length != 4)
				printUsageAndExit();

			doVersionNumber(args[1], args[2], args[3]);

		}

		if (args[0].equals("show")) {

			if (args.length != 2)
				printUsageAndExit();

			doShow(args[1]);

		} else if (args[0].equals("diff")) {

			if (args.length != 3)
				printUsageAndExit();

			doDiff(args[1], args[2]);

		} else if (args[0].equals("showJar")) {

			if (args.length != 2)
				printUsageAndExit();

			doShowJar(args[1]);

		} else if (args[0].equals("diffJar")) {

			if (args.length != 3)
				printUsageAndExit();

			doDiffJar(args[1], args[2]);

		}

	}

	private static void printUsageAndExit() {

		System.out.println("Usage:");
		System.out.println(" show {class}");
		System.out.println(" showJar {jar}");
		System.out.println(" diff {fromClass} {toClass}");
		System.out.println(" diffJar {fromJar} {toJar}");
		System.out
				.println(" versionnumber {fromJar} {toJar} {oldVersionNumber}");

		System.exit(0);

	}

	private static void doShow(String f) {

		InputStream in;

		try {
			in = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		ClassNode cn;

		try {
			cn = new ClassNode(new ClassReader(in));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		System.out.println(cn.getDeepDeclarationsString());
		System.out.println(cn.toString());

	}

	private static void doShowJar(String f) {

		JarNode jn;

		try {
			jn = new JarNode(new File(f));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		System.out.println(jn.getDeepDeclarationsString());

	}

	private static void doDiff(String from, String to) {

		InputStream in1, in2;

		try {
			in1 = new FileInputStream(from);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		try {
			in2 = new FileInputStream(to);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		ClassNode cn1, cn2;

		try {
			cn1 = new ClassNode(new ClassReader(in1));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		try {
			cn2 = new ClassNode(new ClassReader(in2));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		ClassDelta cd = new ClassDelta(cn1, cn2);

		System.out.println(cd.getDeepDeclarationsDiff());

	}

	private static void doDiffJar(String from, String to) {

		JarNode jn1, jn2;

		try {
			jn1 = new JarNode(new File(from));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		try {
			jn2 = new JarNode(new File(to));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		JarDelta jd = new JarDelta(jn1, jn2);

		System.out.println(jd.getDeepDeclarationsDiff());

	}

	private static void doVersionNumber(String from, String to,
			String oldVersion) {

		JarNode jn1, jn2;

		try {
			jn1 = new JarNode(new File(from));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		try {
			jn2 = new JarNode(new File(to));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		JarDelta jd = new JarDelta(jn1, jn2);

		VersionNumberWalker vnw = new VersionNumberWalker(jd);

		String[] strParts = oldVersion.split("\\.");
		int[] parts = new int[strParts.length];

		for (int i = 0; i < strParts.length; i++)
			parts[i] = Integer.parseInt(strParts[i]);

		if (vnw.isMajor()) {

			parts[0]++;

			for (int i = 1; i < parts.length; i++)
				parts[i] = 0;

		} else if (vnw.isMinor()) {

			parts[1]++;

			for (int i = 2; i < parts.length; i++)
				parts[i] = 0;

		}

		for (int i = 0; i < parts.length; i++)
			strParts[i] = Integer.toString(parts[i]);

		String newVersion = StringUtils.join(strParts, ".");
		System.out.println(newVersion);

	}

}
