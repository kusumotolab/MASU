package jp.ac.osaka_u.ist.sdl.scorpio.gui.inter.sourcecode;

import java.awt.GridLayout;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.JPanel;

import jp.ac.osaka_u.ist.sdl.scorpio.gui.SelectedEntities;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.MethodInfo;

/**
 * �\�[�X�R�[�h�\���p�R���|�[�l���g
 * 
 * @author higo
 * 
 */
public class SourceCodeView extends JPanel implements Observer {

	/**
	 * �R���X�g���N�^
	 */
	public SourceCodeView() {
		this.map = new HashMap<MethodInfo, SourceCodePanel>();
	}

	/**
	 * �I�u�U�[�o�p�^�[���p���\�b�h
	 */
	@Override
	public void update(Observable o, Object arg) {

		if (o instanceof SelectedEntities) {

			final SelectedEntities<?> selectedEntities = (SelectedEntities<?>) o;
			if (selectedEntities.getLabel().equals(MethodInfo.METHOD)) {

				final Set<MethodInfo> methods = SelectedEntities
						.<MethodInfo> getInstance(MethodInfo.METHOD).get();

				// �}�b�v����V�����I������Ă��Ȃ����\�b�h�Q���폜
				final Set<MethodInfo> oldMethods = new HashSet<MethodInfo>(
						this.map.keySet());
				for (final MethodInfo oldMethod : oldMethods) {
					if (!methods.contains(oldMethod)) {
						this.map.remove(oldMethod);
					}
				}

				for (final MethodInfo newMethod : methods) {
					if (!this.map.containsKey(newMethod)) {
						final SourceCodePanel panel = new SourceCodePanel(
								newMethod);
						this.map.put(newMethod, panel);
						final CodeCloneInfo codeclone = newMethod
								.getCodeClone();
						panel.addHighlight(codeclone);
						panel.display(codeclone);
					}
				}

				// �S�ăN���A
				this.removeAll();

				// �V�����I������Ă���R�[�h�N���[���Q�̃p�l����\��
				this.setLayout(new GridLayout(1, this.map.size()));
				for (final SourceCodePanel panel : this.map.values()) {
					this.add(panel);
				}

				this.validate();
				this.repaint();
			}

			else {
				this.removeAll();
				this.map.clear();
				this.validate();
				this.repaint();
			}
		}

	}

	private final Map<MethodInfo, SourceCodePanel> map;
}
