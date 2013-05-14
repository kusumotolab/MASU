package jp.ac.osaka_u.ist.sdl.scdetector.settings;

public enum CALL_NORMALIZATION {

	/**
	 * ���\�b�h����`����Ă���N���X�̊��S���薼���p����
	 */
	FQN {

		@Override
		public String getText() {
			return "fqn";
		}
	},

	/**
	 * �Ăяo�����͂��̂܂܁D���������p����D
	 */
	NO {

		@Override
		public String getText() {
			return "no";
		}
	},

	/**
	 * �Ăяo������Ԃ�l�̌^���ɕϊ�����D���������p����D
	 */
	TYPE_WITH_ARG {

		@Override
		public String getText() {
			return "type with arg";
		}
	},

	/**
	 *�@�Ăяo������Ԃ�l�̌^���ɕϊ�����D�������͗p���Ȃ��D
	 */
	TYPE_WITHOUT_ARG {

		@Override
		public String getText() {
			return "type without arg";
		}
	},

	/**
	 * �S�Ă̌Ăяo���𓯈ꎚ��ɐ��K������D�������͗p���Ȃ��D
	 */
	ALL {

		@Override
		public String getText() {
			return "all";
		}
	};

	public abstract String getText();
}
