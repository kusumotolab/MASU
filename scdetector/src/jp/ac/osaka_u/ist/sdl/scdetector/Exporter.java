package jp.ac.osaka_u.ist.sdl.scdetector;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.CloneSetInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.CodeCloneController;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.io.XMLReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class Exporter {

	public static final String ID = "EXPORTER";

	public static void main(String[] args) {

		try {

			// コマンドライン引数を処理
			final Options options = new Options();

			final Option i = new Option("i", "input", true, "input file");
			i.setArgName("input");
			i.setArgs(1);
			i.setRequired(true);
			options.addOption(i);

			final Option o = new Option("o", "output", true, "output file");
			o.setArgName("output");
			o.setArgs(1);
			o.setRequired(true);
			options.addOption(o);

			final CommandLineParser parser = new PosixParser();
			final CommandLine cmd = parser.parse(options, args);

			XMLReader.read(cmd.getOptionValue("i"), Exporter.ID);

			export(cmd.getOptionValue("o"));

		} catch (ParseException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	private static void export(final String output) {

		try {

			final BufferedWriter writer = new BufferedWriter(new FileWriter(
					output));

			for (final CloneSetInfo cloneset : CodeCloneController.getInstance(
					Exporter.ID).getCloneSets()) {
				for (final CodeCloneInfo codeclone : cloneset.getCodeclones()) {
					final int size = codeclone.getLength();
					final int method = codeclone.getNumberOfMethods();
					writer.write(Integer.toString(size));
					writer.write(",");
					writer.write(Integer.toString(method));
					writer.newLine();
				}
			}

			writer.close();

		} catch (IOException e) {

		}
	}
}
