package jp.ac.osaka_u.ist.sel.metricstool.dit;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractClassMetricPlugin;


/**
 * DIT(CK���g���N�X��1��)���v�Z���郁�g���N�X
 * <p>
 * �S�ẴI�u�W�F�N�g�w������ɑΉ�
 * 
 * @author y-higo
 * 
 */
public class DITPlugin extends AbstractClassMetricPlugin {

    /**
     * �����ŗ^����ꂽ�N���X��DIT���v�Z����
     * 
     * @param targetClass DIT�v�Z�ΏۃN���X
     * @return �v�Z����
     */
    @Override
    protected Number measureClassMetric(TargetClassInfo targetClass) {

        ClassInfo classInfo = targetClass;
        for (int depth = 1;; depth++) {

            final List<ClassTypeInfo> superClasses = classInfo.getSuperClasses();
            if (0 == superClasses.size()) {
                return depth;
            }
            classInfo = superClasses.get(0).getReferencedClass();
        }
    }

    /**
     * ���̃v���O�C���̊ȈՐ������P�s�ŕԂ�
     * 
     * @return �ȈՐ���������
     */
    @Override
    protected String getDescription() {
        return "Measuring the DIT metric.";
    }

    /**
     * ���̃v���O�C�����v�����郁�g���N�X�̖��O��Ԃ�
     * 
     * @return DIT
     */
    @Override
    protected String getMetricName() {
        return "DIT";
    }

    /**
     * ���̃v���O�C�����t�B�[���h�Ɋւ�����𗘗p���邩�ǂ�����Ԃ����\�b�h�D false��Ԃ��D
     * 
     * @return false�D
     */
    @Override
    protected boolean useFieldInfo() {
        return false;
    }

    /**
     * ���̃v���O�C�������\�b�h�Ɋւ�����𗘗p���邩�ǂ�����Ԃ����\�b�h�D false��Ԃ��D
     * 
     * @return false�D
     */
    @Override
    protected boolean useMethodInfo() {
        return false;
    }
}
