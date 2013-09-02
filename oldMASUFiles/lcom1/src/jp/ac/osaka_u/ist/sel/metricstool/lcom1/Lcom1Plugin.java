package jp.ac.osaka_u.ist.sel.metricstool.lcom1;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractClassMetricPlugin;


/**
 * 
 * LCOM1(CK���g���N�X��LCOM)���v������v���O�C���N���X.
 * <p>
 * �S�ẴI�u�W�F�N�g�w������ɑΉ�. �N���X�C���\�b�h�C�t�B�[���h�C���\�b�h�����̏���K�v�Ƃ���.
 * 
 * @author kou-tngt
 * 
 */
public class Lcom1Plugin extends AbstractClassMetricPlugin {
    // �I�u�W�F�N�g�����x����������̂�������邽�߂Ƀt�B�[���h�Ƃ��Đ�������
    /**
     * �ΏۃN���X�̃��\�b�h�ꗗ. ���̃I�u�W�F�N�g�͍ė��p�����.
     */
    final List<MethodInfo> methods = new ArrayList<MethodInfo>(100);

    /**
     * �ΏۃN���X�̃C���X�^���X�t�B�[���h�ꗗ. ���̃I�u�W�F�N�g�͍ė��p�����.
     */
    final Set<FieldInfo> instanceFields = new HashSet<FieldInfo>();

    /**
     * �g�p���ꂽ�t�B�[���h�ꗗ ���̃I�u�W�F�N�g�͍ė��p�����.
     */
    final Set<FieldInfo> usedFields = new HashSet<FieldInfo>();

    /**
     * �ė��p���Ă���I�u�W�F�N�g����ɂ���.
     */
    protected void clearReusedObjects() {
        methods.clear();
        instanceFields.clear();
        usedFields.clear();
    }

    /**
     * �I�u�W�F�N�g�ė��p�̌�n��.
     */
    @Override
    protected void teardownExecute() {
        clearReusedObjects();
    }

    /**
     * ���g���N�X�̌v��.
     * 
     * @param targetClass
     *            �Ώۂ̃N���X
     */
    @Override
    protected Number measureClassMetric(TargetClassInfo targetClass) {
        // �I�u�W�F�N�g�ė��p�̂��߂̏���
        clearReusedObjects();

        int p = 0;
        int q = 0;

        methods.addAll(targetClass.getDefinedMethods());

        // ���̃N���X�̃C���X�^���X�t�B�[���h�̃Z�b�g���擾
        instanceFields.addAll(targetClass.getDefinedFields());
        for (Iterator<FieldInfo> it = instanceFields.iterator(); it.hasNext();) {
            if (it.next().isStaticMember()) {
                it.remove();
            }
        }

        final int methodCount = methods.size();

        // �t�B�[���h�𗘗p���郁�\�b�h��1���Ȃ����ǂ���
        boolean allMethodsDontUseAnyField = true;

        // �S���\�b�hi�΂���
        for (int i = 0; i < methodCount; i++) {
            // ���\�b�hi���擾���āC���or�Q�Ƃ��Ă���t�B�[���h��S��set�ɓ����
            final MethodInfo firstMethod = methods.get(i);
            for (final FieldUsageInfo assignment : FieldUsageInfo.getFieldUsages(VariableUsageInfo
                    .getAssignments(firstMethod.getVariableUsages()))) {
                this.usedFields.add(assignment.getUsedVariable());
            }

            for (final FieldUsageInfo reference : FieldUsageInfo.getFieldUsages(VariableUsageInfo
                    .getReferencees(firstMethod.getVariableUsages()))) {
                this.usedFields.add(reference.getUsedVariable());
            }

            // ���N���X�̃C���X�^���X�t�B�[���h�������c��
            usedFields.retainAll(instanceFields);

            if (allMethodsDontUseAnyField) {
                // �܂��ǂ̃��\�b�h��1���t�B�[���h�𗘗p���Ă��Ȃ��ꍇ
                allMethodsDontUseAnyField = usedFields.isEmpty();
            }

            // i�ȍ~�̃��\�b�hj�ɂ���
            for (int j = i + 1; j < methodCount; j++) {
                // ���\�b�hj���擾���āC�Q�Ƃ��Ă���t�B�[���h���P�ł�set�ɂ��邩�ǂ����𒲂ׂ�
                final MethodInfo secondMethod = methods.get(j);
                boolean isSharing = false;
                for (final FieldUsageInfo secondUsedField : FieldUsageInfo
                        .getFieldUsages(VariableUsageInfo.getReferencees(secondMethod
                                .getVariableUsages()))) {
                    if (usedFields.contains(secondUsedField.getUsedVariable())) {
                        isSharing = true;
                        break;
                    }
                }

                // ������Ă���t�B�[���h���P�ł�set�ɂ��邩�ǂ����𒲂ׂ�
                if (!isSharing) {
                    for (final FieldUsageInfo secondUsedField : FieldUsageInfo
                            .getFieldUsages(VariableUsageInfo.getAssignments(secondMethod
                                    .getVariableUsages()))) {
                        if (usedFields.contains(secondUsedField.getUsedVariable())) {
                            isSharing = true;
                            break;
                        }
                    }
                }

                // ���L���Ă���t�B�[���h�������q���C�Ȃ����p�𑝂₷
                if (isSharing) {
                    q++;
                } else {
                    p++;
                }
            }

            usedFields.clear();
        }

        if (p <= q || allMethodsDontUseAnyField) {
            // p��q�ȉ��C�܂��͑S�Ẵ��\�b�h���t�B�[���h�𗘗p���Ȃ��ꍇlcom��0
            return Integer.valueOf(0);
        } else {
            // �����łȂ��Ȃ�p-q��lcom
            return Integer.valueOf(p - q);
        }
    }

