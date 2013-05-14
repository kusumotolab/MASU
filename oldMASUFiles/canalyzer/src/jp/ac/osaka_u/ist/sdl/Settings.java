package jp.ac.osaka_u.ist.sdl;

/**
 * 
 * @author higo
 * 
 *         ���s���̈��������i�[���邽�߂̃N���X
 * 
 */
public class Settings {

	private static Settings INSTANCE = null;

	public static Settings getInstance() {
		if (null == INSTANCE) {
			INSTANCE = new Settings();
		}
		return INSTANCE;
	}

	private Settings() {
		this.targetDirectory = null;
	}

	/**
	 * 
	 * @return ��͑Ώۃf�B���N�g��
	 * 
	 *         ��͑Ώۃf�B���N�g����Ԃ��D
	 * 
	 */
	public String getTargetDirectory() {
		return this.targetDirectory;
	}

	public void setTargetDirectory(final String targetDirectory) {

		if (null == targetDirectory) {
			throw new IllegalArgumentException();
		}
		this.targetDirectory = targetDirectory;
	}

	/**
	 * ��͑Ώۃf�B���N�g�����L�^���邽�߂̕ϐ�
	 */
	private String targetDirectory;
}
