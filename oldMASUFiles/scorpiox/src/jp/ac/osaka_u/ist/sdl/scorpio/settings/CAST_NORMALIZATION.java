package jp.ac.osaka_u.ist.sdl.scorpio.settings;


public enum CAST_NORMALIZATION {

    /**
     * �L���X�g�����̂܂ܗp���� 
     */
    NO {

        @Override
        public String getText() {
            return "no";
        }
    },

    /**
     * �L���X�g�����̌^�̐��K������
     */
    TYPE {

        @Override
        public String getText() {
            return "type";
        }
    },

    /**
     * �S�ẴL���X�g�𓯈�̎���ɐ��K������
     */
    ALL {

        @Override
        public String getText() {
            return "all";
        }
    };

    public abstract String getText();
}
