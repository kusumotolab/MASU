package jp.ac.osaka_u.ist.sdl.scdetector.settings;


public enum VERBOSE {

    TRUE {

        @Override
        public boolean isVerbose() {
            return true;
        }
    },

    FALSE {

        @Override
        public boolean isVerbose() {
            return false;
        }
    };

    public abstract boolean isVerbose();
}
