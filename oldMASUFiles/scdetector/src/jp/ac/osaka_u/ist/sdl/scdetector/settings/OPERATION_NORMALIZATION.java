package jp.ac.osaka_u.ist.sdl.scdetector.settings;

public enum OPERATION_NORMALIZATION {

    /**
     * ���Z�����̂܂ܗp���� 
     */
    NO {

        @Override
        public String getText() {
            return "no";
        }
    },

    /**
     * ���Z�����̌^�̐��K������
     */
    TYPE {

        @Override
        public String getText() {
            return "type";
        }
    },

    /**
     *�@���Z�̃L���X�g�𓯈�̎���ɐ��K������
     */
    ALL {

        @Override
        public String getText() {
            return "all";
        }
    };

    public abstract String getText();
}
