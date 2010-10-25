package jp.ac.osaka_u.ist.sdl.scdetector.gui.sourcecode;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;

import jp.ac.osaka_u.ist.sdl.scdetector.Scorpioui;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.ElementInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.FileController;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.FileInfo;

/**
 * ソースコードを表示するパネル
 * 
 * @author higo
 * 
 */
class SourceCodePanel extends JPanel {

	SourceCodePanel(final FileInfo file) {

		this.file = file;

		this.fileNameField = new JTextField();
		this.fileNameField.setEditable(false);

		this.sourceCodeArea = new JTextArea();
		this.sourceCodeArea.setTabSize(1);
		this.sourceCodeArea.setEditable(false);

		this.sourceCodeScrollPane = new JScrollPane(this.sourceCodeArea);
		this.sourceCodeScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.sourceCodeScrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.sourceCodeScrollPane.setRowHeaderView(new LineNumberView(
				this.sourceCodeArea));

		this.setLayout(new BorderLayout());
		this.add(this.fileNameField, BorderLayout.NORTH);
		this.add(this.sourceCodeScrollPane, BorderLayout.CENTER);

		this.readFile(file);
	}

	/**
	 * ファイルを読み込む
	 * 
	 * @param file
	 *            　読み込むファイル
	 */
	void readFile(final FileInfo file) {

		this.fileNameField.setText(file.getName());

		this.sourceCodeArea.replaceSelection("");

		try {

			final InputStreamReader reader = new InputStreamReader(
					new FileInputStream(file.getName()), "JISAutoDetect");
			final StringBuilder sb = new StringBuilder();
			for (int c = reader.read(); c != -1; c = reader.read()) {
				sb.append((char) c);
			}
			reader.close();
			this.sourceCodeArea.append(sb.toString());

		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

	}

	/**
	 * コードクローン部分を強調表示する
	 * 
	 * @param codeclone
	 *            　強調表示するコードクローン
	 */
	void addHighlight(final CodeCloneInfo codeclone) {

		final DefaultHighlightPainter highlightPainter = new DefaultHighlightPainter(
				new Color(180, 180, 180, 125));
		for (final ElementInfo element : codeclone.getElements()) {

			final int fileID = element.getFileID();
			final FileInfo file = FileController.getInstance(Scorpioui.ID)
					.getFile(fileID);
			if (this.file == file) {

				try {
					final int fromLine = element.getFromLine();
					final int fromColumn = element.getFromColumn();
					final int toLine = element.getToLine();
					final int toColumn = element.getToColumn();

					final int fromOffset = this.sourceCodeArea
							.getLineStartOffset(fromLine - 1)
							+ (fromColumn - 1);
					final int toOffset = this.sourceCodeArea
							.getLineStartOffset(toLine - 1)
							+ (toColumn - 1);

					this.sourceCodeArea.getHighlighter().addHighlight(
							fromOffset, toOffset, highlightPainter);

				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * コードクローンを表示する（スクロールする）
	 * 
	 * @param codeclone
	 *            　表示するコードクローン
	 */
	void display(final CodeCloneInfo codeclone) {

		final Document doc = this.sourceCodeArea.getDocument();
		final Element root = doc.getDefaultRootElement();
		ElementInfo element = null;
		for (final ElementInfo e : codeclone.getElements()) {
			final int fileID = e.getFileID();
			final FileInfo file = FileController.getInstance(Scorpioui.ID)
					.getFile(fileID);
			if (file == this.file) {
				element = e;
				break;
			}
		}

		if (null == element) {
			return;
		}

		// 下の modelToViewの返り値をnullにしないために強制的に正の値を設定
		this.sourceCodeArea.setBounds(new Rectangle(10, 10));

		try {
			Element elem = root.getElement(Math.max(1,
					element.getFromLine() - 2));
			if (null != elem) {
				Rectangle rect = this.sourceCodeArea.modelToView(elem
						.getStartOffset());
				Rectangle vr = this.sourceCodeScrollPane.getViewport()
						.getViewRect();
				if ((null != rect) && (null != vr)) {
					rect.setSize(10, vr.height);
					this.sourceCodeArea.scrollRectToVisible(rect);
				}
				this.sourceCodeArea.setCaretPosition(elem.getStartOffset());
			}

			// textArea.requestFocus();
		} catch (BadLocationException e) {
			System.err.println(e.getMessage());
		}
	}

	final FileInfo file;

	final private JTextField fileNameField;

	final private JTextArea sourceCodeArea;

	final private JScrollPane sourceCodeScrollPane;
}
