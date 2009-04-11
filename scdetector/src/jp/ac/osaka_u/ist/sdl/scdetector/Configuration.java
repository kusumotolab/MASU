package jp.ac.osaka_u.ist.sdl.scdetector;


public final class Configuration {

    public static Configuration INSTANCE = new Configuration();

    private Configuration() {
        this.c = 1000000;
        this.d = null;
        this.l = null;
        this.o = null;
        this.r = true;
        this.pv = 1;
        this.pi = 1;
        this.pc = 1;
        this.po = 0;
        this.pl = 1;
        this.pr = false;
        this.fi = false;
        this.fj = false;
        this.fk = 100;
        this.fl = 2;
    }

    public int getC() {
        return this.c;
    }

    public void setC(final int c) {
        this.c = c;
    }

    public String getD() {
        return this.d;
    }

    public void setD(final String d) {
        this.d = d;
    }

    public String getL() {
        return this.l;
    }

    public void setL(final String l) {
        this.l = l;
    }

    public String getO() {
        return this.o;
    }

    public void setO(final String o) {
        this.o = o;
    }

    public boolean getR() {
        return this.r;
    }

    public void setR(final boolean r) {
        this.r = r;
    }

    public int getS() {
        return this.s;
    }

    public void setS(final int s) {
        this.s = s;
    }

    public int getPV() {
        return this.pv;
    }

    public void setPV(final int pv) {

        if ((pv < 0) || (2 < pv)) {
            throw new RuntimeException("\"pv\" option must be between 0 and 2.");
        }
        this.pv = pv;
    }

    public int getPI() {
        return this.pi;
    }

    public void setPI(final int pi) {

        if ((pi < 0) || (3 < pi)) {
            throw new RuntimeException("\"pi\" option must be between 0 and 3.");
        }
        this.pi = pi;
    }

    public int getPC() {
        return this.pc;
    }

    public void setPC(final int pc) {

        if ((pc < 0) || (2 < pc)) {
            throw new RuntimeException("\"pc\" option must be between 0 and 2.");
        }
        this.pc = pc;
    }

    public int getPO() {
        return this.po;
    }

    public void setPO(final int po) {

        if ((po < 0) || (2 < po)) {
            throw new RuntimeException("\"po\" option must be between 0 and 2.");
        }
        this.po = po;
    }

    public int getPL() {
        return this.pl;
    }

    public void setPL(final int pl) {

        if ((pl < 0) || (2 < pl)) {
            throw new RuntimeException("\"pl\" option must be between 0 and 2.");
        }
        this.pl = pl;
    }

    public boolean getPR() {
        return this.pr;
    }

    public void setPR(final boolean pr) {
        this.pr = pr;
    }

    // 他のクローンペアに包含されているクローンペアを取り除くオプション
    public boolean getFI() {
        return this.fi;
    }

    public boolean setFI(final boolean fi) {
        return this.fi = fi;
    }

    public boolean getFJ() {
        return this.fj;
    }

    public void setFJ(final boolean fj) {
        this.fj = fj;
    }

    public int getFK() {
        return this.fk;
    }

    public void setFK(final int fk) {

        if ((fk < 1) || (100 < fk)) {
            throw new RuntimeException("\"fk\" option must be between 1 and 100.");
        }

        this.fk = fk;
    }

    public int getFL() {
        return this.fl;
    }

    public void setFL(final int fl) {

        if (fl < 0) {
            throw new RuntimeException("\"fl\" option must be 0 or more.");
        }

        this.fl = fl;
    }

    /**
     * ハッシュ値が同じ文が閾値以上の個数ある場合は，スライス拠点としないためのオプション
     */
    private int c;

    /**
     * 解析対象ディレクトリを指定するためのオプション
     */
    private String d;

    /**
     * 解析対象プログラミング言語を指定するためのオプション
     */
    private String l;

    /**
     * 解析結果を出力するファイルを指定するためのオプション
     */
    private String o;

    /**
     * 変数参照もバックワードスライスの依存関係として含めるかを指定するためのオプション
     */
    private boolean r;

    /**
     * 出力するコードクローンのサイズの下限を指定するためのオプション．
     * 大きさは文の数を表す．
     */
    private int s;

    /**
     * 変数利用の正規化レベルを指定するためのオプション
     * 0: 変数名をそのまま使う
     * 1: 変数名を型名に正規化する．
     * 2: 全ての変数を同一字句に正規化する．
     */
    private int pv;

    /**
     * (メソッドまたはコンストラクタ)呼び出しの正規化レベルを指定するためのオプション
     * 0: 呼び出し名はそのまま，引数情報も用いる
     * 1: 呼び出し名を返り値の型名に変換する，引数情報も用いる．
     * 2: 呼び出し名を返り値の型名に変換する，引数情報は用いない．
     * 3: 全ての呼び出しを同一字句に正規化する．引数情報は用いない
     */
    private int pi;

    /**
     * キャスト使用の正規化レベルを指定するためのオプション
     * 0: キャスト使用をそのまま用いる．
     * 1: キャスト使用を型に正規化する．
     * 2: 全てのキャスト使用を同一字句に正規化する．
     */
    private int pc;

    /**
     * 
     * 単項演算，二項演算，三項演算の正規化レベルを指定するためのオプション
     * 0: 演算をそのまま用いる
     * 1: 演算をその型に正規化する
     * 2: 全ての演算を同一の字句に正規化する
     */
    private int po;

    /**
     *
     * リテラルの正規化レベルを指定するためのオプション
     * 0: リテラルをそのまま用いる
     * 1: リテラルをその型の正規化する
     * 2: 全てのリテラルを同一の字句に正規化する 
     */
    private int pl;

    /**
     * 
     * クラス参照名を正規化するためのオプション
     * false: クラス参照は正規化しない
     * true: 全てのクラス参照を同一字句に正規化する
     */
    private boolean pr;

    /**
     * 他のクローンペアに内包されるクローンペアをフィルタリングするためのオプション
     */
    private boolean fi;

    /**
     * 始めと終りが一致しているクローンペアをフィルタリングするためのオプション
     */
    private boolean fj;

    /**
     * 閾値以上オーバーラップしているクローンペアをフィルタリングするためのオプション
     */
    private int fk;

    /**
     * コードサイズが閾値以上異なるクローンペアをフィルタリングするためのオプション
     */
    private int fl;
}
