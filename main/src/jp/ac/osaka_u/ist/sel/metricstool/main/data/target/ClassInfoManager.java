package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;


/**
 * �N���X�����Ǘ�����N���X�D ClassInfo ��v�f�Ƃ��Ď��D
 * 
 * @author y-higo
 * 
 */
public class ClassInfoManager implements Iterable<ClassInfo> {

    /**
     * �N���X�����Ǘ����Ă���C���X�^���X��Ԃ��D �V���O���g���p�^�[���������Ă���D
     * 
     * @return �N���X�����Ǘ����Ă���C���X�^���X
     */
    public static ClassInfoManager getInstance() {
        if (CLASS_INFO_DATA == null) {
            CLASS_INFO_DATA = new ClassInfoManager();
        }
        return CLASS_INFO_DATA;
    }

    /**
     * 
     * @param classInfo �ǉ�����N���X��� (classInfo)
     */
    public void add(final ClassInfo classInfo) {
        this.classInfos.add(classInfo);
    }

    /**
     * �N���X���� Iterator ��Ԃ��D���� Iterator �� unmodifiable �ł���C�ύX������s�����Ƃ͂ł��Ȃ��D
     */
    public Iterator<ClassInfo> iterator() {
        Set<ClassInfo> unmodifiableClassInfos = Collections.unmodifiableSet(this.classInfos);
        return unmodifiableClassInfos.iterator();
    }

    /**
     * 
     * �R���X�g���N�^�D �V���O���g���p�^�[���Ŏ������Ă��邽�߂� private �����Ă���D
     */
    private ClassInfoManager() {
        this.classInfos = new TreeSet<ClassInfo>();
    }

    /**
     * 
     * �V���O���g���p�^�[�����������邽�߂̕ϐ��D
     */
    private static ClassInfoManager CLASS_INFO_DATA = null;

    /**
     * 
     * �N���X��� (ClassInfo) ���i�[����ϐ��D
     */
    private final Set<ClassInfo> classInfos;
}