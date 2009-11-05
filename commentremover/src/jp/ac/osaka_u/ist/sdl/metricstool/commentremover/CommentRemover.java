package jp.ac.osaka_u.ist.sdl.metricstool.commentremover;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class CommentRemover {

	private static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	public static void main(String[] args) throws IOException {

		try {

			// 　コマンドライン引数を処理
			final Options options = new Options();

			{
				final Option i = new Option("i", "input", true,
						"input directory");
				i.setArgName("input");
				i.setArgs(1);
				i.setRequired(true);
				options.addOption(i);
			}

			{
				final Option o = new Option("o", "output", true,
						"output directory");
				o.setArgName("output");
				o.setArgs(1);
				o.setRequired(true);
				options.addOption(o);
			}

			{
				final Option l = new Option("l", "language", true, "language");
				l.setArgName("language");
				l.setArgs(1);
				l.setRequired(true);
				options.addOption(l);
			}

			{
				final Option x = new Option("x", "encoding", true, "encoding");
				x.setArgName("encoding");
				x.setArgs(1);
				x.setRequired(false);
				options.addOption(x);
			}

			{
				final Option a = new Option("a", "blankline", false,
						"blank line");
				a.setArgName("blankline");
				a.setRequired(false);
				options.addOption(a);
			}

			{
				final Option b = new Option("b", "blockcomment", false,
						"block comment");
				b.setArgName("blockcomment");
				b.setRequired(false);
				options.addOption(b);
			}

			{
				final Option c = new Option("c", "linecomment", false,
						"line comment");
				c.setArgName("linecomment");
				c.setRequired(false);
				options.addOption(c);
			}

			{
				final Option d = new Option("d", "bracketline", false,
						"bracket line");
				d.setArgName("bracketline");
				d.setRequired(false);
				options.addOption(d);
			}

			{
				final Option e = new Option("e", "indent", false, "indent");
				e.setArgName("indent");
				e.setRequired(false);
				options.addOption(e);
			}

			{
				final Option v = new Option("v", "verbose", false,
						"verbose output");
				v.setArgName("verbose");
				v.setRequired(false);
				options.addOption(v);
			}

			final CommandLineParser parser = new PosixParser();
			final CommandLine cmd = parser.parse(options, args);

			{
				final String language = cmd.getOptionValue("l");
				if (!language.equalsIgnoreCase("java")
						&& !language.equalsIgnoreCase("c")
						&& !language.equalsIgnoreCase("charp")) {
					System.out.print("unavailable language: ");
					System.out.println(language);
					System.exit(0);
				}
			}

			final String inputPath = cmd.getOptionValue("i");
			final String outputPath = cmd.getOptionValue("o");

			int index = 0;
			final Set<File> files = getFiles(new File(inputPath), cmd
					.getOptionValue("l"));
			for (final File file : files) {

				if (cmd.hasOption("v")) {
					System.out.print("processing ... ");
					System.out.print(file.getAbsolutePath());
					System.out.print(" [");
					System.out.print(index++ + 1);
					System.out.print("/");
					System.out.print(files.size());
					System.out.println("]");
				}

				String text = readFile(file, cmd.hasOption("x") ? cmd
						.getOptionValue("x") : null);
				if (!cmd.hasOption("c")) {
					text = deleteLineComment(text);
				}
				if (!cmd.hasOption("b")) {
					text = deleteBlockComment(text);
				}
				if (!cmd.hasOption("a")) {
					text = deleteBlankLine(text);
				}
				if (!cmd.hasOption("d")) {
					text = deleteBracketLine(text);
				}
				if (!cmd.hasOption("e")) {
					text = deleteIndent(text);
				}

				writeFile(text, file.getAbsolutePath().replace(inputPath,
						outputPath));
			}

		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(0);
		} catch (ParseException e) {
			System.err.println(e.getMessage());
			System.exit(0);
		}
	}

	/**
	 * ファイルのSetを取得
	 */
	public static Set<File> getFiles(final File file, final String language) {

		final Set<File> files = new HashSet<File>();

		// ディレクトリならば，再帰的に処理
		if (file.isDirectory()) {
			File[] subfiles = file.listFiles();
			for (int i = 0; i < subfiles.length; i++) {
				files.addAll(getFiles(subfiles[i], language));
			}

			// ファイルならば，拡張子が対象言語と一致すれば登録
		} else if (file.isFile()) {

			final String path = file.getAbsolutePath();
			if (language.equals("java")) {
				if (path.endsWith(".java")) {
					files.add(file);
				}
			} else if (language.equals("csharp")) {
				if (path.endsWith(".cs")) {
					files.add(file);
				}
			} else if (language.equals("c")) {
				if (path.endsWith(".c") || path.endsWith("cpp")
						|| path.endsWith("cxx") || path.endsWith(".h")
						|| path.endsWith(".hpp") || path.endsWith(".hxx")) {
					files.add(file);
				}
			}

			// ディレクトリでもファイルでもない場合は不正
		} else {
			System.err.println("\"" + file.getAbsolutePath()
					+ "\" is not a vaild file!");
			System.exit(0);
		}

		return files;
	}

	/**
	 * ファイルを読み込む
	 */
	public static String readFile(final File file, final String encoding) {

		try {

			final StringBuilder text = new StringBuilder();
			final InputStreamReader reader = new InputStreamReader(
					new FileInputStream(file), null != encoding ? encoding
							: "JISAutoDetect");
			while (reader.ready()) {
				final int c = reader.read();
				text.append((char) c);
			}

			/*
			 * final BufferedReader reader = new BufferedReader(new FileReader(
			 * file)); while (reader.ready()) { String line = reader.readLine();
			 * text.append(line); text.append(LINE_SEPARATOR); }
			 */

			return text.toString();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * ラインコメントを削除
	 */
	public static String deleteLineComment(String src) {

		StringBuilder buf = new StringBuilder();

		boolean isLineComment = false;
		boolean isString = false;

		for (int i = 0; i < src.length(); i++) {
			final char ch = src.charAt(i);

			// ラインコメントの中にいるとき
			if (isLineComment) {
				if (ch == LINE_SEPARATOR.charAt(0)) {
					isLineComment = false;
				}
			}

			// String型のリテラルの中にいるとき
			else if (isString) {
				buf.append(ch);
				
				//エスケープシーケンスだったら次の文字も追加				
				if (ch == '\\') {
					buf.append(src.charAt(i++));
				} 
				
				//リテラルを抜ける
				else if (ch == '\"') {
					isString = false;
				}
			}

			// ラインコメント開始
			else if (ch == '/' && src.charAt(i + 1) == '/') {
				isLineComment = true;
			}
			
			// Stringのリテラル開始
			else if(ch == '\"'){
				isString = true;
				buf.append(ch);
			}

			// そのまま処理
			else {
				buf.append(ch);
			}
		}

		return buf.toString();
	}

	/**
	 * ブロックコメントを削除
	 */
	public static String deleteBlockComment(String src) {

		StringBuilder buf = new StringBuilder();

		boolean isBlockComment = false;
		boolean isString = false;
		for (int i = 0; i < src.length(); i++) {
			char ch = src.charAt(i);

			//ブロックコメントの中にいるとき
			if (isBlockComment) {
				buf.append(ch);
				if (ch == '/' && src.charAt(i - 1) == '*') {
					isBlockComment = false;
				}
			} 
			
			// String型のリテラルの中にいるとき
			else if (isString) {
				buf.append(ch);
				
				//エスケープシーケンスだったら次の文字も追記				
				if (ch == '\\') {
					buf.append(src.charAt(i++));
				} 
				
				//リテラルを抜ける
				else if (ch == '\"') {
					isString = false;
				}
			}
			
			//　ブロックコメントに入る
			else if (ch == '/' && src.charAt(i + 1) == '*') {
				buf.append("/*");
				i++;
				isBlockComment = true;
			} 
			
			// そのまま処理
			else {
				buf.append(ch);
			}
		}

		return buf.toString();
	}

	/**
	 * 空白行を削除
	 */
	public static String deleteBlankLine(String src) throws IOException {

		StringBuilder buf = new StringBuilder();
		BufferedReader reader = new BufferedReader(new StringReader(src));

		String inLine;
		while ((inLine = reader.readLine()) != null) {
			if (!inLine.matches("^\\s*$")) {
				buf.append(inLine);
				buf.append(LINE_SEPARATOR);
			}
		}

		return buf.toString();
	}

	/**
	 * 中括弧のみの行を削除
	 */
	public static String deleteBracketLine(String src) {

		final String OPEN_BRACKET_LINE = LINE_SEPARATOR + "[ \t]*[{][ \t]*"
				+ LINE_SEPARATOR;
		final String CLOSE_BRACKET_LINE = LINE_SEPARATOR + "[ \t]*[}][ \t]*"
				+ LINE_SEPARATOR;

		String text1 = src;
		while (true) {
			int beforeLength = text1.length();
			text1 = text1.replaceAll(OPEN_BRACKET_LINE, "{" + LINE_SEPARATOR);
			int afterLength = text1.length();
			if (beforeLength == afterLength) {
				break;
			}
		}

		String text2 = text1;
		while (true) {
			int beforeLength = text2.length();
			text2 = text2.replaceAll(CLOSE_BRACKET_LINE, "}" + LINE_SEPARATOR);
			int afterLength = text2.length();
			if (beforeLength == afterLength) {
				break;
			}
		}

		return text2;
	}

	/**
	 * インデントを削除
	 */
	public static String deleteIndent(String src) {
		return src.replaceAll(LINE_SEPARATOR + "[ \t]+", LINE_SEPARATOR);
	}

	/**
	 * ファイルに出力
	 */
	public static void writeFile(final String text, final String path) {

		try {

			final File file = new File(path);
			file.getParentFile().mkdirs();

			final OutputStream out = new FileOutputStream(path);
			for (int i = 0; i < text.length(); i++) {
				out.write(text.charAt(i));
			}
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
