package jp.ac.osaka_u.ist.sdl.scorpio.settings;


public enum LITERAL_NORMALIZATION {

    /**
     * ���e���������̂܂ܗp���� 
     */
    NO {

        @Override
        public String getText() {
            return "no";
        }
    },

    /**
     * ���e���������̌^�̐��K������
     */
    TYPE {

        @Override
        public String getText() {
            return "type";
        }
    },

    /**
     * �S�Ẵ��e�����𓯈�̎���ɐ��K������
     */
    ALL {

        @Override
        public String getText() {
            return "all";
        }
    };

    public abstract String getText();
}
