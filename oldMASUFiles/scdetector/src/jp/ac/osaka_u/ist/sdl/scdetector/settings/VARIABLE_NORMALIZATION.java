package jp.ac.osaka_u.ist.sdl.scdetector.settings;

public enum VARIABLE_NORMALIZATION {

    /**
     * �ϐ������̂܂ܗp���� 
     */
    NO {

        @Override
        public String getText() {
            return "no";
        }
    },

    /**
     * �ϐ������̌^�̐��K������
     */
    TYPE {

        @Override
        public String getText() {
            return "type";
        }
    },

    /**
     * �S�Ă̕ϐ��𓯈�̎���ɐ��K������
     */
    ALL {

        @Override
        public String getText() {
            return "all";
        }
    };

    public abstract String getText();
}
