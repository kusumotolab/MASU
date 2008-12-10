package jp.ac.osaka_u.ist.sdl.scdetector.gui.sourcecode;


import javax.swing.JTextArea;


class SourceCodePanel extends JTextArea {

    SourceCodePanel() {

    }
    
    private void displayCodeFragment(final CodeFragmentInfo codeFragment) {

        final Document doc = this.getDocument();
        final Element root = doc.getDefaultRootElement();
        try {
            Element elem = root.getElement(Math.max(1, codeFragment.getFromLine() - 2));
            if (null != elem) {
                Rectangle rect = this.modelToView(elem.getStartOffset());
                Rectangle vr = this.getScrollPane().getViewport().getViewRect();
                if ((null != rect) && (null != vr)) {
                    rect.setSize(10, vr.height);
                    this.scrollRectToVisible(rect);
                }
                this.setCaretPosition(elem.getStartOffset());
            }

            // textArea.requestFocus();
        } catch (BadLocationException e) {
            System.err.println(e.getMessage());
        }
    }
}
