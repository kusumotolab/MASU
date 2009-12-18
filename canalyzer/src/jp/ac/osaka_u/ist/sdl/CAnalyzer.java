package jp.ac.osaka_u.ist.sdl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class CAnalyzer {

	public static void main(String[] args) {

		try {

			// コマンドライン引数を処理
			final Options options = new Options();

			{
				final Option d = new Option("d", "directory", true,
						"target directory");
				d.setArgName("directory");
				d.setArgs(1);
				d.setRequired(true);
				options.addOption(d);
			}

			final CommandLineParser parser = new PosixParser();
			final CommandLine cmd = parser.parse(options, args);

			Settings.getInstance().setTargetDirectory(cmd.getOptionValue("d"));

		} catch (ParseException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}

		final CAnalyzer analyzer = new CAnalyzer();
		final SortedSet<File> files = analyzer.getTargetFiles(Settings
				.getInstance().getTargetDirectory());
		final SortedMap<File, String> textFileMap = new TreeMap<File, String>();
		for(File file : files){
			final String text = analyzer.getProcessedText(file);
			textFileMap.put(file, text);
		}

	}

	private SortedSet<File> getTargetFiles(final String directory) {

		final SortedSet<File> files = new TreeSet<File>();

		final File dir = new File(directory);
		final File[] children = dir.listFiles();
		for (int i = 0; i < children.length; i++) {

			// ディレクトリなら再帰
			if (children[i].isDirectory()) {
				files
						.addAll(this.getTargetFiles(children[i]
								.getAbsolutePath()));
			}

			// ファイルなら拡張子を調べる
			else if (children[i].isFile()) {
				final String path = children[i].getName();
				if (path.endsWith(".c")) {
					files.add(children[i]);
				}
			}
		}

		return Collections.unmodifiableSortedSet(files);
	}

	private String getProcessedText(final File file) {

		final StringBuilder unprocessedText = new StringBuilder();
		final Map<String, String> replaceMap = new HashMap<String, String>();

		try {

			for (final BufferedReader reader = new BufferedReader(
					new FileReader(file)); reader.ready();) {
				final String line = reader.readLine();
				if (!line
						.matches("[ |\\t]*#define[ |\\t]?[\\S]?[ |\\t]?[\\S]?[ |\\t]*")) {
					unprocessedText.append(line);
					unprocessedText
							.append(System.getProperty("line.separator"));
				}

				else {

					final StringTokenizer tokenizer = new StringTokenizer(line,
							" \t");
					if (3 != tokenizer.countTokens()) {
						throw new IllegalStateException();
					}

					final String define = tokenizer.nextToken();
					if (!define.equals("#define")) {
						throw new IllegalStateException();
					}

					final String replaced = tokenizer.nextToken();
					final String replacing = tokenizer.nextToken();
					replaceMap.put(replaced, replacing);
				}
			}

		} catch (IOException e) {

		}

		// #define の文字列を置換
		final String processedText = unprocessedText.toString();
		for (final Entry<String, String> entry : replaceMap.entrySet()) {
			processedText.replaceAll(entry.getKey(), entry.getValue());
		}

		return processedText;
	}
}
