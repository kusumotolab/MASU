package jp.ac.osaka_u.ist.sdl.scdetector;


public final class Configuration {

    public static Configuration INSTANCE = new Configuration();

    private Configuration() {
        this.d = null;
        this.l = null;
        this.o = null;
        this.pv = 1;
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

    public int getPV() {
        return this.pv;
    }

    public void setPV(final int pv) {

        if ((pv < 0) || (2 < pv)) {
            throw new RuntimeException("\"pv\" option must be between 0 and 2.");
        }
        this.pv = pv;
    }

    private String d;

    private String l;

    private String o;

    private int pv;
}
