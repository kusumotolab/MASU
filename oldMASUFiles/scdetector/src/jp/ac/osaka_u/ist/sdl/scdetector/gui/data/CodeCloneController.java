package jp.ac.osaka_u.ist.sdl.scdetector.gui.data;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * GUI�ŃN���[���Z�b�g���Ǘ����邽�߂̃N���X
 * 
 * @author higo
 *
 */
public class CodeCloneController {

    private CodeCloneController() {
        this.clonesets = new TreeSet<CloneSetInfo>();
    }

    /**
     * �N���[���Z�b�g��ǉ�����
     * 
     * @param cloneset �ǉ�����N���[���Z�b�g
     */
    public void add(final CloneSetInfo cloneset) {

        if (null == cloneset) {
            throw new IllegalArgumentException();
        }

        this.clonesets.add(cloneset);
    }

    /**
     * �N���[���Z�b�g��SortedSet��Ԃ�
     * 
     * @return �N���[���Z�b�g��SortedSet
     */
    public SortedSet<CloneSetInfo> getCloneSets() {
        return Collections.unmodifiableSortedSet(this.clonesets);
    }

    /**
     * �N���[���Z�b�g�̐���Ԃ�
     * 
     * @return�@�N���[���Z�b�g�̐�
     */
    public int getNumberOfClonesets() {
        return this.clonesets.size();
    }

    /**
     * �N���[���Z�b�g�����ׂď���
     */
    public void clear() {
        this.clonesets.clear();
    }

    private SortedSet<CloneSetInfo> clonesets;

    /**
     * �w�肳�ꂽCodeCloneController��Ԃ�
     * 
     * @param id
     * @return
     */
    public static CodeCloneController getInstance(final String id) {

        CodeCloneController controller = map.get(id);
        if (null == controller) {
            controller = new CodeCloneController();
            map.put(id, controller);
        }

        return controller;
    }

    private static final Map<String, CodeCloneController> map = new HashMap<String, CodeCloneController>();
}
