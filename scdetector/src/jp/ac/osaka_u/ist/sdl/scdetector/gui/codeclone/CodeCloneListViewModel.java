package jp.ac.osaka_u.ist.sdl.scdetector.gui.codeclone;


import javax.swing.table.AbstractTableModel;

import jp.ac.osaka_u.ist.sdl.scdetector.Scorpioui;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.CloneSetInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.FileController;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.FileInfo;


/**
 * 
 * @author higo
 *
 */
class CodeCloneListViewModel extends AbstractTableModel {

    /**
     * コンストラクタ
     * 
     * @param cloneSet　モデルを初期化するためのクローンセット
     */
    CodeCloneListViewModel(final CloneSetInfo cloneSet) {
        this.codeclones = cloneSet.getCodeclones().toArray(new CodeCloneInfo[] {});
    }

    /**
     * 行数を返す
     * 
     * @Return 行数
     */
    @Override
    public int getRowCount() {
        return this.codeclones.length;
    }

    /**
     * 列数を返す
     * 
     * @Return 列数
     */
    @Override
    public int getColumnCount() {
        return TITLES.length;
    }

    /**
     * 指定した場所のオブジェクトを返す
     * 
     * @param row 行
     * @param col 列
     */
    @Override
    public Object getValueAt(int row, int col) {

        switch (col) {
        case COL_CLASS:
            final int fileID = this.codeclones[row].getFirstElement().getFileID();
            final FileInfo file = FileController.getInstance(Scorpioui.ID).getFile(fileID);
            return file.getName();
        case COL_POSITION:
            return this.getPositionText(this.codeclones[row]);
        case COL_GAPS:
            return this.codeclones[row].getNumberOfGapps();
        default:
            assert false : "Here shouldn't be reached!";
            return null;
        }
    }

    /**
     * 列の型を指定
     */
    @Override
    public Class<?> getColumnClass(int row) {
        return Integer.class;
    }

    /**
     * タイトルを返す
     */
    @Override
    public String getColumnName(int col) {
        return TITLES[col];
    }

    /**
     * 指定した列のコードクローンを返す
     * 
     * @param row 指令した列
     * @return　指定した列のコードクローン
     */
    public CodeCloneInfo getCodeClone(final int row) {
        return this.codeclones[row];
    }

    /**
     * このモデル内のコードクローン一覧を返す
     * 
     * @return このモデル内のコードクローン一覧
     */
    public CodeCloneInfo[] getCodeClones() {
        return this.codeclones;
    }

    private String getPositionText(final CodeCloneInfo codeFragment) {
        final StringBuilder text = new StringBuilder();
        text.append(codeFragment.getFirstElement().getFromLine());
        text.append(".");
        text.append(codeFragment.getFirstElement().getFromColumn());
        text.append(" - ");
        text.append(codeFragment.getLastElement().getToLine());
        text.append(".");
        text.append(codeFragment.getLastElement().getToColumn());
        return text.toString();
    }

    static final int COL_CLASS = 0;

    static final int COL_POSITION = 1;

    static final int COL_GAPS = 2;

    static final String[] TITLES = new String[] { "CLASS", "POSITION", "GAPS" };

    final private CodeCloneInfo[] codeclones;

}
