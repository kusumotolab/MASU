package jp.ac.osaka_u.ist.sdl.scdetector.gui.sourcecode;


import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.JTabbedPane;

import jp.ac.osaka_u.ist.sdl.scdetector.CodeFragmentInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.gui.SelectedEntities;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;


public class SourceCodeView extends JTabbedPane implements Observer {

    public SourceCodeView() {
        this.map = new HashMap<CodeFragmentInfo, SourceCodePanel>();
    }

    public void update(Observable o, Object arg) {

        if (o instanceof SelectedEntities) {

            final SelectedEntities<?> selectedEntities = (SelectedEntities<?>) o;
            if (selectedEntities.getLabel().equals(CodeFragmentInfo.CODEFRAGMENT)) {

                final Set<CodeFragmentInfo> codeFragments = SelectedEntities
                        .<CodeFragmentInfo> getInstance(CodeFragmentInfo.CODEFRAGMENT).get();

                // 全てクリア
                this.removeAll();
                this.map.clear();

                //新しく選択されているファイルを開く
                for (final CodeFragmentInfo newCodeFragment : codeFragments) {
                    final FileInfo ownerFile = getOwnerFile(newCodeFragment.first());
                    final SourceCodePanel panel = new SourceCodePanel(ownerFile);
                    this.add(panel);
                    panel.addHighlight(newCodeFragment);
                    panel.display(newCodeFragment);
                    this.map.put(newCodeFragment, panel);
                }
            }
        }
    }

    private static FileInfo getOwnerFile(final ExecutableElementInfo element) {

        final LocalSpaceInfo ownerSpace = element.getOwnerSpace();
        final ClassInfo ownerClass = ownerSpace.getOwnerClass();
        return ((TargetClassInfo) ownerClass).getOwnerFile();
    }

    private final Map<CodeFragmentInfo, SourceCodePanel> map;
}
