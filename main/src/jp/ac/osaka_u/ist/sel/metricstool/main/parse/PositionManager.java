package jp.ac.osaka_u.ist.sel.metricstool.main.parse;


public interface PositionManager {

    public int getStartLine(Object key);

    public int getStartColumn(Object key);

    public int getEndLine(Object key);

    public int getEndColumn(Object key);

    public void setStartLine(Object key, int line);

    public void setStartColumn(Object key, int column);

    public void setEndLine(Object key, int line);

    public void setEndColumn(Object key, int column);

    public void setPosition(Object key, int startLine, int startColumn, int endLine,
            int endColumn);

}