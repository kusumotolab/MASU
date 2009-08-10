package jp.ac.osaka_u.ist.sdl.scdetector;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.CloneSetInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.CodeCloneController;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.ElementInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.FileController;
import jp.ac.osaka_u.ist.sdl.scdetector.io.XMLReader;
import jp.ac.osaka_u.ist.sel.icca.iccalib.data.clone.CloneManager;
import jp.ac.osaka_u.ist.sel.icca.iccalib.data.file.FileManager;
import jp.ac.osaka_u.ist.sel.icca.iccalib.parse.CCFinderFileParser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;


public class CCFinderComparator {

    public static void main(String[] args) {
        new CCFinderComparator(args);
    }

    public CCFinderComparator(String[] args) {

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

            CCFinderFileParser ccfinderParser = new CCFinderFileParser(cmd.getOptionValue("i1"));
            ccfinderParser.parse();
            XMLReader.read(cmd.getOptionValue("i2"), SCORPIO);

            System.out.print("The number of clone sets1: ");
            System.out.println(CloneManager.SINGLETON.getCloneSetCount());

            System.out.print("The number of clone sets2: ");
            System.out.println(CodeCloneController.getInstance(SCORPIO).getNumberOfClonesets());

            Set<Line> elements1 = new HashSet<Line>();
            Set<Line> elements2 = new HashSet<Line>();

            for (final jp.ac.osaka_u.ist.sel.icca.iccalib.data.clone.CloneSetInfo cloneSet : CloneManager.SINGLETON
                    .getCloneSets()) {

                /*
                //オーバーラップするクローンは削除
                final Set<jp.ac.osaka_u.ist.sel.icca.iccalib.data.clone.CodeFragmentInfo> refinedCloneSet = new HashSet<jp.ac.osaka_u.ist.sel.icca.iccalib.data.clone.CodeFragmentInfo>();
                CodeFragmentInfo[] array = cloneSet.getCodeFragments().toArray(
                        new CodeFragmentInfo[0]);
                I: for (int i = 0; i < array.length; i++) {
                    J: for (int j = 0; j < array.length; j++) {
                        if(array[i] == array[j]){
                            continue J;
                        }
                        if (array[i].isOverlap(array[j])) {
                            continue I;
                        }
                    }
                    refinedCloneSet.add(array[i]);
                }*/

                for (final jp.ac.osaka_u.ist.sel.icca.iccalib.data.clone.CodeFragmentInfo codeclone : cloneSet
                        .getCodeFragments()) {
                    /*for (final jp.ac.osaka_u.ist.sel.icca.iccalib.data.clone.CodeFragmentInfo codeclone : refinedCloneSet) {*/
                    final String filename = FileManager.SINGLETON.getFile(codeclone).getPath();
                    for (int line = codeclone.getFromLine(); line <= codeclone.getToLine(); line++) {
                        final Line lineObject = new Line(filename, line);
                        elements1.add(lineObject);
                    }
                }
            }

            for (final CloneSetInfo cloneSet : CodeCloneController.getInstance(SCORPIO)
                    .getCloneSets()) {
                for (final CodeCloneInfo codeFragment : cloneSet.getCodeclones()) {
                    for (final ElementInfo element : codeFragment.getElements()) {
                        final String filename = FileController.getInstance(SCORPIO).getFile(
                                element.getFileID()).getName();
                        for (int line = element.getFromLine(); line <= element.getToLine(); line++) {
                            final Line lineObject = new Line(filename, line);
                            elements2.add(lineObject);
                        }
                    }
                }
            }

            /*
            Set<Line> elements3 = elements1;
            elements1 = elements2;
            elements2 = elements3;
            */
            
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

    class Line {

        Line(final String filename, final int line) {
            this.filename = filename;
            this.line = line;
        }

        @Override
        public boolean equals(Object o) {

            if (!(o instanceof Line)) {
                return false;
            }

            final Line target = (Line) o;
            return this.getFilename().equals(target.getFilename())
                    && (this.getLine() == target.getLine());
        }

        @Override
        public int hashCode() {
            return this.getFilename().hashCode() + this.getLine();
        }

        public int getLine() {
            return this.line;
        }

        public String getFilename() {
            return this.filename;
        }

        private final String filename;

        private final int line;
    }

    private static final String SCORPIO = "SCORPIO";
}
