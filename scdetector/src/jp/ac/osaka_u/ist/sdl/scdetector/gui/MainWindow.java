package jp.ac.osaka_u.ist.sdl.scdetector.gui;


import java.awt.BorderLayout;
import java.util.Set;

import javax.swing.JFrame;

import jp.ac.osaka_u.ist.sdl.scdetector.ClonePairInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.clonepair.ClonePairListView;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.sourcecode.SourceCodeView;


public class MainWindow extends JFrame {

    public MainWindow(final Set<ClonePairInfo> clonePairs) {

        final ClonePairListView clonePairListView = new ClonePairListView(clonePairs);
        final SourceCodeView sourceCodeView = new SourceCodeView();

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(clonePairListView.scrollPane, BorderLayout.WEST);
        this.getContentPane().add(sourceCodeView, BorderLayout.CENTER);
    }
}
