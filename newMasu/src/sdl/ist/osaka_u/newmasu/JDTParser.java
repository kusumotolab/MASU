package sdl.ist.osaka_u.newmasu;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.MetricsTool;
import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import sdl.ist.osaka_u.newmasu.test.DrawPDG;
import sdl.ist.osaka_u.newmasu.util.ListFiles;

public class JDTParser extends MetricsTool {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// コマンドライン引数を処理
		final Options options = new Options();
		{
			options.addOption(OptionBuilder.withArgName("directory").hasArg()
					.isRequired().withDescription("target directory")
					.create("d"));

			options.addOption(OptionBuilder
					.withArgName("language")
					.hasArg()
					.isRequired()
					.withDescription(
							"programming language of analyzed source code")
					.create("l"));

			options.addOption(OptionBuilder
					.withArgName("MethodMetricsFile")
					.hasArg()
					.withDescription(
							"specify file that measured METHOD metrics were stored into")
					.create("M"));

			options.addOption(OptionBuilder.withArgName("Library directory")
					.hasArgs()
					.withDescription("specify directory that stored libraries")
					.create("L"));
		}

		CommandLine cmd = null;

		try {
			final CommandLineParser parser = new BasicParser();
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			HelpFormatter f = new HelpFormatter();
			f.printHelp("OptionsTip", options);

			e.printStackTrace();
			return;
		}

		// 解析用設定
		if (cmd.hasOption("M")) {
			Settings.getInstance()
					.setMethodMetricsFile(cmd.getOptionValue("M"));
		}

		Settings.getInstance().setLanguage(cmd.getOptionValue("l"));
		Settings.getInstance().addTargetDirectory(cmd.getOptionValue("d"));
		Settings.getInstance().setVerbose(true);

		// 対象ディレクトリ以下のJavaファイルを登録し，解析
		{
			final JDTParser viewer = new JDTParser();

			viewer.loadPlugins(Settings.getInstance().getMetrics());

			// viewer.analyzeLibraries();
			viewer.addLibraries(cmd);

			// viewer.readTargetFiles();
			viewer.addTargetFiles();

			viewer.analyzeTargetFiles();
			viewer.launchPlugins();
			viewer.writeMetrics();
		}
		
		// for test
		final DrawPDG testPDG = new DrawPDG(); 

		System.out.println("successfully finished.");
	}

	/**
	 * 対象ファイルのASTから未解決クラス，フィールド，メソッド情報を取得
	 */
	@Override
	public void parseTargetFiles() {
		final JDTTargetFileParser parser = new JDTTargetFileParser();

		// final TargetFile[] files = DataManager.getInstance()
		// .getTargetFileManager().getFiles().toArray(new TargetFile[0]);
	}

	/**
	 * Settingsの中にライブラリファイルを登録する． デフォルトのディレクトリは./resource
	 */
	public void addLibraries(final CommandLine cmd) {
		String[] libPaths = new String[1];
		libPaths[0] = "./resource";

		if (cmd.hasOption("L"))
			libPaths = cmd.getOptionValues("L");

		for (String arg : libPaths) {
			Path path = Paths.get(arg);
			if (Files.exists(path)) {
				ArrayList<String> jarList = ListFiles.list("jar", path);

				for (String p : jarList) {
					Settings.getInstance().addLibrary(p);
				}
			}
		}
	}

	/**
	 * 対象ディレクトリから対象ファイルを列挙
	 */
	private void addTargetFiles() {
		final Set<String> targetDir = Settings.getInstance()
				.getTargetDirectories();
		for (String dir : targetDir) {
			final ArrayList<String> files = ListFiles.list("java",
					Paths.get(dir));
			for (String file : files)
				Settings.getInstance().addListFile(file);
		}
		
	}

}
