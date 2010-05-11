package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * クラス，メソッド，フィールドなどの修飾子を表すクラス．現在以下の，修飾子情報を持つ
 * <ul>
 * <li>public</li>
 * <li>private</li>
 * <li>virtual(abstract)
 * <li>
 * </ul>
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public class ModifierInfo implements Serializable {

    public ModifierInfo() {
    }

    /**
     * 修飾子名を返す
     * 
     * @return 修飾子名
     */
    public String getName() {
        return this.name;
    }

    protected String name;

    @Override
    public String toString() {
        return this.name;
    }
}
