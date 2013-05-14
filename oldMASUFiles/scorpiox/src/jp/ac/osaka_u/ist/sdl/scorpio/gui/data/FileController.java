package jp.ac.osaka_u.ist.sdl.scorpio.gui.data;


import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 * GUI�Ńt�@�C�����Ǘ����邽�߂̃N���X
 * 
 * @author higo
 */
public class FileController {

    private FileController() {
        this.files = new TreeMap<Integer, FileInfo>();
    }

    /**
     * �t�@�C����ǉ�����
     * 
     * @param file �ǉ�����t�@�C��
     */
    public void add(final FileInfo file) {

        if (null == file) {
            throw new IllegalArgumentException();
        }

        this.files.put(file.getID(), file);
    }

    /**
     * ID�Ŏw�肳�ꂽ�t�@�C����Ԃ�
     * 
     * @param id �t�@�C����ID
     * @return ID�Ŏw�肳�ꂽ�t�@�C��
     */
    public FileInfo getFile(final int id) {
        return this.files.get(id);
    }

    /**
     * �t�@�C����Collection��Ԃ�
     * 
     * @return �t�@�C����Collection
     */
    public Collection<FileInfo> getFiles() {
        return Collections.unmodifiableCollection(this.files.values());
    }

    /**
     * �t�@�C���̐���Ԃ�
     * 
     * @return�@�t�@�C���̐�
     */
    public int getNumberOfFiles() {
        return this.files.size();
    }

    /**
     * �t�@�C�������ׂď���
     */
    public void clear() {
        this.files.clear();
    }

    private final SortedMap<Integer, FileInfo> files;

    /**
     * �w�肳�ꂽFileController��Ԃ�
     * 
     * @param id
     * @return
     */
    public static FileController getInstance(final String id) {

        FileController controller = map.get(id);
        if (null == controller) {
            controller = new FileController();
            map.put(id, controller);
        }

        return controller;
    }

    private static final Map<String, FileController> map = new HashMap<String, FileController>();
}
