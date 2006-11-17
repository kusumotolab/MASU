package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader;

/**
 * @author kou-tngt
 * 
 * この例外はプラグインの構成情報を記録したXMLファイルの形式が、XMLの構文上正しくない場合や
 * 既定のフォーマットに従っていない場合，必要な情報が欠けている場合に投げられる．
 */
public class IllegalPluginXmlFormatException extends PluginLoadException {

    public IllegalPluginXmlFormatException() {
        super();
    }

    public IllegalPluginXmlFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalPluginXmlFormatException(String message) {
        super(message);
    }

    public IllegalPluginXmlFormatException(Throwable cause) {
        super(cause);
    }

}
