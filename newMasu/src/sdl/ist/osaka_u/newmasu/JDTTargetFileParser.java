package sdl.ist.osaka_u.newmasu;

import java.util.List;
import java.util.Set;

import sdl.ist.osaka_u.newmasu.AST.ASTCaller;

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

public class JDTTargetFileParser {

	public JDTTargetFileParser(
			/*final MessagePrinter out,
			final MessagePrinter err*/) {

//		MetricsToolSecurityManager.getInstance().checkAccess();
//		if ((null == out) || (null == err)) {
//			throw new IllegalArgumentException();
//		}

		switch (Settings.getInstance().getLanguage()) {

		case JAVA15:
		case JAVA14:
		case JAVA13:

			final ASTCaller ast = new ASTCaller();

			break;

		case CSHARP:
			assert false : "The new MASU cannot solve C# files";
		default:
			assert false : "here shouldn't be reached!";
		}

	}
}
