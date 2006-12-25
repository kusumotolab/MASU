package jp.ac.osaka_u.ist.sel.metricstool.main.parse;

import java.util.HashMap;
import java.util.Map;

public class DefaultPositionManager implements PositionManager {
    public static class Position{
        public int getEndLine() {
            return endLine;
        }
        public void setEndLine(int endLine) {
            this.endLine = endLine;
        }
        public int getStartLine() {
            return startLine;
        }
        public void setStartLine(int startLine) {
            this.startLine = startLine;
        }
        public int getEndColumn() {
            return endColumn;
        }
        public void setEndColumn(int endColumn) {
            this.endColumn = endColumn;
        }
        public int getStartColumn() {
            return startColumn;
        }
        public void setStartColumn(int startColumn) {
            this.startColumn = startColumn;
        }
        
        public String toString(){
            return startLine + "." + startColumn + " - " + endLine + "." + endColumn;
        }
        
        private int startLine;
        private int startColumn;
        private int endLine;
        private int endColumn;
    }


    public int getStartLine(Object key) {
        return getLineColumn(key).getStartLine();
    }
    
    public int getStartColumn(Object key){
        return getLineColumn(key).getStartColumn();
    }

    public int getEndLine(Object key) {
        return getLineColumn(key).getEndLine();
    }
    
    public int getEndColumn(Object key){
        return getLineColumn(key).getEndColumn();
    }
    
    public void setStartLine(Object key, int line){
        getLineColumn(key).setStartLine(line);
    }
    
    public void setStartColumn(Object key, int column){
        getLineColumn(key).setStartLine(column);
    }
    
    public void setEndLine(Object key, int line){
        getLineColumn(key).setEndLine(line);
    }
    
    public void setEndColumn(Object key, int column){
        getLineColumn(key).setEndLine(column);
    }
    
    public void setPosition(Object key, int startLine, int startColumn, int endLine, int endColumn){
        Position position = getLineColumn(key);
        position.setStartLine(startLine);
        position.setStartColumn(startColumn);
        position.setEndLine(endLine);
        position.setEndColumn(endColumn);
    }
    
    private Position getLineColumn(Object key){
        if (null == key){
            throw new NullPointerException("key is null");
        }
        
        if (dataMap.containsKey(key)){
            return dataMap.get(key);
        } else {
            Position newLine = new Position();
            dataMap.put(key, newLine);
            return newLine;
        }
    }
    
    private final Map<Object,Position> dataMap = new HashMap<Object, Position>();
}
