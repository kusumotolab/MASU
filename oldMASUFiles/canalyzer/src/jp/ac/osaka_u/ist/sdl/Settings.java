package jp.ac.osaka_u.ist.sdl;

/**
 * 
 * @author higo
 * 
 *         実行時の引数情報を格納するためのクラス
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
	 * @return 解析対象ディレクトリ
	 * 
	 *         解析対象ディレクトリを返す．
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
	 * 解析対象ディレクトリを記録するための変数
	 */
	private String targetDirectory;
}
