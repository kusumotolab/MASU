package jp.ac.osaka_u.ist.sdl.scdetector.io;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sdl.scdetector.data.CloneSetInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;


public class BellonWriter {

    public BellonWriter(final String outputFile, final SortedSet<FileInfo> files,
            final Set<CloneSetInfo> cloneSets) {

        this.files = files;
        this.cloneSets = cloneSets;

        try {
            this.writer = new BufferedWriter(new FileWriter(outputFile));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void write() {

        try {
            for (final CloneSetInfo cloneSet : this.cloneSets) {
                final CodeCloneInfo[] codeclones = cloneSet.getCodeClones().toArray(
                        new CodeCloneInfo[0]);
                for (int i = 0; i < codeclones.length; i++) {
                    final ExecutableElementInfo firstI = codeclones[i].getElements().first();
                    final ExecutableElementInfo lastI = codeclones[i].getElements().last();
                    final String filenameI = ((TargetClassInfo) firstI.getOwnerMethod()
                            .getOwnerClass()).getOwnerFile().getName();
                    for (int j = i + 1; j < codeclones.length; j++) {
                        final ExecutableElementInfo firstJ = codeclones[j].getElements().first();
                        final ExecutableElementInfo lastJ = codeclones[j].getElements().last();
                        final String filenameJ = ((TargetClassInfo) firstJ.getOwnerMethod()
                                .getOwnerClass()).getOwnerFile().getName();

                        if ((!filenameI.equals(filenameJ))
                                || (firstI.getFromLine() != firstJ.getFromLine())
                                || (lastI.getToLine() != lastJ.getToLine())) {
                            this.writer.write(filenameI);
                            this.writer.write("\t");
                            this.writer.write(Integer.toString(firstI.getFromLine()));
                            this.writer.write("\t");
                            this.writer.write(Integer.toString(lastI.getToLine()));
                            this.writer.write("\t");
                            this.writer.write(filenameJ);
                            this.writer.write("\t");
                            this.writer.write(Integer.toString(firstJ.getFromLine()));
                            this.writer.write("\t");
                            this.writer.write(Integer.toString(lastJ.getToLine()));
                            this.writer.write("\t");
                            this.writer.newLine();
                        }
                    }
                }
            }
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

    final Set<CloneSetInfo> cloneSets;

    final SortedSet<FileInfo> files;
}