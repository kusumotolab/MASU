package jp.ac.osaka_u.ist.sdl.scdetector;


import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.CloneSetInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.CodeCloneController;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.ElementInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.io.XMLReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;


/**
 * 二つの出力結果の違いを計算するためのクラス
 * 
 * @author higo
 *
 */
public class SCComparator {

    public static void main(String[] args) {

        try {

            //　コマンドライン引数を処理
            final Options options = new Options();

            final Option i1 = new Option("i1", "input", true, "input file1");
            i1.setArgName("file1");
            i1.setArgs(1);
            i1.setRequired(true);
            options.addOption(i1);

            final Option i2 = new Option("i2", "input", true, "input file2");
            i2.setArgName("file1");
            i2.setArgs(1);
            i2.setRequired(true);
            options.addOption(i2);

            final CommandLineParser parser = new PosixParser();
            final CommandLine cmd = parser.parse(options, args);

            XMLReader.read(cmd.getOptionValue("i1"), WITHOUT_FILTERING);
            XMLReader.read(cmd.getOptionValue("i2"), WITH_FILTERING);
            //BellonReader.read(cmd.getOptionValue("i1"), WITHOUT_FILTERING);
            //BellonReader.read(cmd.getOptionValue("i2"), WITH_FILTERING);

            System.out.print("The number of clone sets1: ");
            System.out.println(CodeCloneController.getInstance(WITHOUT_FILTERING)
                    .getNumberOfClonesets());

            System.out.print("The number of clone sets2: ");
            System.out.println(CodeCloneController.getInstance(WITH_FILTERING)
                    .getNumberOfClonesets());

            final SortedSet<ElementInfo> elements1 = new TreeSet<ElementInfo>();
            final SortedSet<ElementInfo> elements2 = new TreeSet<ElementInfo>();

            for (final CloneSetInfo cloneSet : CodeCloneController.getInstance(WITHOUT_FILTERING)
                    .getCloneSets()) {
                for (final CodeCloneInfo codeclone : cloneSet.getCodeclones()) {
                    elements1.addAll(codeclone.getElements());
                }
            }
            for (final CloneSetInfo cloneSet : CodeCloneController.getInstance(WITH_FILTERING)
                    .getCloneSets()) {
                for (final CodeCloneInfo codeFragment : cloneSet.getCodeclones()) {
                    elements2.addAll(codeFragment.getElements());
                }
            }
            System.out.println("The number of elements1: " + elements1.size());
            System.out.println("The number of elements2: " + elements2.size());
            final float beforeNumber = elements1.size();
            elements1.retainAll(elements2);
            final float afterNumber = elements1.size();
            System.out.println(beforeNumber + " : " + afterNumber);
            System.out.println("coverage rate: " + 100 * afterNumber / beforeNumber);

        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private static final String WITHOUT_FILTERING = "WITHOUT_FILTERING";

    private static final String WITH_FILTERING = "WITH_FILTERING";
}
