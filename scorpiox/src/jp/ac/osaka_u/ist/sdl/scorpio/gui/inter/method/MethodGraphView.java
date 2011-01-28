package jp.ac.osaka_u.ist.sdl.scorpio.gui.inter.method;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import jp.ac.osaka_u.ist.sdl.scorpio.gui.SelectedEntities;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CodeCloneInfo;

public class MethodGraphView extends JPanel implements Observer {

	/**
	 * オブザーバパターン用メソッド
	 */
	@Override
	public void update(Observable o, Object arg) {

		if (o instanceof SelectedEntities) {

			final SelectedEntities<?> selectedEntities = (SelectedEntities<?>) o;
			if (selectedEntities.getLabel().equals(CodeCloneInfo.CODECLONE)) {

				final CodeCloneInfo codeclone = SelectedEntities
						.<CodeCloneInfo> getInstance(CodeCloneInfo.CODECLONE)
						.get().first();
			}
		}
	}
	
	
}
