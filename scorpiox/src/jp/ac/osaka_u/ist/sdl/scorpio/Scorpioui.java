package jp.ac.osaka_u.ist.sdl.scorpio;


import java.awt.Dimension;
import java.awt.Toolkit;

import jp.ac.osaka_u.ist.sdl.scorpio.gui.MainWindow;
import jp.ac.osaka_u.ist.sdl.scorpio.io.XMLReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;


public class Scorpioui {

    public static void main(String[] args) {

        try {

            //　コマンドライン引数を処理
            final Options options = new Options();

            final Option i = new Option("i", "input", true, "input file");
            i.setArgName("language");
            i.setArgs(1);
            i.setRequired(true);
            options.addOption(i);

            final CommandLineParser parser = new PosixParser();
            final CommandLine cmd = parser.parse(options, args);

            XMLReader.read(cmd.getOptionValue("i"), Scorpioui.ID);
            final MainWindow mainWindow = new MainWindow();
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            mainWindow.setSize(new Dimension(d.width - 5, d.height - 27));
            mainWindow.setVisible(true);

        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static final String ID = "SCVISUALIZER";
}
