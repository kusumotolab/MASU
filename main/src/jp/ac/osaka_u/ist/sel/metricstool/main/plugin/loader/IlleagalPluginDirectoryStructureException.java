package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader;

/**
 * @author kou-tngt
 * 
 * この例外は，プラグインのディレクトリ構成がプラグインの規則に従っていない場合に投げられる．
 * 具体的には，plugin.xmlがプラグインのディレクトリ直下に存在しない場合などである．
 */
public class IlleagalPluginDirectoryStructureException extends PluginLoadException {

    public IlleagalPluginDirectoryStructureException() {
        super();
    }

    public IlleagalPluginDirectoryStructureException(String message, Throwable cause) {
        super(message, cause);
    }

    public IlleagalPluginDirectoryStructureException(String message) {
        super(message);
    }

    public IlleagalPluginDirectoryStructureException(Throwable cause) {
        super(cause);
    }

}
