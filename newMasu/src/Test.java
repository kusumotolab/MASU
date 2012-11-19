//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//
//import sdl.ist.osaka_u.skimura.util.ListFilesWalker;
//
//
//public class Test {
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		
//		final ListFilesWalker lf = new ListFilesWalker("java");
//		final Path path = Paths.get("./target");
//
//		try {
//			Files.walkFileTree(path, lf);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		final ArrayList<Path> paths = lf.getResults();
//		for(Path p : paths)
//		{
//			System.out.println();
//		}
//
//	}
//
//}
