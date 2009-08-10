package jp.ac.osaka_u.ist.sdl.scdetector.gui.sourcecode;


import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.JTabbedPane;

import jp.ac.osaka_u.ist.sdl.scdetector.Scorpioui;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.SelectedEntities;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.FileController;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.FileInfo;


/**
 * ソースコード表示用コンポーネント
 * 
 * @author higo
 *
 */
public class SourceCodeView extends JTabbedPane implements Observer {

    /**
     * コンストラクタ
     */
    public SourceCodeView() {
        this.map = new HashMap<CodeCloneInfo, SourceCodePanel>();
    }

    /**
     * オブザーバパターン用メソッド
     */
    @Override
    public void update(Observable o, Object arg) {

        if (o instanceof SelectedEntities) {

            final SelectedEntities<?> selectedEntities = (SelectedEntities<?>) o;
            if (selectedEntities.getLabel().equals(CodeCloneInfo.CODECLONE)) {

                final Set<CodeCloneInfo> codeFragments = SelectedEntities
                        .<CodeCloneInfo> getInstance(CodeCloneInfo.CODECLONE).get();

                // 全てクリア
                this.removeAll();
                this.map.clear();

                //新しく選択されているファイルを開く
                for (final CodeCloneInfo newCodeFragment : codeFragments) {
                    final int fileID = newCodeFragment.getFirstElement().getFileID();
                    final FileInfo file = FileController.getInstance(Scorpioui.ID).getFile(
                            fileID);
                    final SourceCodePanel panel = new SourceCodePanel(file);
                    this.add(panel);
                    panel.addHighlight(newCodeFragment);
                    panel.display(newCodeFragment);
                    this.map.put(newCodeFragment, panel);
                }
            }
        }
    }

    private final Map<CodeCloneInfo, SourceCodePanel> map;
}
