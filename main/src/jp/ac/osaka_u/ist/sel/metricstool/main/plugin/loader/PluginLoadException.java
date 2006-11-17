package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader;

/**
 * @author kou-tngt
 * 
 * この例外はプラグインのロードを試み，失敗した時に投げられる．
 */
public class PluginLoadException extends Exception {
    public PluginLoadException(){
        super();
    }
    
    public PluginLoadException(String message){
        super(message);
    }

    public PluginLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginLoadException(Throwable cause) {
        super(cause);
    }
}
