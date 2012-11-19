package sdl.ist.osaka_u.newmasu.util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class ListFilesWalker extends SimpleFileVisitor<Path> {
	
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
