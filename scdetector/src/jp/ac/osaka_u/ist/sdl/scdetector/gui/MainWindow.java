package jp.ac.osaka_u.ist.sdl.scdetector.gui;


import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import jp.ac.osaka_u.ist.sdl.scdetector.data.ClonePairInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.data.CloneSetInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.data.CodeFragmentInfo;
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

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
