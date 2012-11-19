package sdl.ist.osaka_u.newmasu.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class ListFiles {

	/**
	 * ディレクトリにあるファイルを列挙する
	 * @param extension 列挙したい拡張子
	 * @param targetDirPath 対象のディレクトリ
	 * @return ファイル名の配列
	 */
	public static ArrayList<String> list(String extension, Path targetDirPath) {
		final ListFilesWalker jarWalker = new ListFilesWalker(extension);
		final Path jarDirPath = targetDirPath;
		try {
			Files.walkFileTree(jarDirPath, jarWalker);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jarWalker.getResults();
	}
}