    /**
     * ���̃v���O�C���̊ȈՐ������P�s�ŕԂ�
     * 
     * @return �ȈՐ���������
     */
    @Override
    protected String getDescription() {
        return "Measuring the LCOM1 metric(CK-metrics's LCOM).";
    }

    /**
     * ���̃v���O�C���̏ڍא�����Ԃ�
     * 
     * @return �ڍא���������
     */
    @Override
    protected String getDetailDescription() {
        return DETAIL_DESCRIPTION;
    }

    /**
     * ���g���N�X����Ԃ��D
     * 
     * @return ���g���N�X��
     */
    @Override
    protected String getMetricName() {
        return "LCOM1";
    }

    /**
     * ���̃v���O�C�����t�B�[���h�Ɋւ�����𗘗p���邩�ǂ�����Ԃ����\�b�h�D true��Ԃ��D
     * 
     * @return true�D
     */
    @Override
    protected boolean useFieldInfo() {
        return true;
    }

    /**
     * ���̃v���O�C�������\�b�h�Ɋւ�����𗘗p���邩�ǂ�����Ԃ����\�b�h�D true��Ԃ��D
     * 
     * @return true�D
     */
    @Override
    protected boolean useMethodInfo() {
        return true;
    }

    /**
     * �ڍא���������萔
     */
    private final static String DETAIL_DESCRIPTION;

    static {
        final String lineSeparator = "\n";// System.getProperty("line.separator");//TODO
        // ���̕ӂ̃Z�L�����e�B�͊ɘa����������������
        final StringBuilder builder = new StringBuilder();

        builder.append("This plugin measures the LCOM1 metric(CK-metrics's LCOM).");
        builder.append(lineSeparator);
        builder
                .append("The LCOM1 is one of the class cohesion metrics measured by following steps:");
        builder.append(lineSeparator);
        builder.append("1. P is a set of pairs of methods which do not share any field.");
        builder.append("If all methods do not use any field, P is a null set.");
        builder.append(lineSeparator);
        builder.append("2. Q is a set of pairs of methods which share some fields.");
        builder.append(lineSeparator);
        builder.append("3. If |P| > |Q|, the result is measured as |P| - |Q|, otherwise 0.");
        builder.append(lineSeparator);

        DETAIL_DESCRIPTION = builder.toString();
    }
}
