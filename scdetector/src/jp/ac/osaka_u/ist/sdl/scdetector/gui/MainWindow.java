package jp.ac.osaka_u.ist.sdl.scdetector.gui;


import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import jp.ac.osaka_u.ist.sdl.scdetector.Scorpioui;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.cloneset.CloneSetListView;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.codeclone.CodeCloneListView;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.CloneSetInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.CodeCloneController;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.sourcecode.SourceCodeView;


/**
 * GUIのメインウィンドウ
 * 
 * @author higo
 *
 */
public class MainWindow extends JFrame {

    public MainWindow() {

        final CloneSetListView cloneSetListView = new CloneSetListView(CodeCloneController
                .getInstance(Scorpioui.ID).getCloneSets());
        SelectedEntities.<CloneSetInfo> getInstance(CloneSetInfo.CLONESET).addObserver(
                cloneSetListView);
        SelectedEntities.<CodeCloneInfo> getInstance(CodeCloneInfo.CODECLONE).addObserver(
                cloneSetListView);

        final CodeCloneListView codeFragmentListView = new CodeCloneListView();
        SelectedEntities.<CloneSetInfo> getInstance(CloneSetInfo.CLONESET).addObserver(
                codeFragmentListView);
        SelectedEntities.<CodeCloneInfo> getInstance(CodeCloneInfo.CODECLONE).addObserver(
                codeFragmentListView);

        final SourceCodeView sourceCodeView = new SourceCodeView();
        SelectedEntities.<CloneSetInfo> getInstance(CloneSetInfo.CLONESET).addObserver(
                sourceCodeView);
        SelectedEntities.<CodeCloneInfo> getInstance(CodeCloneInfo.CODECLONE).addObserver(
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
