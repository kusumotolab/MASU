package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.TypeParameterStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedSuperTypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeParameterInfo;


/**
 * 型パラメータ情報を構築するビルダー
 * 
 * @author kou-tngt, t-miyake
 *
 */
public class TypeParameterBuilder extends CompoundDataBuilder<UnresolvedTypeParameterInfo> {

    /**
     * 引数に与えられた構築データの管理者とデフォルトの名前情報ビルダー，型情報ビルダーを用いて初期化する
     * @param buildDataManager　構築データの管理者
     */
    public TypeParameterBuilder(final BuildDataManager buildDataManager) {
        this(buildDataManager, new NameBuilder(), new TypeBuilder(buildDataManager));
    }

    /**
     * 引数に与えられた構築データの管理者，名前情報ビルダー，型情報ビルダーを用いて初期化する
     * @param buildDataManager　構築データの管理者
     * @param nameBuilder　名前情報ビルダー
     * @param typeBuilder　型情報ビルダー
     */
    public TypeParameterBuilder(final BuildDataManager buildDataManager, final NameBuilder nameBuilder,
            final TypeBuilder typeBuilder) {
        if (null == buildDataManager) {
            throw new NullPointerException("buildDataManager is null.");
        }

        if (null == nameBuilder) {
            throw new NullPointerException("nameBuilder is null.");
        }

        if (null == typeBuilder) {
            throw new NullPointerException("typeBuilder is null.");
        }

        this.buildDataManager = buildDataManager;
        this.nameBuilder = nameBuilder;
        this.typeBuilder = typeBuilder;

        //内部ビルダーの登録
        this.addInnerBuilder(nameBuilder);
        this.addInnerBuilder(typeBuilder);

        //状態通知を受け取りたいものを登録
        this.addStateManager(new TypeParameterStateManager());
    }

    /**
     * 状態変化イベントの通知を受けるメソッド．
     * @param event 状態変化イベント
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.StateDrivenDataBuilder#stateChangend(jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent)
     */
    @Override
    public void stateChangend(final StateChangeEvent<AstVisitEvent> event) {
        final StateChangeEventType type = event.getType();

        if (this.isActive()) {
            if (type.equals(TypeParameterStateManager.TYPE_PARAMETER.ENTER_TYPE_PARAMETER_DEF)) {
                //型パラメータ定義に入ったのでがんばる
                this.nameBuilder.activate();
                this.typeBuilder.activate();
                this.inTypeParameterDefinition = true;
            } else if (type
                    .equals(TypeParameterStateManager.TYPE_PARAMETER.EXIT_TYPE_PARAMETER_DEF)) {
                //型パラメータ定義が終わったので，データを構築して後始末
                this.buildTypeParameter();

                this.nameBuilder.deactivate();
                this.typeBuilder.deactivate();
                this.lowerBoundsType = null;
                this.upperBoundsType = null;
                this.nameBuilder.clearBuiltData();
                this.typeBuilder.clearBuiltData();
                this.inTypeParameterDefinition = false;

            } else if (this.inTypeParameterDefinition) {
                //型パラメータ定義部ないでの出来事
                if (type.equals(TypeParameterStateManager.TYPE_PARAMETER.ENTER_TYPE_LOWER_BOUNDS)) {
                    //型の下限宣言がきたので型構築部ががんばる
                    this.nameBuilder.deactivate();
                    this.typeBuilder.activate();
                } else if (type
                        .equals(TypeParameterStateManager.TYPE_PARAMETER.EXIT_TYPE_LOWER_BOUNDS)) {
                    //型の下限情報を構築する
                    this.lowerBoundsType = this.builtTypeBounds();
                } else if (type
                        .equals(TypeParameterStateManager.TYPE_PARAMETER.ENTER_TYPE_UPPER_BOUNDS)) {
                    //型の上限宣言が来たので構築部ががんばる
                    this.nameBuilder.deactivate();
                    this.typeBuilder.activate();
                } else if (type
                        .equals(TypeParameterStateManager.TYPE_PARAMETER.EXIT_TYPE_UPPER_BOUNDS)) {
                    //型の上限情報を構築する
                    this.upperBoundsType = this.builtTypeBounds();
                }
            }
        }
    }

    /**
     * 型パラメータ定義部の終了時に呼び出され，型パラメータを構築するメソッド
     * このメソッドをオーバーライドすることで，型パラメータ定義部で任意の処理を行わせることができる．
     */
    protected void buildTypeParameter() {
        //型パラメータの名前が複数合ったらおかしい
        assert (this.nameBuilder.getBuiltDataCount() == 1);

        final String[] name = this.nameBuilder.getFirstBuiltData();
        
        //型パラメータの名前が複数個に分かれててもおかしい
        assert (name.length == 1);

        //型の上限情報下限情報を取得
        final UnresolvedTypeInfo upperBounds = this.getUpperBounds();
        final UnresolvedTypeInfo lowerBounds = this.getLowerBounds();

        UnresolvedTypeParameterInfo parameter = null;
        if (null == lowerBounds) {
            //下限がなければ普通に作る
            parameter = new UnresolvedTypeParameterInfo(name[0], upperBounds);
        } else {
            //下限がある場合はこっちを作る
            parameter = new UnresolvedSuperTypeParameterInfo(name[0], upperBounds, lowerBounds);
        }

        //最後にデータ管理者に登録する
        this.buildDataManager.addTypeParameger(parameter);
    }

    /**
     * 型の上限情報を返す．
     * @return　型の上限情報
     */
    protected UnresolvedTypeInfo getUpperBounds() {
        return this.upperBoundsType;
    }

    /**
     * 型の下限情報を返す．
     * @return　型の下限情報
     */
    protected UnresolvedTypeInfo getLowerBounds() {
        return this.lowerBoundsType;
    }

    /**
     * 最後に構築された型の情報を返す．
     * @return　最後に構築された型
     */
    protected UnresolvedTypeInfo builtTypeBounds() {
        return this.typeBuilder.getLastBuildData();
    }

    /**
     * 名前構築を行うビルダーを返す．
     * @return　名前構築を行うビルダー
     */
    protected NameBuilder getNameBuilder() {
        return this.nameBuilder;
    }

    /**
     * 型情報を構築するビルダーを返す
     * @return　型情報を構築するビルダー
     */
    protected TypeBuilder getTypeBuilder() {
        return this.typeBuilder;
    }

    /**
     * 名前構築を行うビルダー
     */
    private final NameBuilder nameBuilder;

    /**
     * 型情報を構築するビルダー
     */
    private final TypeBuilder typeBuilder;

    /**
     * 構築情報の管理者
     */
    private final BuildDataManager buildDataManager;

    /**
     * 型パラメータの上限
     */
    private UnresolvedTypeInfo upperBoundsType;

    /**
     * 型パラメータの下限
     */
    private UnresolvedTypeInfo lowerBoundsType;

    /**
     * 型パラメータ定義部にいるかどうかを表す
     */
    private boolean inTypeParameterDefinition = false;

}
