package jp.ac.osaka_u.ist.sdl.scdetector;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import jp.ac.osaka_u.ist.sdl.scdetector.data.ClonePairInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.data.CloneSetInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.data.NodePairInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.PDGController;
import jp.ac.osaka_u.ist.sdl.scdetector.io.XMLWriter;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.CALL_NORMALIZATION;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.CAST_NORMALIZATION;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.CONTROL_FILTER;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.Configuration;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.DEPENDENCY_TYPE;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.DISSOLVE;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.HEURISTICS;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.LITERAL_NORMALIZATION;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.MERGE;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.OPERATION_NORMALIZATION;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.OUTPUT_FORMAT;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.PDG_TYPE;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.REFERENCE_NORMALIZATION;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.SLICE_TYPE;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.SMALL_METHOD;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.VARIABLE_NORMALIZATION;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.VERBOSE;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.DISSOLUTION;
import jp.ac.osaka_u.ist.sel.metricstool.main.MetricsTool;
import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageListener;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePool;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.InterProceduralPDG;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.IntraProceduralPDG;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.PDG;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGControlDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGDataDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGExecutionDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.DefaultPDGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.IPDGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGDataNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGMethodEnterNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * �v���O�����ˑ��O���t�̓��`�ȃT�u�O���t�������R�[�h�N���[���Ƃ��Č��o����v���O���� ���o�Ώۂ͈����ŗ^������D
 * 
 * @author higo
 */
public class Scorpio extends MetricsTool {

	public static final String ID = "SCORPIO";

	public static void main(String[] args) {

		// �����̏���
		processArgs(args);

		// ��͂̐ݒ���s��
		doSettings();

		final long start = System.nanoTime();

		// �Ώۃf�B���N�g���ȉ���Java�t�@�C����o�^���C���
		final Scorpio scorpio = new Scorpio();
		scorpio.analyzeLibraries();
		scorpio.readTargetFiles();
		scorpio.analyzeTargetFiles();

		// PDG���\�z
		out.println("building PDGs ...");
		final IPDGNodeFactory pdgNodeFactory = buildPDGs();

		// PDG�m�[�h�̃n�b�V���f�[�^���\�z����
		out.println("constructing PDG nodes hashtable ...");
		final SortedMap<Integer, List<PDGNode<?>>> equivalenceGroups = buildEquivalenceGroups(pdgNodeFactory);

		// PDG�̋K�͂�\��
		printPDGsize(pdgNodeFactory);

		// �n�b�V���l������2��StatementInfo����_�ɂ��ăR�[�h�N���[�������o
		out.println("detecting code clones from PDGs ... ");
		final List<ClonePairInfo> clonepairList = detectClonePairs(equivalenceGroups);

		// ���̃N���[���Ɋ��S�Ɋ܂܂�Ă���N���[������菜��
		out.println("filtering out unnecessary clone pairs ...");
		final Set<ClonePairInfo> refinedClonePairs = refineClonePairs(clonepairList);

		// �N���[���y�A����N���[���Z�b�g�ɕϊ�
		out.println("converting clone pairs to clone sets ...");
		final SortedSet<CloneSetInfo> clonesets = convert(refinedClonePairs);

		// �N���[���Z�b�g���o��
		write(clonesets, pdgNodeFactory);

		// �v�Z�R�X�g��\��
		printComputationalCost();

		final long time = System.nanoTime() - start;
		out.println("elapsed time: " + time / (float) 1000000000);

		out.println("successifully finished.");
	}

	private static void processArgs(final String[] args) {

		try {

			// �R�}���h���C������������
			final Options options = new Options();

			{
				final Option b = new Option(
						"b",
						"libraries",
						true,
						"specify libraries (.jar file or .class file or directory that contains .jar and .class files)");
				b.setArgName("libraries");
				b.setArgs(1);
				b.setRequired(false);
				options.addOption(b);
			}

			{
				final Option c = new Option("c", "count", true,
						"count for filtering out repeated statements");
				c.setArgName("count");
				c.setArgs(1);
				c.setRequired(false);
				options.addOption(c);
			}

			{
				final Option d = new Option(
						"d",
						"directory",
						true,
						"specify target directories (separate with comma \',\' if you specify multiple directories");
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
				final Option e = new Option("e", "merge", true, "merge");
				e.setArgName("merge");
				e.setArgs(1);
				e.setRequired(false);
				options.addOption(e);
			}

			{
				final Option f = new Option("f", "dissolve", true, "dissolve");
				f.setArgName("dissolve");
				f.setArgs(1);
				f.setRequired(false);
				options.addOption(f);
			}

			{
				final Option g = new Option("g", "format", true, "format");
				g.setArgName("format");
				g.setArgs(1);
				g.setRequired(false);
				options.addOption(g);
			}

			{
				final Option h = new Option("h", "heuristics", true,
						"heuristics");
				h.setArgName("heuristics");
				h.setArgs(1);
				h.setRequired(false);
				options.addOption(h);
			}

			{
				final Option m = new Option("m", "method", true, "small method");
				m.setArgName("method");
				m.setArgs(1);
				m.setRequired(false);
				options.addOption(m);
			}

			{
				final Option o = new Option("o", "output", true, "output file");
				o.setArgName("output file");
				o.setArgs(1);
				o.setRequired(true);
				options.addOption(o);
			}

			{
				final Option p = new Option("p", "pdg", true, "pdg type");
				p.setArgName("pdg");
				p.setArgs(1);
				p.setRequired(false);
				options.addOption(p);
			}

			{
				final Option q = new Option("q", "dependency", true,
						"dependency type");
				q.setArgName("dependency");
				q.setArgs(1);
				q.setRequired(false);
				options.addOption(q);
			}

			{
				final Option s = new Option("s", "size", true,
						"lower size of detected clone");
				s.setArgName("size");
				s.setArgs(1);
				s.setRequired(false);
				options.addOption(s);
			}

			{
				final Option t = new Option("t", "slice", true, "slice type");
				t.setArgName("slice");
				t.setArgs(1);
				t.setRequired(false);
				options.addOption(t);
			}

			{
				final Option u = new Option("u", "control", true,
						"control node");
				u.setArgName("control");
				u.setArgs(1);
				u.setRequired(false);
				options.addOption(u);
			}

			{
				final Option v = new Option("v", "verbose", true,
						"verbose output");
				v.setArgName("verbose");
				v.setArgs(1);
				v.setRequired(false);
				options.addOption(v);
			}

			{
				final Option w = new Option("w", "thread", true,
						"number of threads");
				w.setArgName("thread");
				w.setArgs(1);
				w.setRequired(false);
				options.addOption(w);
			}

			{
				final Option x = new Option("x", "distance", true,
						"distance of data dependency");
				x.setArgName("distance");
				x.setArgs(1);
				x.setRequired(false);
				options.addOption(x);
			}

			{
				final Option y = new Option("y", "distance", true,
						"distance of control dependency");
				y.setArgName("distance");
				y.setArgs(1);
				y.setRequired(false);
				options.addOption(y);
			}

			{
				final Option z = new Option("z", "distance", true,
						"distance of execution dependency");
				z.setArgName("distance");
				z.setArgs(1);
				z.setRequired(false);
				options.addOption(z);
			}

			{
				final Option pv = new Option("pv", true,
						"parameterize variables");
				pv.setArgName("variable parameterization level");
				pv.setArgs(1);
				pv.setRequired(false);
				pv.setType(Integer.class);
				options.addOption(pv);
			}

			{
				final Option pi = new Option("pi", true,
						"parameterize invocations");
				pi.setArgName("invocation parameterization level");
				pi.setArgs(1);
				pi.setRequired(false);
				pi.setType(Integer.class);
				options.addOption(pi);
			}

			{
				final Option po = new Option("po", true,
						"parameterize operations");
				po.setArgName("operation parameterization level");
				po.setArgs(1);
				po.setRequired(false);
				po.setType(Integer.class);
				options.addOption(po);
			}

			{
				final Option pl = new Option("pl", true,
						"parameterize literals");
				pl.setArgName("literal parameterization level");
				pl.setArgs(1);
				pl.setRequired(false);
				pl.setType(Integer.class);
				options.addOption(pl);
			}

			{
				final Option pc = new Option("pc", true, "parameterize casts");
				pc.setArgName("cast parameterization level");
				pc.setArgs(1);
				pc.setRequired(false);
				pc.setType(Integer.class);
				options.addOption(pc);
			}

			{
				final Option pr = new Option("pr", false,
						"parameterize class references");
				pr.setArgs(1);
				pr.setRequired(false);
				options.addOption(pr);
			}

			final CommandLineParser parser = new PosixParser();
			final CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption("b")) {
				final StringTokenizer tokenizer = new StringTokenizer(cmd
						.getOptionValue("b"), ",");
				while (tokenizer.hasMoreElements()) {
					final String library = tokenizer.nextToken();
					Settings.getInstance().addLibrary(library);
				}
			}
			if (cmd.hasOption("c")) {
				Configuration.INSTANCE.setC(Integer.valueOf(cmd
						.getOptionValue("c")));
			}
			{
				final StringTokenizer tokenizer = new StringTokenizer(cmd
						.getOptionValue("d"), ",");
				while (tokenizer.hasMoreElements()) {
					final String directory = tokenizer.nextToken();
					Configuration.INSTANCE.addD(directory);
				}
			}
			if (cmd.hasOption("e")) {
				final String merge = cmd.getOptionValue("e");
				if (merge.equalsIgnoreCase("yes")) {
					Configuration.INSTANCE.setE(MERGE.TRUE);
				} else if (merge.equalsIgnoreCase("no")) {
					Configuration.INSTANCE.setE(MERGE.FALSE);
				} else {
					System.err.println("Unknown option : " + merge);
					System.err
							.println("\"-e\" option must have \"yes\" or \"no\"");
					System.exit(0);
				}
			}
			if (cmd.hasOption("f")) {
				final String dissolve = cmd.getOptionValue("f");
				if (dissolve.equalsIgnoreCase("yes")) {
					Configuration.INSTANCE.setF(DISSOLUTION.TRUE);
				} else if (dissolve.equalsIgnoreCase("partly")) {
					Configuration.INSTANCE.setF(DISSOLUTION.PARTLY);
				} else if (dissolve.equalsIgnoreCase("no")) {
					Configuration.INSTANCE.setF(DISSOLUTION.FALSE);
				} else {
					System.err.println("Unknown option : " + dissolve);
					System.err
							.println("\"-f\" option must have \"yes\" or \"no\"");
					System.exit(0);
				}
			}
			if (cmd.hasOption("g")) {
				final String format = cmd.getOptionValue("g");
				if (format.equalsIgnoreCase("set")) {
					Configuration.INSTANCE.setG(OUTPUT_FORMAT.SET);
				} else if (format.equalsIgnoreCase("pair")) {
					Configuration.INSTANCE.setG(OUTPUT_FORMAT.PAIR);
				} else {
					System.err.println("Unknown option : " + format);
					System.err
							.println("\"-g\" option must have \"set\" or \"pair\"");
					System.exit(0);
				}
			}
			if (cmd.hasOption("h")) {
				final String heuristics = cmd.getOptionValue("h");
				if (heuristics.equalsIgnoreCase("on")) {
					Configuration.INSTANCE.setH(HEURISTICS.ON);
				} else if (heuristics.equalsIgnoreCase("off")) {
					Configuration.INSTANCE.setH(HEURISTICS.OFF);
				} else {
					System.err.println("Unknown option : " + heuristics);
					System.err
							.println("\"-h\" option must have \"on\" or \"off\"");
					System.exit(0);
				}
			}
			Configuration.INSTANCE.setL(cmd.getOptionValue("l"));
			if (cmd.hasOption("m")) {
				final String smallmethod = cmd.getOptionValue("m");
				if (smallmethod.equalsIgnoreCase("hashed")) {
					Configuration.INSTANCE.setM(SMALL_METHOD.HASHED);
				} else if (smallmethod.equalsIgnoreCase("unhashed")) {
					Configuration.INSTANCE.setM(SMALL_METHOD.UNHASHED);
				} else {
					System.err.println("Unknown option : " + smallmethod);
					System.err
							.println("\"-m\" option must have \"hashed\" or \"unhashed\"");
					System.exit(0);
				}
			}
			Configuration.INSTANCE.setO(cmd.getOptionValue("o"));
			if (cmd.hasOption("p")) {
				final String pdg = cmd.getOptionValue("p");
				if (pdg.equalsIgnoreCase("intra")) {
					Configuration.INSTANCE.setP(PDG_TYPE.INTRA);
				} else if (pdg.equalsIgnoreCase("inter")) {
					Configuration.INSTANCE.setP(PDG_TYPE.INTER);
					if (DISSOLUTION.FALSE == Configuration.INSTANCE.getF()) {
						Configuration.INSTANCE.setF(DISSOLUTION.PARTLY);
					}

				} else {
					System.err.println("Unknown option : " + pdg);
					System.err
							.println("\"-p\" option must have \"intra\" or \"inter\"");
					System.exit(0);
				}
			}
			if (cmd.hasOption("q")) {
				Configuration.INSTANCE.resetQ();
				final StringTokenizer tokenizer = new StringTokenizer(cmd
						.getOptionValue("q"), ",");
				while (tokenizer.hasMoreTokens()) {
					final String dependency = tokenizer.nextToken();
					if (dependency.equalsIgnoreCase("data")) {
						Configuration.INSTANCE.addQ(DEPENDENCY_TYPE.DATA);
					} else if (dependency.equalsIgnoreCase("control")) {
						Configuration.INSTANCE.addQ(DEPENDENCY_TYPE.CONTROL);
					} else if (dependency.equalsIgnoreCase("execution")) {
						Configuration.INSTANCE.addQ(DEPENDENCY_TYPE.EXECUTION);
					} else {
						System.err.println("Unknown option : " + dependency);
						System.err
								.println("\"-q\" option must have \"data\", \"control\", or \"execution\"");
						System.exit(0);
					}
				}

			}
			if (cmd.hasOption("s")) {
				Configuration.INSTANCE.setS(Integer.valueOf(cmd
						.getOptionValue("s")));
			}
			if (cmd.hasOption("t")) {
				Configuration.INSTANCE.resetT();
				final StringTokenizer tokenizer = new StringTokenizer(cmd
						.getOptionValue("t"), ",");
				while (tokenizer.hasMoreTokens()) {
					final String slice = tokenizer.nextToken();
					if (slice.equalsIgnoreCase("backward")) {
						Configuration.INSTANCE.addT(SLICE_TYPE.BACKWARD);
					} else if (slice.equalsIgnoreCase("forward")) {
						Configuration.INSTANCE.addT(SLICE_TYPE.FORWARD);
					} else {
						System.err.println("Unknown option : " + slice);
						System.err
								.println("\"-t\" option must have \"backward\", or \"forward\"");
						System.exit(0);
					}
				}
			}
			if (cmd.hasOption("u")) {
				final String control = cmd.getOptionValue("u");
				if (control.equalsIgnoreCase("yes")) {
					Configuration.INSTANCE.setU(CONTROL_FILTER.USE);
				} else if (control.equalsIgnoreCase("no")) {
					Configuration.INSTANCE.setU(CONTROL_FILTER.NO_USE);
				} else {
					System.err.println("Unknown option : " + control);
					System.err
							.println("\"-u\" option must have \"yes\" or \"no\"");
					System.exit(0);
				}
			}
			if (cmd.hasOption("s")) {
				Configuration.INSTANCE.setS(Integer.valueOf(cmd
						.getOptionValue("s")));
			}
			if (cmd.hasOption("v")) {
				final String verbose = cmd.getOptionValue("v");
				if (verbose.equalsIgnoreCase("yes")) {
					Configuration.INSTANCE.setV(VERBOSE.TRUE);
				} else if (verbose.equalsIgnoreCase("no")) {
					Configuration.INSTANCE.setV(VERBOSE.FALSE);
				} else {
					System.err.println("Unknown option : " + verbose);
					System.err
							.println("\"-v\" option must have \"yes\" or \"no\"");
					System.exit(0);
				}
			}
			if (cmd.hasOption("w")) {
				Configuration.INSTANCE.setW(Integer.valueOf(cmd
						.getOptionValue("w")));
			}
			if (cmd.hasOption("x")) {
				Configuration.INSTANCE.setX(Integer.valueOf(cmd
						.getOptionValue("x")));
			}
			if (cmd.hasOption("y")) {
				Configuration.INSTANCE.setY(Integer.valueOf(cmd
						.getOptionValue("y")));
			}
			if (cmd.hasOption("z")) {
				Configuration.INSTANCE.setZ(Integer.valueOf(cmd
						.getOptionValue("z")));
			}
			if (cmd.hasOption("pv")) {
				final String text = cmd.getOptionValue("pv");
				if (text.equalsIgnoreCase("no")) {
					Configuration.INSTANCE.setPV(VARIABLE_NORMALIZATION.NO);
				} else if (text.equalsIgnoreCase("type")) {
					Configuration.INSTANCE.setPV(VARIABLE_NORMALIZATION.TYPE);
				} else if (text.equalsIgnoreCase("all")) {
					Configuration.INSTANCE.setPV(VARIABLE_NORMALIZATION.ALL);
				} else {
					System.err.println("Unknown option : " + text);
					System.err
							.println("\"-pv\" option must have \"no\", \"type\", or \"all\"");
					System.exit(0);
				}
			}
			if (cmd.hasOption("pi")) {
				final String text = cmd.getOptionValue("pi");
				if (text.equalsIgnoreCase("fqn")) {
					Configuration.INSTANCE.setPI(CALL_NORMALIZATION.FQN);
				} else if (text.equalsIgnoreCase("no")) {
					Configuration.INSTANCE.setPI(CALL_NORMALIZATION.NO);
				} else if (text.equalsIgnoreCase("type_with_arg")) {
					Configuration.INSTANCE
							.setPI(CALL_NORMALIZATION.TYPE_WITH_ARG);
				} else if (text.equalsIgnoreCase("type_without_arg")) {
					Configuration.INSTANCE
							.setPI(CALL_NORMALIZATION.TYPE_WITHOUT_ARG);
				} else if (text.equalsIgnoreCase("all")) {
					Configuration.INSTANCE.setPI(CALL_NORMALIZATION.ALL);
				} else {
					System.err.println("Unknown option : " + text);
					System.err
							.println("\"-pi\" option must have \"no\", \"type_with_arg\", \"type_without_arg\", or \"all\"");
					System.exit(0);
				}
			}
			if (cmd.hasOption("po")) {
				final String text = cmd.getOptionValue("po");
				if (text.equalsIgnoreCase("no")) {
					Configuration.INSTANCE.setPO(OPERATION_NORMALIZATION.NO);
				} else if (text.equalsIgnoreCase("type")) {
					Configuration.INSTANCE.setPO(OPERATION_NORMALIZATION.TYPE);
				} else if (text.equalsIgnoreCase("all")) {
					Configuration.INSTANCE.setPO(OPERATION_NORMALIZATION.ALL);
				} else {
					System.err.println("Unknown option : " + text);
					System.err
							.println("\"-po\" option must have \"no\", \"type\", or \"all\"");
					System.exit(0);
				}
			}
			if (cmd.hasOption("pl")) {
				final String text = cmd.getOptionValue("pl");
				if (text.equalsIgnoreCase("no")) {
					Configuration.INSTANCE.setPL(LITERAL_NORMALIZATION.NO);
				} else if (text.equalsIgnoreCase("type")) {
					Configuration.INSTANCE.setPL(LITERAL_NORMALIZATION.TYPE);
				} else if (text.equalsIgnoreCase("all")) {
					Configuration.INSTANCE.setPL(LITERAL_NORMALIZATION.ALL);
				} else {
					System.err.println("Unknown option : " + text);
					System.err
							.println("\"-pl\" option must have \"no\", \"type\", or \"all\"");
					System.exit(0);
				}
			}
			if (cmd.hasOption("pc")) {
				final String text = cmd.getOptionValue("pc");
				if (text.equalsIgnoreCase("no")) {
					Configuration.INSTANCE.setPC(CAST_NORMALIZATION.NO);
				} else if (text.equalsIgnoreCase("type")) {
					Configuration.INSTANCE.setPC(CAST_NORMALIZATION.TYPE);
				} else if (text.equalsIgnoreCase("all")) {
					Configuration.INSTANCE.setPC(CAST_NORMALIZATION.ALL);
				} else {
					System.err.println("Unknown option : " + text);
					System.err
							.println("\"-pc\" option must have \"no\", \"type\", or \"all\"");
					System.exit(0);
				}
			}
			if (cmd.hasOption("pr")) {
				final String text = cmd.getOptionValue("pr");
				if (text.equalsIgnoreCase("no")) {
					Configuration.INSTANCE.setPR(REFERENCE_NORMALIZATION.NO);
				} else if (text.equalsIgnoreCase("all")) {
					Configuration.INSTANCE.setPR(REFERENCE_NORMALIZATION.ALL);
				} else {
					System.err.println("Unknown option : " + text);
					System.err
							.println("\"-pr\" option must have \"no\" or \"all\"");
					System.exit(0);
				}
			}

			// �R�}���h���C���ݒ�ɃG���[�����邩�ǂ������m�F
			if (Configuration.INSTANCE.getP().useInterProceduralPDG()
					&& Configuration.INSTANCE.getE().isMerge()) {
				System.err
						.println("-e option cannot be used in interprocedural PDG mode.");
				System.exit(0);
			}

			if ((Configuration.INSTANCE.getF() == DISSOLUTION.TRUE || Configuration.INSTANCE
					.getF() == DISSOLUTION.PARTLY)
					&& Configuration.INSTANCE.getE().isMerge()) {
				System.err
						.println("-f and -e options cannot be set as \"yes\" at the same time.");
			}

		} catch (ParseException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}

	private static void doSettings() {

		// ���\���p�ݒ�
		try {
			final Class<?> metricstool = MetricsTool.class;
			final Field out = metricstool.getDeclaredField("out");
			out.setAccessible(true);
			out.set(null, new DefaultMessagePrinter(new MessageSource() {
				public String getMessageSourceName() {
					return "scorpio";
				}
			}, MESSAGE_TYPE.OUT));
			final Field err = metricstool.getDeclaredField("err");
			err.setAccessible(true);
			err.set(null, new DefaultMessagePrinter(new MessageSource() {
				public String getMessageSourceName() {
					return "scorpio";
				}
			}, MESSAGE_TYPE.ERROR));
			if (Configuration.INSTANCE.getV().isVerbose()) {
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
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		// ��͗p�ݒ�
		Settings.getInstance().setLanguage(Configuration.INSTANCE.getL());
		for (final String directory : Configuration.INSTANCE.getD()) {
			Settings.getInstance().addTargetDirectory(directory);
		}
		Settings.getInstance().setVerbose(true);
		Settings.getInstance().setThreadNumber(Configuration.INSTANCE.getW());
	}

	private static final IPDGNodeFactory buildPDGs() {

		// -f yes �̂Ƃ��Ƀ}���`�X���b�h��PDG�����܂��\�z�ł��Ȃ����߁C���̂悤�ȏ�����ǉ����Ă���
		final int threadNumber = Configuration.INSTANCE.getW();
		Configuration.INSTANCE.setW(1);

		final IPDGNodeFactory pdgNodeFactory = new DefaultPDGNodeFactory();
		final boolean data = Configuration.INSTANCE.getQ().contains(
				DEPENDENCY_TYPE.DATA);
		final boolean control = Configuration.INSTANCE.getQ().contains(
				DEPENDENCY_TYPE.CONTROL);
		final boolean execution = Configuration.INSTANCE.getQ().contains(
				DEPENDENCY_TYPE.EXECUTION);
		final DISSOLUTION dissolve = Configuration.INSTANCE.getF();
		final int dataDistance = Configuration.INSTANCE.getX();
		final int controlDistance = Configuration.INSTANCE.getY();
		final int executionDistance = Configuration.INSTANCE.getZ();

		// �e���\�b�h��PDG���\�z
		final TargetMethodInfo[] methods = DataManager.getInstance()
				.getMethodInfoManager().getTargetMethodInfos().toArray(
						new TargetMethodInfo[0]);
		buildPDGs(methods, pdgNodeFactory, data, control, execution, dissolve,
				dataDistance, controlDistance, executionDistance);

		// �e�R���X�g���N�^��PDG���\�z
		final TargetConstructorInfo[] constructors = DataManager.getInstance()
				.getMethodInfoManager().getTargetConstructorInfos().toArray(
						new TargetConstructorInfo[0]);
		buildPDGs(constructors, pdgNodeFactory, data, control, execution,
				dissolve, dataDistance, controlDistance, executionDistance);

		switch (Configuration.INSTANCE.getP()) {

		case INTRA:

			// ���_�W�񂪎w�肳��Ă���ꍇ�́CPDG��ϊ�����
			// ���݂̂Ƃ���C���_�W���Inter�ł͂ł��Ȃ�
			if (Configuration.INSTANCE.getE().equals(MERGE.TRUE)) {
				out.println("optimizing PDGs ... ");
				for (final IntraProceduralPDG pdg : PDGController.SINGLETON
						.getPDGs()) {
					PDGMergedNode.merge((IntraProceduralPDG) pdg,
							pdgNodeFactory);
				}
			}

			break;

		case INTER:
			final InterProceduralPDG pdg = new InterProceduralPDG(
					PDGController.SINGLETON.getPDGs(), data, control, execution);

			break;
		default:
			assert false : "Here shouldn't be reached!";
		}

		Configuration.INSTANCE.setW(threadNumber);

		return pdgNodeFactory;
	}

	private static <T extends CallableUnitInfo> void buildPDGs(
			final T[] methods, final IPDGNodeFactory pdgNodeFactory,
			final boolean data, final boolean control, final boolean execution,
			final DISSOLUTION dissolve, final int dataDistance,
			final int controlDistance, final int executionDistance) {

		final AtomicInteger index = new AtomicInteger(0);
		final Thread[] threads = new Thread[Configuration.INSTANCE.getW()];
		for (int i = 0; i < threads.length; i++) {

			threads[i] = new Thread(new PDGBuildingThread<T>(methods, index,
					pdgNodeFactory, data, control, execution, true, dissolve,
					dataDistance, controlDistance, executionDistance));
			MetricsToolSecurityManager.getInstance().addPrivilegeThread(
					threads[i]);
			threads[i].start();
		}

		for (final Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static SortedMap<Integer, List<PDGNode<?>>> buildEquivalenceGroups(
			final IPDGNodeFactory pdgNodeFactory) {

		final SortedMap<Integer, List<PDGNode<?>>> equivalenceGroups = new TreeMap<Integer, List<PDGNode<?>>>();

		ALLNODE: for (final PDGNode<?> pdgNode : pdgNodeFactory.getAllNodes()) {

			{
				// case �G���g���͓o�^���Ȃ�
				final ExecutableElementInfo core = pdgNode.getCore();
				if (core instanceof CaseEntryInfo) {
					continue ALLNODE;
				}
			}

			// �ח��x�̎���Control�m�[�h�͓o�^���Ȃ�
			if (Configuration.INSTANCE.getF() == DISSOLUTION.TRUE
					|| Configuration.INSTANCE.getF() == DISSOLUTION.PARTLY) {
				if (pdgNode instanceof PDGControlNode) {
					continue ALLNODE;
				}
			}

			// �f�[�^�m�[�h�͓o�^���Ȃ�
			if (pdgNode instanceof PDGDataNode<?>) {
				continue ALLNODE;
			}

			// ���������\�b�h�͓o�^���Ȃ�
			switch (Configuration.INSTANCE.getM()) {
			case UNHASHED:

				// �W��m�[�h�̂Ƃ��͓��ʏ���
				if (pdgNode instanceof PDGMergedNode) {

				}

				// �W��m�[�h�ȊO�̎��͕��ʂɏ���
				else {

					switch (Configuration.INSTANCE.getP()) {
					case INTRA:

						final PDG pdg = PDGController.SINGLETON.getPDG(pdgNode);
						if (pdg.getNumberOfNodes() < Configuration.INSTANCE
								.getS()) {
							continue ALLNODE;
						}

						break;

					case INTER:

						// Inter�̎��͉������Ȃ�

						break;
					default:
						assert false : "Here shouldn't be reached!";
					}

				}
				break;
			default:
				break;
			}

			final ExecutableElementInfo element = pdgNode.getCore();
			final String converted = Conversion.getNormalizedElement(element);
			final int hash = converted.hashCode();
			List<PDGNode<?>> group = equivalenceGroups.get(hash);
			if (null == group) {
				group = new ArrayList<PDGNode<?>>();
				equivalenceGroups.put(hash, group);
			}
			group.add(pdgNode);
		}

		return equivalenceGroups;
	}

	private static void printPDGsize(final IPDGNodeFactory pdgNodeFactory) {

		{ // PDG�m�[�h�̐���\��
			final StringBuilder text = new StringBuilder();
			text.append("the number of PDG nodes is ");
			text.append(pdgNodeFactory.getAllNodes().size());
			text.append(".");
			out.println(text.toString());
		}

		{ // PDG�G�b�W�̐���\��
			final Set<PDGEdge> edges = new HashSet<PDGEdge>();
			for (final Entry<CallableUnitInfo, IntraProceduralPDG> entry : PDGController.SINGLETON
					.entrySet()) {
				edges.addAll(entry.getValue().getAllEdges());
			}

			final StringBuilder text = new StringBuilder();
			text.append("the number of dependency is ");
			text.append(edges.size());
			text.append(" (data:");
			text.append(PDGDataDependenceEdge.extractDataDependenceEdge(edges)
					.size());
			text.append(", control:");
			text.append(PDGControlDependenceEdge.extractControlDependenceEdge(
					edges).size());
			text.append(", execution:");
			text.append(PDGExecutionDependenceEdge
					.extractExecutionDependenceEdge(edges).size());
			text.append(").");
			out.println(text.toString());
		}
	}

	private static List<ClonePairInfo> detectClonePairs(
			final SortedMap<Integer, List<PDGNode<?>>> equivalenceGroups) {

		// �o�����ɂ��C�K�v�̂Ȃ��Ǝv����m�[�h����菜��
		if (Configuration.INSTANCE.getH() == HEURISTICS.ON) {
			equivalenceGroups.remove(1495279981); // String a = xxx + yyy;
			equivalenceGroups.remove(543405271); // throw b;
			equivalenceGroups.remove("boolean=boolean".hashCode());
			equivalenceGroups.remove("boolean=boolean&boolean".hashCode());
			equivalenceGroups.remove("boolean=boolean&&boolean".hashCode());
			equivalenceGroups.remove("boolean=boolean||boolean".hashCode());
			equivalenceGroups.remove("boolean=char!=char".hashCode());
			equivalenceGroups.remove("boolean=char==char".hashCode());
			equivalenceGroups.remove("boolean=char>=char".hashCode());
			equivalenceGroups.remove("boolean=char<=char".hashCode());
			equivalenceGroups.remove("boolean=long>int".hashCode());
			equivalenceGroups.remove("boolean=(boolean)".hashCode());
			equivalenceGroups.remove("byte=byte".hashCode());
			equivalenceGroups.remove("byte=byte<<int".hashCode());
			equivalenceGroups.remove("byte=(byte)".hashCode());
			equivalenceGroups.remove("int=byte&int".hashCode());
			equivalenceGroups.remove("int=int".hashCode());
			equivalenceGroups.remove("int=int-int".hashCode());
			equivalenceGroups.remove("int=byte&int".hashCode());
			equivalenceGroups.remove("int=(int)".hashCode());
			equivalenceGroups.remove("int=int+int".hashCode());
			equivalenceGroups.remove("int+=int".hashCode());
			equivalenceGroups.remove("long+=int".hashCode());
		}

		final List<ClonePairInfo> clonepairList = Collections
				.synchronizedList(new ArrayList<ClonePairInfo>());
		final Thread[] threads = new Thread[Configuration.INSTANCE.getW()];
		final List<NodePairInfo> nodepairs = makeNodePairs(equivalenceGroups
				.values());
		final AtomicInteger index = new AtomicInteger(0);

		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new SlicingThread(nodepairs, index,
					clonepairList));
			threads[i].start();
		}

		// �S�ẴX���b�h���I���̂�҂�
		for (final Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return clonepairList;
	}

	private static Set<ClonePairInfo> refineClonePairs(
			final List<ClonePairInfo> clonepairList) {

		// �N���[���y�A���X�g�̃O���[�v�𐶐��C����̓t�B���^�����O�Ɏg�p
		ConcurrentMap<PDGNode<?>, List<ClonePairInfo>> clonepairListGroup = new ConcurrentHashMap<PDGNode<?>, List<ClonePairInfo>>();
		for (final ClonePairInfo clonepair : clonepairList) {
			for (final PDGNode<?> node : clonepair.codecloneA.getRealElements()) {
				List<ClonePairInfo> list = clonepairListGroup.get(node);
				if (null == list) {
					list = Collections
							.synchronizedList(new ArrayList<ClonePairInfo>());
					clonepairListGroup.put(node, list);
				}
				list.add(clonepair);
			}

			for (final PDGNode<?> node : clonepair.codecloneB.getRealElements()) {
				List<ClonePairInfo> list = clonepairListGroup.get(node);
				if (null == list) {
					list = Collections
							.synchronizedList(new ArrayList<ClonePairInfo>());
					clonepairListGroup.put(node, list);
				}
				list.add(clonepair);
			}
		}

		// �t�B���^�����O��̃N���[��������ϐ�
		final Set<ClonePairInfo> refined = Collections
				.synchronizedSet(new HashSet<ClonePairInfo>());

		// �t�B���^�����O�����̓}���`�X���b�h�ōs��
		final Thread[] threads = new Thread[Configuration.INSTANCE.getW()];
		final AtomicInteger index = new AtomicInteger(0);
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new CloneFilteringThread(clonepairList,
					clonepairListGroup, index, refined));
			threads[i].start();
		}

		// �S�ẴX���b�h���I���̂�҂�
		for (final Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return refined;
	}

	private static SortedSet<CloneSetInfo> convert(
			final Set<ClonePairInfo> clonepairs) {

		if (Configuration.INSTANCE.getG() == OUTPUT_FORMAT.SET) {

			final Map<CodeCloneInfo, CloneSetInfo> cloneSetBag = new HashMap<CodeCloneInfo, CloneSetInfo>();

			for (final ClonePairInfo clonePair : clonepairs) {

				final CodeCloneInfo cloneA = clonePair.codecloneA;
				final CodeCloneInfo cloneB = clonePair.codecloneB;

				final CloneSetInfo cloneSetA = cloneSetBag.get(cloneA);
				final CloneSetInfo cloneSetB = cloneSetBag.get(cloneB);

				// �R�[�h��A�CB�Ƃ����łɓo�^����Ă���ꍇ
				if ((null != cloneSetA) && (null != cloneSetB)) {

					// A �� B�̏�������N���[���Z�b�g���Ⴄ�ꍇ�́C��������
					if (cloneSetA != cloneSetB) {
						final CloneSetInfo cloneSetC = new CloneSetInfo();
						cloneSetC.addAll(cloneSetA.getCodeClones());
						cloneSetC.addAll(cloneSetB.getCodeClones());

						for (final CodeCloneInfo codeFragment : cloneSetA
								.getCodeClones()) {
							cloneSetBag.remove(codeFragment);
						}
						for (final CodeCloneInfo codeFragment : cloneSetB
								.getCodeClones()) {
							cloneSetBag.remove(codeFragment);
						}

						for (final CodeCloneInfo codeFragment : cloneSetC
								.getCodeClones()) {
							cloneSetBag.put(codeFragment, cloneSetC);
						}
					}

				} else if ((null != cloneSetA) && (null == cloneSetB)) {

					cloneSetA.add(cloneB);
					cloneSetBag.put(cloneB, cloneSetA);

				} else if ((null == cloneSetA) && (null != cloneSetB)) {

					cloneSetB.add(cloneA);
					cloneSetBag.put(cloneA, cloneSetB);

				} else {

					final CloneSetInfo cloneSet = new CloneSetInfo();
					cloneSet.add(cloneA);
					cloneSet.add(cloneB);

					cloneSetBag.put(cloneA, cloneSet);
					cloneSetBag.put(cloneB, cloneSet);

				}
			}

			final SortedSet<CloneSetInfo> cloneSets = new TreeSet<CloneSetInfo>();
			for (final CloneSetInfo cloneSet : cloneSetBag.values()) {
				if (1 < cloneSet.getNumberOfCodeclones()) {
					cloneSets.add(cloneSet);
				}
			}

			return cloneSets;

		} else if (Configuration.INSTANCE.getG() == OUTPUT_FORMAT.PAIR) {

			final SortedSet<CloneSetInfo> clonesets = new TreeSet<CloneSetInfo>();
			for (final ClonePairInfo clonepair : clonepairs) {
				final CloneSetInfo cloneset = new CloneSetInfo();
				cloneset.add(clonepair.codecloneA);
				cloneset.add(clonepair.codecloneB);
				clonesets.add(cloneset);
			}

			return clonesets;
		}

		else {
			assert false : "Here shouldn't be reached!";
			return null;
		}
	}

	private static void write(final SortedSet<CloneSetInfo> clonesets,
			final IPDGNodeFactory pdgNodeFactory) {

		final XMLWriter writer = new XMLWriter(Configuration.INSTANCE.getO(),
				DataManager.getInstance().getFileInfoManager().getFileInfos(),
				clonesets);

		/*
		 * final BellonWriter writer = new BellonWriter(Configuration.INSTANCE
		 * .getO(), DataManager.getInstance().getFileInfoManager()
		 * .getFileInfos(), cloneSets);
		 */

		writer.write();
		writer.close();
	}

	private static void printComputationalCost() {

		{
			final StringBuilder text = new StringBuilder();
			text.append("the number of base points for pairwise slicing is ");
			text.append(SlicingThread.numberOfPairs);
			out.println(text.toString());
		}

		{
			final StringBuilder text = new StringBuilder();
			text.append("the number of comparison is ");
			text.append(SlicingThread.numberOfComparion);
			out.println(text.toString());
		}
	}

	private static List<NodePairInfo> makeNodePairs(
			final Collection<List<PDGNode<?>>> nodeListSet) {

		final List<NodePairInfo> nodepairs = new ArrayList<NodePairInfo>();

		for (final List<PDGNode<?>> nodeList : nodeListSet) {

			// ���\�b�h�����m�[�h�̏ꍇ�͓ǂݔ�΂�
			if (nodeList.get(0) instanceof PDGMethodEnterNode) {
				continue;
			}

			// 臒l�ȏ��v����m�[�h������ꍇ�͓ǂݔ�΂�
			if (Configuration.INSTANCE.getC() <= nodeList.size()) {
				continue;
			}

			// �R���g���[���m�[�h�ȊO�͖�������I�v�V�������t���Ă���ꍇ
			if (Configuration.INSTANCE.getU().useControlFilter()
					&& !(nodeList.get(0) instanceof PDGControlNode)) {
				continue;
			}

			// �m�[�h�̕������s���Ă���ꍇ�́C�������̓X���C�X��_�Ƃ��ėp���Ȃ�
			if (Configuration.INSTANCE.getF().equals(DISSOLVE.TRUE)
					&& (nodeList.get(0) instanceof PDGControlNode)) {
				continue;
			}

			for (int i = 0; i < nodeList.size(); i++) {

				for (int j = i + 1; j < nodeList.size(); j++) {
					nodepairs.add(new NodePairInfo(nodeList.get(i), nodeList
							.get(j)));
				}
			}
		}

		return nodepairs;
	}
}