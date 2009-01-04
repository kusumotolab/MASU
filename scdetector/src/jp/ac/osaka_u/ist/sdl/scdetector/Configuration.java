package jp.ac.osaka_u.ist.sdl.scdetector;


public final class Configuration {

    public static Configuration INSTANCE = new Configuration();

    private Configuration() {
        this.c = 50;
        this.d = null;
        this.l = null;
        this.o = null;
        this.r = true;
        this.pv = 1;
        this.pm = 2;
        this.pc = 2;
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

    public int getPM() {
        return this.pm;
    }

    public void setPM(final int pm) {

        if ((pm < 0) || (3 < pm)) {
            throw new RuntimeException("\"pm\" option must be between 0 and 3.");
        }
        this.pm = pm;
    }

    public int getPC() {
        return this.pc;
    }

    public void setPC(final int pc) {

        if ((pc < 0) || (3 < pc)) {
            throw new RuntimeException("\"pc\" option must be between 0 and 3.");
        }
        this.pc = pc;
    }

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
     */
    private int pv;

    /**
     * メソッド呼び出しの正規化レベルを指定するためのオプション
     */
    private int pm;

    /**
     * コンストラクタ呼び出しの正規化レベルを指定するためのオプション
     */
    private int pc;

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
