package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


public final class LineCommentInfo extends CommentInfo {

    public LineCommentInfo(final String content, final int fromLine, int fromColumn) {
        super(content, fromLine, fromColumn, fromLine, Integer.MAX_VALUE);
    }
}
