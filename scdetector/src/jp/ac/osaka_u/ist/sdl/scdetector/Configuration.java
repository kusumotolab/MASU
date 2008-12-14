package jp.ac.osaka_u.ist.sdl.scdetector;


public final class Configuration {

    public static Configuration INSTANCE = new Configuration();

    private Configuration() {
        this.d = null;
        this.l = null;
        this.o = null;
        this.pv = 1;
        this.pm = 2;
        this.pc = 2;
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

    private String d;

    private String l;

    private String o;

    private int pv;

    private int pm;

    private int pc;
}
