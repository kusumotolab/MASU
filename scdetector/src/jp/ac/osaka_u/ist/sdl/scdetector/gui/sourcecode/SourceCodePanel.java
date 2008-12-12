package jp.ac.osaka_u.ist.sdl.scdetector.gui.sourcecode;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.SortedSet;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;


class SourceCodePanel extends JPanel {

    SourceCodePanel() {

        this.fileNameField = new JTextField();
        this.fileNameField.setEditable(false);

        this.sourceCodeArea = new JTextArea();
        this.sourceCodeArea.setTabSize(1);

        this.sourceCodeScrollPane = new JScrollPane(this.sourceCodeArea);
        this.sourceCodeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.sourceCodeScrollPane
                .setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        this.setLayout(new BorderLayout());
        this.add(this.fileNameField, BorderLayout.NORTH);
        this.add(this.sourceCodeScrollPane, BorderLayout.CENTER);
    }

    SourceCodePanel(final FileInfo file) {

        this();

        this.readFile(file);
    }

    void readFile(final FileInfo file) {

        this.fileNameField.setText(file.getName());

        this.sourceCodeArea.replaceSelection("");

        try {

            final InputStreamReader reader = new InputStreamReader(new FileInputStream(file
                    .getName()), "JISAutoDetect");
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

    void addHighlight(final SortedSet<ExecutableElement> clone) {

        final DefaultHighlightPainter highlightPainter = new DefaultHighlightPainter(new Color(180,
                180, 180, 125));

        for (final ExecutableElement element : clone) {

            try {

                final int fromLine = element.getFromLine();
                final int fromColumn = element.getFromColumn();
                final int toLine = element.getToLine();
                final int toColumn = element.getToColumn();

                final int fromOffset;
                if (0 < fromLine) {
                    fromOffset = this.sourceCodeArea.getLineStartOffset(fromLine - 1)
                            + (fromColumn - 1);
                } else {
                    fromOffset = (fromColumn - 1);
                }

                final int toOffset;
                if (0 < toLine) {
                    toOffset = this.sourceCodeArea.getLineStartOffset(toLine - 1) + (toColumn - 1);
                } else {
                    toOffset = (toColumn - 1);
                }

                this.sourceCodeArea.getHighlighter().addHighlight(fromOffset, toOffset,
                        highlightPainter);

            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    void display(final SortedSet<ExecutableElement> clone) {

        final Document doc = this.sourceCodeArea.getDocument();
        final Element root = doc.getDefaultRootElement();

        final ExecutableElement firstElement = clone.first();

        // â∫ÇÃ modelToViewÇÃï‘ÇËílÇnullÇ…ÇµÇ»Ç¢ÇΩÇﬂÇ…ã≠êßìIÇ…ê≥ÇÃílÇê›íË
        this.sourceCodeArea.setBounds(new Rectangle(10, 10));

        try {
            Element elem = root.getElement(Math.max(1, firstElement.getFromLine() - 2));
            if (null != elem) {
                Rectangle rect = this.sourceCodeArea.modelToView(elem.getStartOffset());
                Rectangle vr = this.sourceCodeScrollPane.getViewport().getViewRect();
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

    final private JTextField fileNameField;

    final private JTextArea sourceCodeArea;

    final private JScrollPane sourceCodeScrollPane;
}
