package sdl.ist.osaka_u.newmasu;

import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
//import antlr.ASTFactory;
//import antlr.RecognitionException;
//import antlr.TokenStreamException;
//import antlr.collections.AST;
//import antlr.ASTFactory;
//import antlr.RecognitionException;
//import antlr.TokenStreamException;
//import antlr.collections.AST;

public class JDTTargetFileParser{

	public JDTTargetFileParser(final MessagePrinter out, final MessagePrinter err) {

		MetricsToolSecurityManager.getInstance().checkAccess();
		if ( (null == out)
				|| (null == err)) {
			throw new IllegalArgumentException();
		}
//		this.out = out;
//		this.err = err;
		
		
		final Set<String> dirPaths = Settings.getInstance().getTargetDirectories();
		final List<String> jarPaths = Settings.getInstance().getLibraries();
		final Set<String> javaPaths = Settings.getInstance().getListFiles();
	
		final String[] sourcePaths = dirPaths.toArray(new String[dirPaths.size()]);
		final String[] classPaths = jarPaths.toArray(new String[jarPaths.size()]);
		final String[] sources = javaPaths.toArray(new String[javaPaths.size()]);
		
		
		for(String filePath: javaPaths)
		{
//			try {
//
//				if (Settings.getInstance().isVerbose()) {
//					final StringBuilder text = new StringBuilder();
//					text.append("parsing ");
//					text.append(file.getName());
//					text.append(" [");
//					text.append(i + 1);
//					text.append("/");
//					text.append(file.length);
//					text.append("]");
//					this.out.println(text.toString());
//				}
//
//				final FileInfo fileInfo = new FileInfo(filePath);
//				DataManager.getInstance().getFileInfoManager()
//						.add(fileInfo, Thread.currentThread());
//
//				final BufferedReader reader = new BufferedReader(
//						new InputStreamReader(new FileInputStream(
//								this.files[i].getName()), "JISAutoDetect"));



//				reader.close();

//			} catch (FileNotFoundException e) {
//				err.println(e.getMessage());
//			} 
////			catch (RecognitionException e) {
////				this.files[i].setCorrectSytax(false);
////				err.println(e.getMessage());
////				// TODO エラーが起こったことを TargetFileData などに通知する処理が必要
////			} catch (TokenStreamException e) {
////				this.files[i].setCorrectSytax(false);
////				err.println(e.getMessage());
////				// TODO エラーが起こったことを TargetFileData などに通知する処理が必要
////			} 
////			catch (ASTParseException e) {
////				err.println(e.getMessage());
////			}
//			catch (IOException e) {
//				err.println(e.getMessage());
//			}
		}

		
		
		switch (Settings.getInstance().getLanguage()) {
		
		case JAVA15:
		case JAVA14:
		case JAVA13:
			
			final ASTCaller ast = new ASTCaller();
//			for( TargetFile f : files){
//				//TODO: 
//				f.setCorrectSytax(true);
//			}
			
			
			// JavaAstVisitorManagerの書き換え
			
		
//			final Java15Lexer java15lexer = new Java15Lexer(reader);
//			java15lexer.setTabSize(1);
//			final Java15Parser java15parser = new Java15Parser(
//					java15lexer);
//
//			final ASTFactory java15factory = new MasuAstFactory();
//			java15factory
//					.setASTNodeClass(CommonASTWithLineNumber.class);
//
//			java15parser.setASTFactory(java15factory);
//
//			java15parser.compilationUnit();
//			this.files[i].setCorrectSytax(true);
//
//			AstVisitorManager<AST> java15VisitorManager = new JavaAstVisitorManager<AST>(
//					new AntlrAstVisitor(new Java15AntlrAstTranslator()),
//					Settings.getInstance());
//			java15VisitorManager.visitStart(java15parser.getAST());


			//TODO: astから情報取得

//			fileInfo.addAllComments(java15lexer.getCommentSet());
//			fileInfo.setLOC(java15lexer.getLine());

			break;

		case CSHARP:
			assert false : "The new MASU cannot solve C# files";
		default:
			assert false : "here shouldn't be reached!";
		}
		
		
	}
//
//	private final TargetFile[] files;
//
//	private final AtomicInteger index;
//
//	private final MessagePrinter out;
//
//	private final MessagePrinter err;

}
