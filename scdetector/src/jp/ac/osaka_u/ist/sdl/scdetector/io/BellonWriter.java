package jp.ac.osaka_u.ist.sdl.scdetector.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sdl.scdetector.data.CloneSetInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;

public class BellonWriter {

	public BellonWriter(final String outputFile,
			final SortedSet<FileInfo> files, final Set<CloneSetInfo> cloneSets) {

		this.files = files;
		this.cloneSets = cloneSets;

		try {
			this.writer = new BufferedWriter(new FileWriter(outputFile));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public void write() {

		try {

			final SortedSet<String> lines = new TreeSet<String>();

			for (final CloneSetInfo cloneSet : this.cloneSets) {
				final CodeCloneInfo[] codeclones = cloneSet.getCodeClones()
						.toArray(new CodeCloneInfo[0]);
				for (int i = 0; i < codeclones.length; i++) {
					final ExecutableElementInfo firstI = codeclones[i]
							.getElements().first();
					final ExecutableElementInfo lastI = codeclones[i]
							.getElements().last();
					final String filenameI = ((TargetClassInfo) firstI
							.getOwnerMethod().getOwnerClass()).getOwnerFile()
							.getName();
					for (int j = i + 1; j < codeclones.length; j++) {

						{ // 共通要素を持っている場合はクローンペアにしない
							final Set<ExecutableElementInfo> commonElements = new HashSet<ExecutableElementInfo>();
							commonElements.addAll(codeclones[i].getElements());
							commonElements.retainAll(codeclones[j]
									.getElements());
							if (!commonElements.isEmpty()) {
								continue;
							}
						}

						final ExecutableElementInfo firstJ = codeclones[j]
								.getElements().first();
						final ExecutableElementInfo lastJ = codeclones[j]
								.getElements().last();
						final String filenameJ = ((TargetClassInfo) firstJ
								.getOwnerMethod().getOwnerClass())
								.getOwnerFile().getName();

						final StringBuilder line = new StringBuilder();

						line.append(filenameI);
						line.append("\t");
						line.append(Integer.toString(firstI.getFromLine()));
						line.append("\t");
						line.append(Integer.toString(lastI.getToLine()));
						line.append("\t");
						line.append(filenameJ);
						line.append("\t");
						line.append(Integer.toString(firstJ.getFromLine()));
						line.append("\t");
						line.append(Integer.toString(lastJ.getToLine()));

						lines.add(line.toString());
					}
				}
			}

			for (final String line : lines) {
				this.writer.write(line);
				this.writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			this.writer.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	BufferedWriter writer;

	final Set<CloneSetInfo> cloneSets;

	final SortedSet<FileInfo> files;
}
