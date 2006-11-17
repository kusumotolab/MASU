package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader;

/**
 * @author kou-tngt
 * 
 * この例外は，プラグインのディレクトリ構成がプラグインの規則に従っていない場合に投げられる．
 * 具体的には，plugin.xmlがプラグインのディレクトリ直下に存在しない場合などである．
 */
public class IllegalPluginDirectoryStructureException extends PluginLoadException {

    public IllegalPluginDirectoryStructureException() {
        super();
    }

    public IllegalPluginDirectoryStructureException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalPluginDirectoryStructureException(String message) {
        super(message);
    }

    public IllegalPluginDirectoryStructureException(Throwable cause) {
        super(cause);
    }

}
