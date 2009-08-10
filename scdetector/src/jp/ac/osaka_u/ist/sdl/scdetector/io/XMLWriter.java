package jp.ac.osaka_u.ist.sdl.scdetector.io;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sdl.scdetector.Scorpio;
import jp.ac.osaka_u.ist.sdl.scdetector.data.CloneSetInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.PDGController;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.Configuration;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.DEPENDENCY_TYPE;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.SLICE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.IPDGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.PDG;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;


public class XMLWriter {

    public XMLWriter(final String outputFile, final SortedSet<FileInfo> files,
            final SortedSet<CloneSetInfo> cloneSets, IPDGNodeFactory pdgNodeFactory) {

        this.files = files;
        this.cloneSets = cloneSets;
        this.pdgNodeFactory = pdgNodeFactory;

        try {
            this.writer = new BufferedWriter(new FileWriter(outputFile));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void write() {

        // FileInfoとファイル番号のマップを作成
        final Map<FileInfo, Integer> fileToIntegerMap = new HashMap<FileInfo, Integer>();
        for (final FileInfo file : this.files) {
            final int value = fileToIntegerMap.size();
            fileToIntegerMap.put(file, value);
        }

        // 各ファイルの重複ノード情報を作成
        final Map<FileInfo, Set<PDGNode<?>>> fileToNodeMap = new HashMap<FileInfo, Set<PDGNode<?>>>();
        {
            for (final FileInfo file : this.files) {
                fileToNodeMap.put(file, new HashSet<PDGNode<?>>());
            }
            for (final CloneSetInfo cloneSet : this.cloneSets) {
                for (final CodeCloneInfo codeclone : cloneSet.getCodeClones()) {
                    for (final ExecutableElementInfo element : codeclone.getElements()) {
                        final FileInfo ownerFile = ((TargetClassInfo) element.getOwnerMethod()
                                .getOwnerClass()).getOwnerFile();
                        final Set<PDGNode<?>> nodes = fileToNodeMap.get(ownerFile);
                        assert null != nodes : "Illegal State!";
                        final PDGNode<?> node = this.pdgNodeFactory.getNode(element);
                        nodes.add(node);
                    }
                }
            }
        }

        // XMLのヘッダを出力
        try {
            this.writer.write("<?xml version=\"1.0\"?>");
            this.writer.newLine();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        // 開始タグを出力
        try {
            this.writer.write("<RESULT c=\"");
            this.writer.write(String.valueOf(Configuration.INSTANCE.getC()));
            this.writer.write("\" d=\"");
            this.writer.write(Configuration.INSTANCE.getD());
            this.writer.write("\" l=\"");
            this.writer.write(Configuration.INSTANCE.getL());
            this.writer.write("\" o=\"");
            this.writer.write(Configuration.INSTANCE.getO());
            this.writer.write("\" p=\"");
            this.writer.write(Configuration.INSTANCE.getP().getText());
            for (final DEPENDENCY_TYPE dependency : Configuration.INSTANCE.getQ()) {
                this.writer.write(dependency.getText());
                this.writer.write(",");
            }
            this.writer.write("\" s=\"");
            this.writer.write(String.valueOf(Configuration.INSTANCE.getS()));
            this.writer.write("\" t=\"");
            for (final SLICE_TYPE slice : Configuration.INSTANCE.getT()) {
                this.writer.write(slice.getText());
                this.writer.write(",");
            }
            this.writer.write("\" u=\"");
            this.writer.write(Configuration.INSTANCE.getU().getText());
            this.writer.write("\" pc=\"");
            this.writer.write(String.valueOf(Configuration.INSTANCE.getPC().getText()));
            this.writer.write("\" pi=\"");
            this.writer.write(String.valueOf(Configuration.INSTANCE.getPI().getText()));
            this.writer.write("\" pl=\"");
            this.writer.write(String.valueOf(Configuration.INSTANCE.getPL().getText()));
            this.writer.write("\" po=\"");
            this.writer.write(String.valueOf(Configuration.INSTANCE.getPO().getText()));
            this.writer.write("\" pr=\"");
            this.writer.write(String.valueOf(Configuration.INSTANCE.getPR().getText()));
            this.writer.write("\" pv=\"");
            this.writer.write(String.valueOf(Configuration.INSTANCE.getPV().getText()));
            this.writer.write("\">");
            this.writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // ファイル情報を出力
        try {
            this.writer.write("\t<FILEINFO>");
            this.writer.newLine();

            for (final Map.Entry<FileInfo, Integer> entry : fileToIntegerMap.entrySet()) {

                this.writer.write("\t\t<FILE>");
                this.writer.newLine();

                this.writer.write("\t\t\t<FILEID>" + entry.getValue() + "</FILEID>");
                this.writer.newLine();

                this.writer.write("\t\t\t<FILELOC>" + entry.getKey().getLOC() + "</FILELOC>");
                this.writer.newLine();

                final int number = this.getNumberOfNodes(entry.getKey());
                this.writer.write("\t\t\t<PDGNODE>" + number + "</PDGNODE>");
                this.writer.newLine();

                final Set<PDGNode<?>> duplicatedNodes = fileToNodeMap.get(entry.getKey());
                final float ratio = 100 * duplicatedNodes.size() / (float) number;
                this.writer.write("\t\t\t<DUPLICATEDRADIO>" + ratio + "</DUPLICATEDRADIO>");
                this.writer.newLine();

                this.writer.write("\t\t\t<FILEPATH>" + entry.getKey().getName() + "</FILEPATH>");
                this.writer.newLine();

                this.writer.write("\t\t</FILE>");
                this.writer.newLine();
            }

            this.writer.write("\t</FILEINFO>");
            this.writer.newLine();

        } catch (final IOException e) {
            e.printStackTrace();
        }

        // クローン情報を出力
        try {
            this.writer.write("\t<CLONEINFO>");
            this.writer.newLine();

            for (final CloneSetInfo cloneSet : this.cloneSets) {

                this.writer.write("\t\t<CLONESET>");
                this.writer.newLine();

                for (final CodeCloneInfo codeFragment : cloneSet.getCodeClones()) {

                    this.writer.write("\t\t\t<CLONE>");
                    this.writer.newLine();

                    this.writer.write("\t\t\t\t<GAP>" + codeFragment.getGapsNumber() + "</GAP>");
                    this.writer.newLine();

                    for (final ExecutableElementInfo element : codeFragment.getElements()) {

                        this.writer.write("\t\t\t\t<ELEMENT>");
                        this.writer.newLine();

                        final FileInfo ownerFile = ((TargetClassInfo) element.getOwnerMethod()
                                .getOwnerClass()).getOwnerFile();
                        final int id = fileToIntegerMap.get(ownerFile);

                        this.writer.write("\t\t\t\t\t<OWNERFILEID>" + id + "</OWNERFILEID>");
                        this.writer.newLine();

                        this.writer.write("\t\t\t\t\t<FROMLINE>" + element.getFromLine()
                                + "</FROMLINE>");
                        this.writer.newLine();

                        this.writer.write("\t\t\t\t\t<FROMCOLUMN>" + element.getFromColumn()
                                + "</FROMCOLUMN>");
                        this.writer.newLine();

                        this.writer.write("\t\t\t\t\t<TOLINE>" + element.getToLine() + "</TOLINE>");
                        this.writer.newLine();

                        this.writer.write("\t\t\t\t\t<TOCOLUMN>" + element.getToColumn()
                                + "</TOCOLUMN>");
                        this.writer.newLine();

                        this.writer.write("\t\t\t\t</ELEMENT>");
                        this.writer.newLine();
                    }

                    this.writer.write("\t\t\t</CLONE>");
                    this.writer.newLine();
                }

                this.writer.write("\t\t</CLONESET>");
                this.writer.newLine();
            }

            this.writer.write("\t</CLONEINFO>");
            this.writer.newLine();

        } catch (final IOException e) {
            e.printStackTrace();
        }

        // 終了タグを出力
        try {
            this.writer.write("</RESULT>");
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

    private int getNumberOfNodes(final FileInfo file) {

        final Set<PDG> pdgs = PDGController.getInstance(Scorpio.ID).getPDGs(file.getName());
        int number = 0;
        for (final PDG pdg : pdgs) {
            number += pdg.getNumberOfNodes();
        }

        return number;

    }

    final Set<CloneSetInfo> cloneSets;

    final SortedSet<FileInfo> files;

    final IPDGNodeFactory pdgNodeFactory;
}
