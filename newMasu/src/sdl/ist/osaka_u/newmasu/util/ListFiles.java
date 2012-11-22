package sdl.ist.osaka_u.newmasu.util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
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
	
	static class ListFilesWalker extends SimpleFileVisitor<Path> {
		
		private ArrayList<String> results = new ArrayList<String>();
		public ArrayList<String> getResults() {return results;}
		
		private String extension = "";
		
		public ListFilesWalker(String extension)
		{
			this.extension = extension;
		}
		
		// Print information about
		// each type of file.
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
			if (attr.isRegularFile() && file.toString().endsWith(extension))
				results.add(file.toString());
			return FileVisitResult.CONTINUE;
		}

		// Print each directory visited.
		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
			return FileVisitResult.CONTINUE;
		}

		// If there is some error accessing
		// the file, let the user know.
		// If you don't override this method
		// and an error occurs, an IOException
		// is thrown.
		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) {
			System.err.println(exc);
			return FileVisitResult.CONTINUE;
		}

	}
}
