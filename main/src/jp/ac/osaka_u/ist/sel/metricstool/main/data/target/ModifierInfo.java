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
public final class ModifierInfo implements Serializable {

    /**
     * abstract を表す定数
     */
    public static final String ABSTRACT_STRING = "abstract";

    /**
     * final を表す定数
     */
    public static final String FINAL_STRING = "final";

    /**
     * private を表す定数
     */
    public static final String PRIVATE_STRING = "private";

    /**
     * protected を表す定数
     */
    public static final String PROTECTED_STRING = "protected";

    /**
     * public を表す定数
     */
    public static final String PUBLIC_STRING = "public";

    /**
     * static を表す定数
     */
    public static final String STATIC_STRING = "static";

    /**
     * virtual を表す定数
     */
    public static final String VIRTUAL_STRING = "virtual";

    /**
     * abstract を表す定数
     */
    public static final ModifierInfo ABSTRACT = new ModifierInfo(ABSTRACT_STRING);

    /**
     * final を表す定数
     */
    public static final ModifierInfo FINAL = new ModifierInfo(FINAL_STRING);

    /**
     * private を表す定数
     */
    public static final ModifierInfo PRIVATE = new ModifierInfo(PRIVATE_STRING);

    /**
     * protected を表す定数
     */
    public static final ModifierInfo PROTECTED = new ModifierInfo(PROTECTED_STRING);

    /**
     * public を表す定数
     */
    public static final ModifierInfo PUBLIC = new ModifierInfo(PUBLIC_STRING);

    /**
     * static を表す定数
     */
    public static final ModifierInfo STATIC = new ModifierInfo(STATIC_STRING);

    /**
     * virtual を表す定数
     */
    public static final ModifierInfo VIRTUAL = new ModifierInfo(VIRTUAL_STRING);

    /**
     * 修飾子名から，修飾子オブジェクトを生成するファクトリメソッド
     * 
     * @param name 修飾子名
     * @return 修飾子オブジェクト
     */
    public static ModifierInfo getModifierInfo(final String name) {

        ModifierInfo requiredModifier = MODIFIERS.get(name);
        if (null == requiredModifier) {
            requiredModifier = new ModifierInfo(name);
            MODIFIERS.put(name, requiredModifier);
        }

        return requiredModifier;
    }

    /**
     * 修飾子名を与えて，オブジェクトを初期化
     * 
     * @param name
     */
    private ModifierInfo(final String name) {
        this.name = name;
    }

    /**
     * 修飾子名を返す
     * 
     * @return 修飾子名
     */
    public String getName() {
        return this.name;
    }

    /**
     * 生成した ModifierInfo オブジェクトを保存していくための定数
     */
    private static final Map<String, ModifierInfo> MODIFIERS = new HashMap<String, ModifierInfo>();

    static {
        MODIFIERS.put(ABSTRACT_STRING, ABSTRACT);
        MODIFIERS.put(FINAL_STRING, FINAL);
        MODIFIERS.put(PRIVATE_STRING, PRIVATE);
        MODIFIERS.put(PROTECTED_STRING, PROTECTED);
        MODIFIERS.put(PUBLIC_STRING, PUBLIC);
        MODIFIERS.put(STATIC_STRING, STATIC);
        MODIFIERS.put(VIRTUAL_STRING, VIRTUAL);
    }

    /**
     * 修飾子名を保存するための変数
     */
    private final String name;
}
