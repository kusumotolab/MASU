package jp.ac.osaka_u.ist.sel.metricstool.cbo;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractClassMetricPlugin;


/**
 * CBO(CK���g���N�X��1��)���v�Z���郁�g���N�X
 * <p>
 * �S�ẴI�u�W�F�N�g�w������ɑΉ�
 * 
 * @author y-higo
 * 
 */
public class CBOPlugin extends AbstractClassMetricPlugin {

    /**
     * �����ŗ^����ꂽ�N���X��CBO���v�Z����
     * 
     * @param targetClass
     *            CBO�v�Z�ΏۃN���X
     * @return �v�Z����
     */
    @Override
    protected Number measureClassMetric(TargetClassInfo targetClass) {

        SortedSet<ClassInfo> classes = new TreeSet<ClassInfo>();

        // �t�B�[���h�Ŏg�p����Ă���N���X�^���擾
        for (final FieldInfo field : targetClass.getDefinedFields()) {
            final TypeInfo type = field.getType();
            classes.addAll(this.getCohesiveClasses(type));
        }

        // ���\�b�h�Ŏg�p����Ă���N���X�^���擾
        for (final MethodInfo method : targetClass.getDefinedMethods()) {

            // �Ԃ�l�ɂ��Ă̏���
            {
                final TypeInfo returnType = method.getReturnType();
                classes.addAll(this.getCohesiveClasses(returnType));
            }

            // �����̂��Ă̏���
            for (final ParameterInfo parameter : method.getParameters()) {
                final TypeInfo parameterType = parameter.getType();
                classes.addAll(this.getCohesiveClasses(parameterType));
            }

            // ���[�J���ϐ��ɂ��Ă̏���
            for (final VariableInfo<? extends UnitInfo> variable : LocalVariableInfo
                    .getLocalVariables(method.getDefinedVariables())) {
                final TypeInfo variableType = variable.getType();
                classes.addAll(this.getCohesiveClasses(variableType));
            }
        }

        // �������g�͎�菜��
        classes.remove(targetClass);

        return classes.size();
    }

    /**
     * �^����ꂽ�^�ŗ��p����Ă���N���X��SortedSet��Ԃ�
     * 
     * @param type �Ώۂ̌^
     * @return type�ŗ��p����Ă���N���X��SortedSet
     */
    private SortedSet<ClassInfo> getCohesiveClasses(final TypeInfo type) {

        final SortedSet<ClassInfo> cohesiveClasses = new TreeSet<ClassInfo>();

        if (type instanceof ClassTypeInfo) {
            final ClassTypeInfo classType = (ClassTypeInfo) type;
            cohesiveClasses.add(classType.getReferencedClass());
            for (final TypeInfo typeArgument : classType.getTypeArguments()) {
                cohesiveClasses.addAll(this.getCohesiveClasses(typeArgument));
            }
        }

        return Collections.unmodifiableSortedSet(cohesiveClasses);
    }

    /**
     * ���̃v���O�C���̊ȈՐ������P�s�ŕԂ�
     * 
     * @return �ȈՐ���������
     */
    @Override
    protected String getDescription() {
        return "Measuring the CBO metric.";
    }

    /**
     * ���̃v���O�C�����v�����郁�g���N�X�̖��O��Ԃ�
     * 
     * @return CBO
     */
    @Override
    protected String getMetricName() {
        return "CBO";
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
}
