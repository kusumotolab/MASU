package jp.ac.osaka_u.ist.sdl.scorpio.gui.inter.method;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;
import java.util.SortedSet;

import javax.swing.JPanel;

import jp.ac.osaka_u.ist.sdl.scorpio.gui.SelectedEntities;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.MethodCallInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.MethodInfo;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

public class MethodGraphView extends JPanel implements Observer {

	/**
	 * オブザーバパターン用メソッド
	 */
	@Override
	public void update(Observable o, Object arg) {

		if (o instanceof SelectedEntities) {

			final SelectedEntities<?> selectedEntities = (SelectedEntities<?>) o;
			if (selectedEntities.getLabel().equals(CodeCloneInfo.CODECLONE)) {

				if (SelectedEntities.<CodeCloneInfo> getInstance(
						CodeCloneInfo.CODECLONE).isSet()) {

					final CodeCloneInfo codeclone = SelectedEntities
							.<CodeCloneInfo> getInstance(
									CodeCloneInfo.CODECLONE).get().first();
					final SortedSet<MethodInfo> methods = codeclone
							.getOwnerMethods();

					final Graph<MethodInfo, MethodCallInfo> graph = new SparseGraph<MethodInfo, MethodCallInfo>();
					for (final MethodInfo method : methods) {
						graph.addVertex(method);
					}

					final Layout<MethodInfo, MethodCallInfo> layout = new CircleLayout<MethodInfo, MethodCallInfo>(
							graph);
					layout.setSize(new Dimension(this.getWidth(), this
							.getHeight()));
					final BasicVisualizationServer<MethodInfo, MethodCallInfo> server = new BasicVisualizationServer<MethodInfo, MethodCallInfo>(
							layout);
					server.getRenderContext().setVertexLabelTransformer(
							new ToStringLabeller<MethodInfo>());
					server.getRenderContext().setEdgeLabelTransformer(
							new ToStringLabeller<MethodCallInfo>());
					// server.getRenderContext().setEdgeShapeTransformer(
					// new EdgeShape.Line<Integer, String>());
					// server.getRenderer().getVertexLabelRenderer().setPosition(
					// Position.CNTR);

					this.setLayout(new BorderLayout());
					this.add(server, BorderLayout.CENTER);
				} else {
					this.removeAll();
				}

				this.validate();
				this.repaint();
			}
		}
	}

}
