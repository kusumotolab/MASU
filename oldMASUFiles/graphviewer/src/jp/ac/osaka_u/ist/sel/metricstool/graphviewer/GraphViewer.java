package jp.ac.osaka_u.ist.sel.metricstool.graphviewer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.DISSOLUTION;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.IntraProceduralCFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.DefaultCFGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.main.MetricsTool;
import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageListener;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePool;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.InterProceduralPDG;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.IntraProceduralPDG;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGAcrossEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGControlDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGDataDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGExecutionDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.DefaultPDGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGDataInNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGDataOutNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGMethodEnterNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * ���͂��ꂽ�v���O������CFG�܂���PDG��graphviz�����ɏo�͂��郂�W���[��
 * 
 * @author higo
 * 
 */
public class GraphViewer extends MetricsTool {

	public static void main(String[] args) {

		try {

			// �R�}���h���C������������
			final Options options = new Options();

			{
				final Option d = new Option("d", "directory", true,
						"target directory");
				d.setArgName("directory");
				d.setArgs(1);
				d.setRequired(true);
				options.addOption(d);
			}

			{
				final Option l = new Option("l", "language", true,
						"programming language of analyzed source code");
				l.setArgName("language");
				l.setArgs(1);
				l.setRequired(true);
				options.addOption(l);
			}

			{
				final Option c = new Option("c", "ControlFlowGraph", true,
						"control flow graph");
				c.setArgName("file");
				c.setArgs(1);
				c.setRequired(false);
				options.addOption(c);
			}

			{
				final Option p = new Option("p", "ProgramDepencenceGraph",
						true, "program dependence graph");
				p.setArgName("file");
				p.setArgs(1);
				p.setRequired(false);
				options.addOption(p);
			}

			{
				final Option s = new Option("s", "SystemDepencenceGraph", true,
						"system dependence graph");
				s.setArgName("file");
				s.setArgs(1);
				s.setRequired(false);
				options.addOption(s);
			}

			{
				final Option o = new Option("o", "optimize", true,
						"remove unnecessary nodes from CFGs and PDGs");
				o.setArgName("boolean");
				o.setArgs(1);
				o.setRequired(false);
				options.addOption(o);
			}

			{
				final Option a = new Option("a", "atomic", true,
						"dissolve complicated statements into simple statements");
				a.setArgName("boolean");
				a.setArgs(1);
				a.setRequired(false);
				options.addOption(a);
			}

			{
				final Option t = new Option("t", "state", true,
						"count object state change on PDGs");
				t.setArgName("boolean");
				t.setArgs(1);
				t.setRequired(false);
				options.addOption(t);
			}

			final CommandLineParser parser = new PosixParser();
			final CommandLine cmd = parser.parse(options, args);

			// ��͗p�ݒ�
			Settings.getInstance().setLanguage(cmd.getOptionValue("l"));
			Settings.getInstance().addTargetDirectory(cmd.getOptionValue("d"));
			Settings.getInstance().setVerbose(true);

			// ���\���p�ݒ�
			{
				final Class<?> metricstool = MetricsTool.class;
				final Field out = metricstool.getDeclaredField("out");
				out.setAccessible(true);
				out.set(null, new DefaultMessagePrinter(new MessageSource() {
					public String getMessageSourceName() {
						return "graphviewer";
					}
				}, MESSAGE_TYPE.OUT));
				final Field err = metricstool.getDeclaredField("err");
				err.setAccessible(true);
				err.set(null, new DefaultMessagePrinter(new MessageSource() {
					public String getMessageSourceName() {
						return "graphviewer";
					}
				}, MESSAGE_TYPE.ERROR));
				MessagePool.getInstance(MESSAGE_TYPE.OUT).addMessageListener(
						new MessageListener() {
							public void messageReceived(MessageEvent event) {
								System.out.print(event.getSource()
										.getMessageSourceName()
										+ " > " + event.getMessage());
							}
						});
				MessagePool.getInstance(MESSAGE_TYPE.ERROR).addMessageListener(
						new MessageListener() {
							public void messageReceived(MessageEvent event) {
								System.err.print(event.getSource()
										.getMessageSourceName()
										+ " > " + event.getMessage());
							}
						});
			}

			// p �� s ��PDG�����̓s����C�����Ɏw��ł��Ȃ�
			if (cmd.hasOption("p") && cmd.hasOption("s")) {
				System.err
						.println("-p and -s cannot be used at the same time.");
				System.exit(0);
			}

			// �Ώۃf�B���N�g���ȉ���Java�t�@�C����o�^���C���
			{
				final GraphViewer viewer = new GraphViewer();
				viewer.analyzeLibraries();
				viewer.readTargetFiles();
				viewer.analyzeTargetFiles();
			}

			boolean optimize = true;
			if (cmd.hasOption("o")) {
				final String text = cmd.getOptionValue("o");
				if (text.equals("yes")) {
					optimize = true;
				} else if (text.equals("no")) {
					optimize = false;
				}
			}

			DISSOLUTION dissolve = DISSOLUTION.FALSE;
			if (cmd.hasOption("a")) {
				final String text = cmd.getOptionValue("a");
				if (text.equals("yes")) {
					dissolve = DISSOLUTION.TRUE;
				} else if (text.equals("partly")) {
					dissolve = DISSOLUTION.PARTLY;
				} else if (text.equals("no")) {
					dissolve = DISSOLUTION.FALSE;
				}
			}

			boolean state = false;
			if (cmd.hasOption("t")) {
				final String text = cmd.getOptionValue("t");
				if (text.equals("yes")) {
					state = true;
				} else if (text.equals("no")) {
					state = false;
				}
			}

			if (cmd.hasOption("c")) {
				out.println("building and outputing CFGs ...");
				final BufferedWriter writer = new BufferedWriter(
						new FileWriter(cmd.getOptionValue("c")));

				writer.write("digraph CFG {");
				writer.newLine();

				int createdGraphNumber = 0;
				for (final TargetMethodInfo method : DataManager.getInstance()
						.getMethodInfoManager().getTargetMethodInfos()) {

					writeMethodCFG(method, createdGraphNumber++, writer,
							optimize, dissolve);
				}

				for (final TargetConstructorInfo constructor : DataManager
						.getInstance().getMethodInfoManager()
						.getTargetConstructorInfos()) {

					writeMethodCFG(constructor, createdGraphNumber++, writer,
							optimize, dissolve);
				}

				writer.write("}");

				writer.close();
			}

			if (cmd.hasOption("p")) {
				out.println("building and outputing intraprocedural PDGs ...");
				final BufferedWriter writer = new BufferedWriter(
						new FileWriter(cmd.getOptionValue("p")));

				writer.write("digraph IntraproceduralPDG {");
				writer.newLine();

				int createdGraphNumber = 0;
				for (final TargetMethodInfo method : DataManager.getInstance()
						.getMethodInfoManager().getTargetMethodInfos()) {

					final IntraProceduralPDG pdg = new IntraProceduralPDG(
							method, new DefaultPDGNodeFactory(),
							new DefaultCFGNodeFactory(), true, true, true,
							state, optimize, dissolve);
					writePDG(pdg, createdGraphNumber++, writer);
				}

				for (final TargetConstructorInfo constructor : DataManager
						.getInstance().getMethodInfoManager()
						.getTargetConstructorInfos()) {

					final IntraProceduralPDG pdg = new IntraProceduralPDG(
							constructor, new DefaultPDGNodeFactory(),
							new DefaultCFGNodeFactory(), true, true, true,
							state, optimize, dissolve);
					writePDG(pdg, createdGraphNumber++, writer);
				}

				writer.write("}");

				writer.close();
			}

			if (cmd.hasOption("s")) {
				out.println("building and outputing interprocedural PDGs ...");
				final BufferedWriter writer = new BufferedWriter(
						new FileWriter(cmd.getOptionValue("s")));

				writer.write("digraph InterproceduralPDG {");
				writer.newLine();

				final Set<CallableUnitInfo> methods = new HashSet<CallableUnitInfo>();
				final MethodInfoManager methodManager = DataManager
						.getInstance().getMethodInfoManager();
				methods.addAll(methodManager.getTargetMethodInfos());
				methods.addAll(methodManager.getTargetConstructorInfos());
				final InterProceduralPDG pdg = new InterProceduralPDG(methods,
						true, true, true, true, optimize);

				writeSDG(pdg, writer);
				writer.write("}");

				writer.close();
			}

			out.println("successfully finished.");

		} catch (IOException e) {
			err.println(e.getMessage());
			System.exit(0);
		} catch (ParseException e) {
			err.println(e.getMessage());
			System.exit(0);
		} catch (NoSuchFieldException e) {
			err.println(e.getMessage());
			System.exit(0);
		} catch (IllegalAccessException e) {
			err.println(e.getMessage());
			System.exit(0);
		}
	}

	static private void writeMethodCFG(final CallableUnitInfo unit,
			final int createdGraphNumber, final BufferedWriter writer,
			final boolean optimize, final DISSOLUTION dissolve)
			throws IOException {

		final IntraProceduralCFG cfg = new IntraProceduralCFG(unit,
				new DefaultCFGNodeFactory(), optimize, dissolve);

		writer.write("subgraph cluster");
		writer.write(Integer.toString(createdGraphNumber));
		writer.write(" {");
		writer.newLine();

		writer.write("label = \"");
		writer.write(unit.getSignatureText());
		writer.write("\";");
		writer.newLine();

		final Map<CFGNode<? extends ExecutableElementInfo>, Integer> nodeLabels = new HashMap<CFGNode<? extends ExecutableElementInfo>, Integer>();
		for (final CFGNode<?> node : cfg.getAllNodes()) {
			nodeLabels.put(node, nodeLabels.size());
		}

		for (final Map.Entry<CFGNode<? extends ExecutableElementInfo>, Integer> entry : nodeLabels
				.entrySet()) {
			writer.write(Integer.toString(createdGraphNumber));
			writer.write(".");
			writer.write(Integer.toString(entry.getValue()));
			writer.write(" [style = filled, label = \"");
			writer.write(entry.getKey().getText().replace("\"", "\\\"")
					.replace("\\\\\"", "\\\\\\\""));
			writer.write("\"");

			// �m�[�h�̐F
			if (cfg.getEnterNode() == entry.getKey()) {
				writer.write(", fillcolor = aquamarine");
			} else if (cfg.getExitNodes().contains(entry.getKey())) {
				writer.write(", fillcolor = deeppink");
			} else {
				writer.write(", fillcolor = white");
			}

			// �m�[�h�̌`
			if (entry.getKey() instanceof CFGControlNode) {
				writer.write(", shape = diamond");
			} else {
				writer.write(", shape = ellipse");
			}

			writer.write("];");
			writer.newLine();
		}

		writeCFGEdges(cfg, nodeLabels, createdGraphNumber, writer);

		writer.write("}");
		writer.newLine();
	}

	static private void writeCFGEdges(
			final CFG cfg,
			final Map<CFGNode<? extends ExecutableElementInfo>, Integer> nodeLabels,
			final int createdGraphNumber, final BufferedWriter writer)
			throws IOException {

		if (null == cfg) {
			return;
		}

		final Set<CFGEdge> edges = new HashSet<CFGEdge>();
		for (final CFGNode<?> node : cfg.getAllNodes()) {
			edges.addAll(node.getBackwardEdges());
			edges.addAll(node.getForwardEdges());
		}

		for (final CFGEdge edge : edges) {
			writer.write(Integer.toString(createdGraphNumber));
			writer.write(".");
			final CFGNode<?> fromNode = edge.getFromNode();
			writer.write(Integer.toString(nodeLabels.get(fromNode)));
			writer.write(" -> ");
			writer.write(Integer.toString(createdGraphNumber));
			writer.write(".");
			final CFGNode<?> toNode = edge.getToNode();
			writer.write(Integer.toString(nodeLabels.get(toNode)));
			writer.write(" [style = solid, label=\""
					+ edge.getDependenceString() + "\"];");
			writer.newLine();
		}
	}

	static private void writePDG(final IntraProceduralPDG pdg,
			final int createdGraphNumber, final BufferedWriter writer)
			throws IOException {

		final CallableUnitInfo method = pdg.getMethodInfo();

		writer.write("subgraph cluster");
		writer.write(Integer.toString(createdGraphNumber));
		writer.write(" {");
		writer.newLine();

		writer.write("label = \"");
		writer.write(method.getSignatureText());
		writer.write("\";");
		writer.newLine();

		final Map<PDGNode<?>, Integer> nodeLabels = new HashMap<PDGNode<?>, Integer>();
		for (final PDGNode<?> node : pdg.getAllNodes()) {
			nodeLabels.put(node, nodeLabels.size());
		}

		for (final Map.Entry<PDGNode<?>, Integer> entry : nodeLabels.entrySet()) {
			writer.write(Integer.toString(createdGraphNumber));
			writer.write(".");
			writer.write(Integer.toString(entry.getValue()));
			writer.write(" [style = filled, label = \"");
			writer.write(entry.getKey().getText().replace("\"", "\\\"")
					.replace("\\\\\"", "\\\\\\\""));
			writer.write("\"");

			// �m�[�h�̐F
			if (entry.getKey() instanceof PDGMethodEnterNode) {
				writer.write(", fillcolor = aquamarine");
			} else if (pdg.getExitNodes().contains(entry.getKey())) {
				writer.write(", fillcolor = deeppink");
			} else if (entry.getKey() instanceof PDGDataInNode) {
				writer.write(", fillcolor = tomato");
			} else if (entry.getKey() instanceof PDGDataOutNode) {
				writer.write(", fillcolor = darkorange");
			} else {
				writer.write(", fillcolor = white");
			}

			// �m�[�h�̌`
			if (entry.getKey() instanceof PDGControlNode) {
				writer.write(", shape = diamond");
			} else if (entry.getKey() instanceof PDGDataInNode
					|| entry.getKey() instanceof PDGDataOutNode) {
				writer.write(", shape = box");
			} else {
				writer.write(", shape = ellipse");
			}

			writer.write("];");
			writer.newLine();
		}

		for (final PDGEdge edge : pdg.getAllEdges()) {
			writer.write(Integer.toString(createdGraphNumber));
			writer.write(".");
			writer.write(Integer.toString(nodeLabels.get(edge.getFromNode())));
			writer.write(" -> ");
			writer.write(Integer.toString(createdGraphNumber));
			writer.write(".");
			writer.write(Integer.toString(nodeLabels.get(edge.getToNode())));
			if (edge instanceof PDGDataDependenceEdge) {
				writer.write(" [style = solid, label=\""
						+ edge.getDependenceString() + "\"]");
			} else if (edge instanceof PDGControlDependenceEdge) {
				writer.write(" [style = dotted, label=\""
						+ edge.getDependenceString() + "\"]");
			} else if (edge instanceof PDGExecutionDependenceEdge) {
				writer.write(" [style = bold, label=\""
						+ edge.getDependenceString() + "\"]");
			}
			writer.write(";");
			writer.newLine();
		}

		writer.write("}");
		writer.newLine();
	}

	static private void writeSDG(final InterProceduralPDG pdg,
			final BufferedWriter writer) throws IOException {

		// �m�[�h��ID���쐬
		int nodeID = 0;
		final Map<PDGNode<?>, Integer> NODE_ID_MAP = new HashMap<PDGNode<?>, Integer>();
		for (final PDGNode<?> node : pdg.getAllNodes()) {
			NODE_ID_MAP.put(node, new Integer(nodeID++));
		}

		// ���\�b�h���O���t���쐬
		int graphID = 0;
		for (final IntraProceduralPDG unitPDG : pdg.getEntries()) {

			writer.write("subgraph cluster");
			writer.write(Integer.toString(graphID++));
			writer.write(" {");
			writer.newLine();

			final CallableUnitInfo method = unitPDG.getMethodInfo();
			writer.write("label = \"");
			writer.write(method.getSignatureText());
			writer.write("\";");
			writer.newLine();

			// �m�[�h���̏o��
			for (final PDGNode<?> node : unitPDG.getAllNodes()) {
				final Integer id = NODE_ID_MAP.get(node);
				writer.write(id.toString());
				writer.write(" [style = filled, label = \"");
				writer.write(node.getText().replace("\"", "\\\"").replace(
						"\\\\\"", "\\\\\\\""));
				writer.write("\"");

				// �m�[�h�̐F
				if (unitPDG.getMethodEnterNode() instanceof PDGMethodEnterNode) {
					writer.write(", fillcolor = aquamarine");
				} else if (unitPDG.getExitNodes().contains(node)) {
					writer.write(", fillcolor = deeppink");
				} else {
					writer.write(", fillcolor = white");
				}

				// �m�[�h�̌`
				if (node instanceof PDGControlNode) {
					writer.write(", shape = diamond");
				} else {
					writer.write(", shape = ellipse");
				}

				writer.write("];");
				writer.newLine();
			}

			// �G�b�W���̏o��
			for (final PDGEdge edge : unitPDG.getAllEdges()) {

				// �G�b�W�̊J�n���_�ƏI�����_�����Ƀ��\�b�h���ɂ���Ƃ��ɁC�G�b�W��`�悷��
				if (!unitPDG.getAllNodes().contains(edge.getFromNode())
						|| !unitPDG.getAllNodes().contains(edge.getToNode())) {
					continue;
				}

				writer.write(NODE_ID_MAP.get(edge.getFromNode()).toString());
				writer.write(" -> ");
				writer.write(NODE_ID_MAP.get(edge.getToNode()).toString());
				if (edge instanceof PDGDataDependenceEdge) {
					writer.write(" [style = solid, label=\""
							+ edge.getDependenceString() + "\"];");
				} else if (edge instanceof PDGControlDependenceEdge) {
					writer.write(" [style = dotted, label=\""
							+ edge.getDependenceString() + "\"];");
				} else if (edge instanceof PDGExecutionDependenceEdge) {
					writer.write(" [style = bold, label=\""
							+ edge.getDependenceString() + "\"];");
				}
				writer.newLine();
			}
			writer.write("}");
			writer.newLine();
		}

		// ���\�b�h���܂�����G�b�W�����o��
		for (final PDGAcrossEdge acrossEdge : pdg.getAcrossEdges()) {
			final PDGEdge edge = (PDGEdge) acrossEdge;
			writer.write(NODE_ID_MAP.get(edge.getFromNode()).toString());
			writer.write(" -> ");
			writer.write(NODE_ID_MAP.get(edge.getToNode()).toString());
			if (edge instanceof PDGDataDependenceEdge) {
				writer.write(" [style = solid, label=\""
						+ edge.getDependenceString() + "\"];");
			} else if (edge instanceof PDGControlDependenceEdge) {
				writer.write(" [style = dotted, label=\""
						+ edge.getDependenceString() + "\"];");
			} else if (edge instanceof PDGExecutionDependenceEdge) {
				writer.write(" [style = bold, label=\""
						+ edge.getDependenceString() + "\"];");
			}
			writer.newLine();
		}

		writer.newLine();
	}
}
