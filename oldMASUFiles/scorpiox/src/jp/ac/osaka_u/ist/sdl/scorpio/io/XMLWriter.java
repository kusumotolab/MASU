package jp.ac.osaka_u.ist.sdl.scorpio.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sdl.scorpio.PDGController;
import jp.ac.osaka_u.ist.sdl.scorpio.data.CloneSetInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.settings.Configuration;
import jp.ac.osaka_u.ist.sdl.scorpio.settings.DEPENDENCY_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.IntraProceduralPDG;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

public class XMLWriter {

	public XMLWriter(final String outputFile, final SortedSet<FileInfo> files,
			final SortedSet<TargetMethodInfo> methods,
			final SortedSet<TargetConstructorInfo> constructors,
			final SortedSet<CloneSetInfo> cloneSets) {

		this.files = files;
		this.methods = methods;
		this.constructors = constructors;
		this.cloneSets = cloneSets;

		try {
			this.writer = new BufferedWriter(new FileWriter(outputFile));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public void write() {

		// FileInfo�ƃt�@�C���ԍ��̃}�b�v���쐬
		final Map<FileInfo, Integer> fileToIntegerMap = new HashMap<FileInfo, Integer>();
		for (final FileInfo file : this.files) {
			final int value = fileToIntegerMap.size();
			fileToIntegerMap.put(file, value);
		}

		// �e�t�@�C���̏d���m�[�h�����쐬
		final Map<FileInfo, Set<PDGNode<?>>> fileToNodeMap = new HashMap<FileInfo, Set<PDGNode<?>>>();
		{
			for (final FileInfo file : this.files) {
				fileToNodeMap.put(file, new HashSet<PDGNode<?>>());
			}
			for (final CloneSetInfo cloneSet : this.cloneSets) {
				for (final CodeCloneInfo codeclone : cloneSet.getCodeClones()) {
					for (final PDGNode<?> node : codeclone.getRealElements()) {

						final ExecutableElementInfo element = node.getCore();
						final FileInfo ownerFile = ((TargetClassInfo) element
								.getOwnerMethod().getOwnerClass())
								.getOwnerFile();
						final Set<PDGNode<?>> nodes = fileToNodeMap
								.get(ownerFile);
						assert null != nodes : "Illegal State!";
						nodes.add(node);
					}
				}
			}
		}

		// MethodInfo�ƃ��\�b�h�ԍ��̃}�b�v���쐬
		final Map<CallableUnitInfo, Integer> methodToIntegerMap = new HashMap<CallableUnitInfo, Integer>();
		for (final TargetMethodInfo method : this.methods) {
			final int value = methodToIntegerMap.size();
			methodToIntegerMap.put(method, value);
		}
		for (final TargetConstructorInfo constructor : this.constructors) {
			final int value = methodToIntegerMap.size();
			methodToIntegerMap.put(constructor, value);
		}

		// XML�̃w�b�_���o��
		try {
			this.writer.write("<?xml version=\"1.0\"?>");
			this.writer.newLine();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		// �J�n�^�O���o��
		try {
			this.writer.write("<RESULT c=\"");
			this.writer.write(String.valueOf(Configuration.INSTANCE.getC()));
			this.writer.write("\" d=\"");
			for (final String directory : Configuration.INSTANCE.getD()) {
				this.writer.write(directory);
				this.writer.write(",");
			}
			this.writer.write("\" l=\"");
			this.writer.write(Configuration.INSTANCE.getL());
			this.writer.write("\" o=\"");
			this.writer.write(Configuration.INSTANCE.getO());
			this.writer.write("\" p=\"");
			this.writer.write(Configuration.INSTANCE.getP().getText());
			this.writer.write("\" q=\"");
			for (final DEPENDENCY_TYPE dependency : Configuration.INSTANCE
					.getQ()) {
				this.writer.write(dependency.getText());
				this.writer.write(",");
			}
			this.writer.write("\" s=\"");
			this.writer.write(String.valueOf(Configuration.INSTANCE.getS()));
			this.writer.write("\" pc=\"");
			this.writer.write(String.valueOf(Configuration.INSTANCE.getPC()
					.getText()));
			this.writer.write("\" pi=\"");
			this.writer.write(String.valueOf(Configuration.INSTANCE.getPI()
					.getText()));
			this.writer.write("\" pl=\"");
			this.writer.write(String.valueOf(Configuration.INSTANCE.getPL()
					.getText()));
			this.writer.write("\" po=\"");
			this.writer.write(String.valueOf(Configuration.INSTANCE.getPO()
					.getText()));
			this.writer.write("\" pr=\"");
			this.writer.write(String.valueOf(Configuration.INSTANCE.getPR()
					.getText()));
			this.writer.write("\" pv=\"");
			this.writer.write(String.valueOf(Configuration.INSTANCE.getPV()
					.getText()));
			this.writer.write("\">");
			this.writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// ���o�^�C�v���o��
		try {
			this.writer.write("<DETECTIONTYPE>");
			this.writer.write(Configuration.INSTANCE.getP().getText());
			this.writer.write("</DETECTIONTYPE>");
		} catch (final IOException e) {
			e.printStackTrace();
		}

		// �t�@�C�������o��
		try {
			this.writer.write("\t<FILEINFO>");
			this.writer.newLine();

			for (final Map.Entry<FileInfo, Integer> entry : fileToIntegerMap
					.entrySet()) {

				this.writer.write("\t\t<FILE>");
				this.writer.newLine();

				this.writer.write("\t\t\t<FILEID>" + entry.getValue()
						+ "</FILEID>");
				this.writer.newLine();

				this.writer.write("\t\t\t<FILELOC>" + entry.getKey().getLOC()
						+ "</FILELOC>");
				this.writer.newLine();

				final int number = this.getNumberOfNodes(entry.getKey());
				this.writer.write("\t\t\t<PDGNODE>" + number + "</PDGNODE>");
				this.writer.newLine();

				final Set<PDGNode<?>> duplicatedNodes = fileToNodeMap.get(entry
						.getKey());
				final float ratio = 100 * duplicatedNodes.size()
						/ (float) number;
				this.writer.write("\t\t\t<DUPLICATEDRADIO>" + ratio
						+ "</DUPLICATEDRADIO>");
				this.writer.newLine();

				this.writer.write("\t\t\t<FILEPATH>" + entry.getKey().getName()
						+ "</FILEPATH>");
				this.writer.newLine();

				this.writer.write("\t\t</FILE>");
				this.writer.newLine();
			}

			this.writer.write("\t</FILEINFO>");
			this.writer.newLine();

		} catch (final IOException e) {
			e.printStackTrace();
		}

		// ���\�b�h�����o��
		try {
			this.writer.write("\t<METHODINFO>");
			this.writer.newLine();

			for (final Map.Entry<CallableUnitInfo, Integer> entry : methodToIntegerMap
					.entrySet()) {

				final CallableUnitInfo unit = entry.getKey();

				this.writer.write("\t\t<METHOD>");
				this.writer.newLine();

				this.writer.write("\t\t\t<METHODNAME>"
						+ (unit instanceof MethodInfo ? ((MethodInfo) unit)
								.getMethodName() : "_init_") + "</METHODNAME>");
				this.writer.newLine();

				this.writer.write("\t\t\t<METHODID>" + entry.getValue()
						+ "</METHODID>");
				this.writer.newLine();

				this.writer.write("\t\t\t<DEFINITIONFILEID>");
				final FileInfo ownerFile = ((TargetClassInfo) unit
						.getOwnerClass()).getOwnerFile();
				final int fileID = fileToIntegerMap.get(ownerFile);
				this.writer.write(Integer.toString(fileID));
				this.writer.write("</DEFINITIONFILEID>");
				this.writer.newLine();

				this.writer.write("\t\t\t<METHODFROMLINE>"
						+ entry.getKey().getFromLine() + "</METHODFROMLINE>");
				this.writer.newLine();

				this.writer.write("\t\t\t<METHODTOLINE>"
						+ entry.getKey().getToLine() + "</METHODTOLINE>");
				this.writer.newLine();

				this.writer.write("\t\t</METHOD>");
				this.writer.newLine();
			}

			this.writer.write("\t</METHODINFO>");
			this.writer.newLine();

		} catch (final IOException e) {
			e.printStackTrace();
		}

		// �N���[�������o��
		try {
			this.writer.write("\t<CLONEINFO>");
			this.writer.newLine();

			for (final CloneSetInfo cloneSet : this.cloneSets) {

				// boolean a = false;
				// for (final CodeCloneInfo codeFragment : cloneSet
				// .getCodeClones()) {
				// if(2 == codeFragment.getOwnerCallableUnits().size()){
				// a = true;
				// }
				// }
				// if(!a){
				// continue;
				// }

				this.writer.write("\t\t<CLONESET>");				
				this.writer.newLine();
				
				this.writer.write("\t\t\t<HASH>");
				this.writer.write(Integer.toString(cloneSet.getHash()));
				this.writer.write("</HASH>");

				for (final CodeCloneInfo codeFragment : cloneSet
						.getCodeClones()) {

					this.writer.write("\t\t\t<CLONE>");
					this.writer.newLine();

					this.writer.write("\t\t\t\t<GAP>"
							+ codeFragment.getGapsNumber() + "</GAP>");
					this.writer.newLine();

					this.writer.write("\t\t\t\t<SPREAD>"
							+ codeFragment.getOwnerCallableUnits().size()
							+ "</SPREAD>");
					this.writer.newLine();

					for (final PDGNode<?> node : codeFragment.getRealElements()) {

						this.writer.write("\t\t\t\t<ELEMENT>");
						this.writer.newLine();

						final ExecutableElementInfo element = node.getCore();
						final CallableUnitInfo ownerMethod = element
								.getOwnerMethod();
						final ClassInfo ownerClass = ownerMethod
								.getOwnerClass();
						final FileInfo ownerFile = ((TargetClassInfo) ownerClass)
								.getOwnerFile();
						final int fileID = fileToIntegerMap.get(ownerFile);
						final int methodID = methodToIntegerMap
								.get(ownerMethod);

						this.writer.write("\t\t\t\t\t<OWNERFILEID>" + fileID
								+ "</OWNERFILEID>");
						this.writer.newLine();

						this.writer.write("\t\t\t\t\t<OWNERMETHODID>"
								+ methodID + "</OWNERMETHODID>");
						this.writer.newLine();

						this.writer.write("\t\t\t\t\t<ELEMENTFROMLINE>"
								+ element.getFromLine() + "</ELEMENTFROMLINE>");
						this.writer.newLine();

						this.writer.write("\t\t\t\t\t<ELEMENTFROMCOLUMN>"
								+ element.getFromColumn()
								+ "</ELEMENTFROMCOLUMN>");
						this.writer.newLine();

						this.writer.write("\t\t\t\t\t<ELEMENTTOLINE>"
								+ element.getToLine() + "</ELEMENTTOLINE>");
						this.writer.newLine();

						this.writer.write("\t\t\t\t\t<ELEMENTTOCOLUMN>"
								+ element.getToColumn() + "</ELEMENTTOCOLUMN>");
						this.writer.newLine();

						this.writer.write("\t\t\t\t</ELEMENT>");
						this.writer.newLine();
					}

					for (final CallInfo<? extends CallableUnitInfo> call : codeFragment
							.getCalls()) {

						final CallableUnitInfo callee = call.getCallee();
						if (null != callee
								&& methodToIntegerMap.containsKey(callee)) {
							this.writer.write("\t\t\t\t<CALLSITE>");
							this.writer.newLine();

							final int calleeID = methodToIntegerMap.get(callee);
							final CallableUnitInfo caller = call
									.getOwnerMethod();
							final int callerID = methodToIntegerMap.get(caller);

							this.writer.write("\t\t\t\t\t<CALLERID>" + callerID
									+ "</CALLERID>");
							this.writer.newLine();

							this.writer.write("\t\t\t\t\t<CALLEEID>" + calleeID
									+ "</CALLEEID>");
							this.writer.newLine();

							this.writer.write("\t\t\t\t\t<CALLFROMLINE>"
									+ call.getFromLine() + "</CALLFROMLINE>");
							this.writer.newLine();

							this.writer.write("\t\t\t\t\t<CALLFROMCOLUMN>"
									+ call.getFromColumn()
									+ "</CALLFROMCOLUMN>");
							this.writer.newLine();

							this.writer.write("\t\t\t\t\t<CALLTOLINE>"
									+ call.getToLine() + "</CALLTOLINE>");
							this.writer.newLine();

							this.writer.write("\t\t\t\t\t<CALLTOCOLUMN>"
									+ call.getToColumn() + "</CALLTOCOLUMN>");
							this.writer.newLine();

							this.writer.write("</CALLSITE>");
							this.writer.newLine();
						}
					}

					this.writer.write("\t\t\t</CLONE>");
					this.writer.newLine();
				}

				this.writer.write("\t\t</CLONESET>");
				this.writer.newLine();
			}

			this.writer.write("\t</CLONEINFO>");
			this.writer.newLine();

		} catch (final IOException e) {
			e.printStackTrace();
		}

		// �I���^�O���o��
		try {
			this.writer.write("</RESULT>");
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

	private int getNumberOfNodes(final FileInfo file) {

		final Set<IntraProceduralPDG> pdgs = PDGController.SINGLETON
				.getPDGs(file.getName());
		int number = 0;
		for (final IntraProceduralPDG pdg : pdgs) {
			number += pdg.getNumberOfNodes();
		}

		return number;

	}

	final Set<CloneSetInfo> cloneSets;

	final Set<TargetMethodInfo> methods;

	final Set<TargetConstructorInfo> constructors;

	final SortedSet<FileInfo> files;
}
