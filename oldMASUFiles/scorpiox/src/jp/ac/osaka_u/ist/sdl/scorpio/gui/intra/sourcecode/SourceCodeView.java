package jp.ac.osaka_u.ist.sdl.scorpio.gui.intra.sourcecode;

import java.awt.GridLayout;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.JPanel;

import jp.ac.osaka_u.ist.sdl.scorpio.ScorpioGUI;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.SelectedEntities;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.FileController;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.FileInfo;

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
		this.map = new HashMap<CodeCloneInfo, SourceCodePanel>();
	}

	/**
	 * �I�u�U�[�o�p�^�[���p���\�b�h
	 */
	@Override
	public void update(Observable o, Object arg) {

		if (o instanceof SelectedEntities) {

			final SelectedEntities<?> selectedEntities = (SelectedEntities<?>) o;
			if (selectedEntities.getLabel().equals(CodeCloneInfo.CODECLONE)) {

				final Set<CodeCloneInfo> codeclones = SelectedEntities
						.<CodeCloneInfo> getInstance(CodeCloneInfo.CODECLONE)
						.get();

				// �}�b�v����V�����I������Ă��Ȃ��R�[�h�N���[���Q���폜
				final Set<CodeCloneInfo> oldCodeclones = new HashSet<CodeCloneInfo>(
						this.map.keySet());
				for (final CodeCloneInfo oldCodeClone : oldCodeclones) {
					if (!codeclones.contains(oldCodeClone)) {
						this.map.remove(oldCodeClone);
					}
				}

				for (final CodeCloneInfo newCodeclone : codeclones) {
					if (!this.map.containsKey(newCodeclone)) {
						final int fileID = newCodeclone.getFirstElement()
								.getFileID();
						final FileInfo file = FileController.getInstance(
								ScorpioGUI.ID).getFile(fileID);
						final SourceCodePanel panel = new SourceCodePanel(file);
						this.map.put(newCodeclone, panel);
						panel.addHighlight(newCodeclone);
						panel.display(newCodeclone);
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
		}
	}

	// private final Set<FileInfo> getOwnerFiles(final CodeCloneInfo codeclone)
	// {
	//
	// final Set<FileInfo> files = new HashSet<FileInfo>();
	// for (final ElementInfo element : codeclone.getElements()) {
	// final int fileID = element.getFileID();
	// final FileInfo file = FileController.getInstance(ScorpioGUI.ID)
	// .getFile(fileID);
	// files.add(file);
	// }
	//
	// return files;
	// }

	private final Map<CodeCloneInfo, SourceCodePanel> map;
}
