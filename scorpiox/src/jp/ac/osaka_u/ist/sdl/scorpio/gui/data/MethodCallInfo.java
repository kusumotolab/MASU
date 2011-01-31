package jp.ac.osaka_u.ist.sdl.scorpio.gui.data;

public class MethodCallInfo {

	final MethodInfo caller;
	final MethodInfo callee;

	final int fromLine;
	final int fromColumn;
	final int toLine;
	final int toColumn;

	public MethodCallInfo(final MethodInfo caller, final MethodInfo callee,
			final int fromLine, final int fromColumn, final int toLine,
			final int toColumn) {
		this.caller = caller;
		this.callee = callee;
		this.fromLine = fromLine;
		this.fromColumn = fromColumn;
		this.toLine = toLine;
		this.toColumn = toColumn;
	}
}
