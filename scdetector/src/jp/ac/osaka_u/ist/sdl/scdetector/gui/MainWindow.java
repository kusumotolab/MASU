package jp.ac.osaka_u.ist.sdl.scdetector.gui;


import java.awt.BorderLayout;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import jp.ac.osaka_u.ist.sdl.scdetector.ClonePairInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.CloneSetInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.CodeFragmentInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.clonepair.CloneSetListView;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.codefragment.CodeFragmentListView;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.sourcecode.SourceCodeView;


public class MainWindow extends JFrame {

    public MainWindow(final Set<CloneSetInfo> cloneSets) {

        final CloneSetListView cloneSetListView = new CloneSetListView(cloneSets);
        SelectedEntities.<CloneSetInfo> getInstance(CloneSetInfo.CLONESET).addObserver(
                cloneSetListView);
        SelectedEntities.<CodeFragmentInfo> getInstance(CodeFragmentInfo.CODEFRAGMENT).addObserver(
                cloneSetListView);

        final CodeFragmentListView codeFragmentListView = new CodeFragmentListView();
        SelectedEntities.<CloneSetInfo> getInstance(CloneSetInfo.CLONESET).addObserver(
                codeFragmentListView);
        SelectedEntities.<ClonePairInfo> getInstance(CodeFragmentInfo.CODEFRAGMENT).addObserver(
                codeFragmentListView);

        final SourceCodeView sourceCodeView = new SourceCodeView();
        SelectedEntities.<ClonePairInfo> getInstance(CloneSetInfo.CLONESET).addObserver(
                sourceCodeView);
        SelectedEntities.<ClonePairInfo> getInstance(CodeFragmentInfo.CODEFRAGMENT).addObserver(
                sourceCodeView);

        final JSplitPane westPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        westPanel.setTopComponent(cloneSetListView.scrollPane);
        westPanel.setBottomComponent(codeFragmentListView.scrollPane);

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(westPanel, BorderLayout.WEST);
        this.getContentPane().add(sourceCodeView, BorderLayout.CENTER);
    }
}
