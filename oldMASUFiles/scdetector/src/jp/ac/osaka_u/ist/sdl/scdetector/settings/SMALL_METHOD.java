package jp.ac.osaka_u.ist.sdl.scdetector.settings;


public enum SMALL_METHOD {

    HASHED {

        @Override
        public boolean isHashed() {
            return true;
        }
    },

    UNHASHED {

        @Override
        public boolean isHashed() {
            return false;
        }

    };

    public abstract boolean isHashed();
}
