package jp.ac.osaka_u.ist.sdl.scdetector.gui.data;


/**
 * 
 * GUI�Ńt�@�C������\�����߂̃N���X
 * 
 * @author higo
 *
 */
public class FileInfo implements Comparable<FileInfo> {

    /**
     * �R���X�g���N�^
     * 
     * @param name �t�@�C����
     * @param id �t�@�C����ID
     */
    public FileInfo(final String name, final int id, final int loc, final int numberOfPDGNodes) {
        this.name = name;
        this.id = id;
        this.loc = loc;
        this.numberOfPDGNodes = numberOfPDGNodes;
    }

    public FileInfo() {
    }

    /**
     * �t�@�C������Ԃ�
     * 
     * @return �t�@�C����
     */
    public String getName() {
        return this.name;
    }

    /**
     * �t�@�C������ݒ肷��
     * 
     * @param name �ݒ肷��t�@�C����
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * �t�@�C����ID��Ԃ�
     * 
     * @return�@�t�@�C����ID
     */
    public int getID() {
        return this.id;
    }

    /**
     * �t�@�C����ID��ݒ肷��
     * 
     * @param id �ݒ肷��ID
     */
    public void setID(final int id) {
        this.id = id;
    }

    /**
     * �t�@�C���̍s����Ԃ�
     * 
     * @return�@�t�@�C���̍s��
     */
    public int getLOC() {
        return this.loc;
    }

    /**
     * �t�@�C���̍s����ݒ肷��
     * 
     * @param loc �ݒ肷��s��
     */
    public void setLOC(final int loc) {
        this.loc = loc;
    }

    /**
     * PDG�̃m�[�h����Ԃ�
     * 
     * @return�@PDG�̃m�[�h��
     */
    public int getNumberOfPDGNodes() {
        return this.numberOfPDGNodes;
    }

    /**
     * PDG�̃m�[�h����ݒ肷��
     * 
     * @param numberOfPDGNodes �ݒ肷��PDG�̃m�[�h��
     */
    public void setNumberOfPDGNodes(final int numberOfPDGNodes) {
        this.numberOfPDGNodes = numberOfPDGNodes;
    }

    /**
     * ��r�֐�
     */
    @Override
    public int compareTo(FileInfo o) {
        if (this.getID() < o.getID()) {
            return -1;
        } else if (this.getID() > o.getID()) {
            return 1;
        } else {
            return 0;
        }
    }

    private String name;

    private int id;

    private int loc;

    private int numberOfPDGNodes;
}
