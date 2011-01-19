package jp.ac.osaka_u.ist.sdl.scorpio.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import jp.ac.osaka_u.ist.sdl.scorpio.Scorpioui;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.cloneset.CloneSetListView;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.codeclone.CodeCloneListView;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CloneSetInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CodeCloneController;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.element.ElementListView;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.sourcecode.SourceCodeView;

/**
 * GUIのメインウィンドウ
 * 
 * @author higo
 * 
 */
public class MainWindow extends JFrame {

	public MainWindow() {

		final CloneSetListView clonesetListView = new CloneSetListView(
				CodeCloneController.getInstance(Scorpioui.ID).getCloneSets());
		SelectedEntities.<CloneSetInfo> getInstance(CloneSetInfo.CLONESET)
				.addObserver(clonesetListView);
		SelectedEntities.<CodeCloneInfo> getInstance(CodeCloneInfo.CODECLONE)
				.addObserver(clonesetListView);
		clonesetListView.scrollPane.setBorder(new TitledBorder(new LineBorder(
				java.awt.Color.black), "Clone Set List"));

		final CodeCloneListView codecloneListView = new CodeCloneListView();
		SelectedEntities.<CloneSetInfo> getInstance(CloneSetInfo.CLONESET)
				.addObserver(codecloneListView);
		SelectedEntities.<CodeCloneInfo> getInstance(CodeCloneInfo.CODECLONE)
				.addObserver(codecloneListView);
		codecloneListView.scrollPane.setBorder(new TitledBorder(new LineBorder(
				java.awt.Color.black), "Code Clone List"));

		final ElementListView elementListView = new ElementListView();
		SelectedEntities.<CloneSetInfo> getInstance(CloneSetInfo.CLONESET)
				.addObserver(elementListView);
		SelectedEntities.<CodeCloneInfo> getInstance(CodeCloneInfo.CODECLONE)
				.addObserver(elementListView);
		elementListView.scrollPane.setBorder(new TitledBorder(new LineBorder(
				java.awt.Color.black), "Element List"));

		final SourceCodeView sourceCodeView = new SourceCodeView();
		SelectedEntities.<CloneSetInfo> getInstance(CloneSetInfo.CLONESET)
				.addObserver(sourceCodeView);
		SelectedEntities.<CodeCloneInfo> getInstance(CodeCloneInfo.CODECLONE)
				.addObserver(sourceCodeView);

		final JSplitPane westPanel1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		westPanel1.setTopComponent(codecloneListView.scrollPane);
		westPanel1.setBottomComponent(elementListView.scrollPane);

		final JSplitPane westPanel2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		westPanel2.setTopComponent(clonesetListView.scrollPane);
		westPanel2.setBottomComponent(westPanel1);

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(westPanel2, BorderLayout.WEST);
		this.getContentPane().add(sourceCodeView, BorderLayout.CENTER);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
}
