package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRICS_TYPE;


/**
 * @author kou-tngt
 * 
 * 仮実装(2006/11/17）
 * 他のコード群で型を利用するため，コンパイル用に登録．
 * 
 * <p>
 * メトリクス計測プラグイン実装用の抽象クラス<p>
 * 各プラグインはこのクラスを継承したクラスを1つもたなければならない．
 * また，そのクラス名をplugin.xmlファイルに指定の形式で記述しなければならない．<p>
 * mainモジュールは各プラグインディレクトリからplugin.xmlファイルを探索し、
 * そこに記述されている，このクラスを継承したクラスをインスタンス化し、
 * 各メソッドを通じて情報を取得した後、executeメソッドを呼び出してメトリクス値を計測する
 */
public abstract class AbstractPlugin {

    /**
     * このプラグインがメトリクスを計測できる言語を返す
     * 利用できる言語に制限のあるプラグインは、このメソッドをオーバーライドする必要がある．
     * @return 計測可能な言語を全て含む配列
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE
     */
    public LANGUAGE[] getMesuarableLanguages() {
        return LANGUAGE.values();
    }

    /**
     * このプラグインが計測するメトリクスの名前を返す抽象メソッド．
     * @return メトリクス名
     */
    public abstract String getMetricsName();

    /**
     * このプラグインが計測するメトリクスのタイプを返す抽象メソッド．
     * @return メトリクスタイプ
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.util.METRICS_TYPE
     */
    public abstract METRICS_TYPE getMetricsType();

    /**
     * このプラグインがクラスに関する情報を利用するかどうかを返すメソッド．
     * デフォルト実装ではfalseを返す．
     * クラスに関する情報を利用するプラグインはこのメソッドをオーバーラードしてtrueを返さなければ成らない．
     * @return ファイルに関する情報を利用する．
     */
    public boolean useClassInfo() {
        return false;
    }

    /**
     * このプラグインがファイルに関する情報を利用するかどうかを返すメソッド．
     * デフォルト実装ではfalseを返す．
     * ファイルに関する情報を利用するプラグインはこのメソッドをオーバーラードしてtrueを返さなければ成らない．
     * @return ファイルに関する情報を利用する．
     */
    public boolean useFileInfo() {
        return false;
    }

    /**
     * このプラグインがメソッドに関する情報を利用するかどうかを返すメソッド．
     * デフォルト実装ではfalseを返す．
     * ファイルに関する情報を利用するプラグインはこのメソッドをオーバーラードしてtrueを返さなければ成らない．
     * @return ファイルに関する情報を利用する．
     */
    public boolean useMethodInfo() {
        return false;
    }

    /**
     * メトリクス解析をスタートする抽象メソッド．
     */
    protected abstract void execute();
}
