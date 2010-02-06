package jp.ac.osaka_u.ist.sel.metricstool.main.parse.asm;


import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.JavaUnresolvedExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.JavaUnresolvedExternalFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.JavaUnresolvedExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.sun.xml.internal.ws.org.objectweb.asm.Type;


public class JavaByteCodeParser implements ClassVisitor {

    public JavaByteCodeParser() {
        this.classInfo = new JavaUnresolvedExternalClassInfo();
    }

    @Override
    public void visit(final int version, final int access, final String name,
            final String signature, final String superName, final String[] interfaces) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == name) {
            throw new IllegalArgumentException();
        } else {
            this.classInfo.setName(name);
        }

        if (null != superName) {
            this.classInfo.setSuperName(superName);
        }

        for (final String interfaceName : interfaces) {
            this.classInfo.addInterface(interfaceName);
        }

        this.classInfo.isInterface(0 != (access & Opcodes.ACC_INTERFACE));

        for (final String modifier : this.getModifiers(access)) {
            this.classInfo.addModifier(modifier);
        }

        // 型パラメータがある場合はその文字列を取得
        if ((null != signature) && (signature.startsWith("<"))) {

            final List<String> typeParameters = this.getTypeParameters(signature);
            for (final String typeParameter : typeParameters) {
                this.classInfo.addTypeParameter(typeParameter);
            }
        }
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void visitAttribute(final Attribute attr) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visitEnd() {
        // TODO Auto-generated method stub

    }

    @Override
    public FieldVisitor visitField(final int access, final String name, final String desc,
            final String signature, final Object value) {

        final JavaUnresolvedExternalFieldInfo field = new JavaUnresolvedExternalFieldInfo();
        if (null == name) {
            throw new IllegalArgumentException();
        } else {
            field.setName(name);
        }

        if (null == desc) {
            throw new IllegalArgumentException();
        } else {
            field.setType(desc);
        }

        this.classInfo.addField(field);
        return null;
    }

    @Override
    public void visitInnerClass(final String name, final String outerName, final String innerName,
            final int value) {
        // TODO Auto-generated method stub

    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc,
            final String signature, final String[] exceptions) {

        final JavaUnresolvedExternalMethodInfo method = new JavaUnresolvedExternalMethodInfo();
        if (null == name) {
            throw new IllegalArgumentException();
        } else {
            method.setName(name);
        }

        if (null == desc) {
            throw new IllegalArgumentException();
        } else {
            for (final Type type : Type.getArgumentTypes(desc)) {
                method.addArgumentType(type.toString());
            }
            method.setReturnType(Type.getReturnType(desc).toString());
        }

        // 型パラメータある場合はその文字列を取得
        if ((null != signature) && (signature.startsWith("<"))) {

            final List<String> typeParameters = this.getTypeParameters(signature);
            for (final String typeParameter : typeParameters) {
                method.addTypeParameter(typeParameter);
            }
        }

        this.classInfo.addMethod(method);
        return null;
    }

    @Override
    public void visitOuterClass(final String owner, final String name, final String desc) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visitSource(final String source, final String debug) {
        // TODO Auto-generated method stub

    }

    public JavaUnresolvedExternalClassInfo getClassInfo() {

        if (null == this.classInfo.getName()) {
            throw new IllegalStateException();
        }

        return this.classInfo;
    }

    private Set<String> getModifiers(final int access) {

        final Set<String> modifiers = new HashSet<String>();

        // ここから修飾子
        if (0 != (access & Opcodes.ACC_PUBLIC)) {
            modifiers.add(ModifierInfo.PUBLIC_STRING);
        }

        if (0 != (access & Opcodes.ACC_PROTECTED)) {
            modifiers.add(ModifierInfo.PROTECTED_STRING);
        }

        if (0 != (access & Opcodes.ACC_PRIVATE)) {
            modifiers.add(ModifierInfo.PRIVATE_STRING);
        }

        if (0 != (access & Opcodes.ACC_FINAL)) {
            modifiers.add(ModifierInfo.FINAL_STRING);
        }

        if (0 != (access & Opcodes.ACC_STATIC)) {
            modifiers.add(ModifierInfo.STATIC_STRING);
        }

        if (0 != (access & Opcodes.ACC_ABSTRACT)) {
            modifiers.add(ModifierInfo.ABSTRACT_STRING);
        }

        if (0 != (access & Opcodes.ACC_SYNCHRONIZED)) {
            modifiers.add(ModifierInfo.SYNCHRONIZED_STRING);
        }

        return Collections.unmodifiableSet(modifiers);
    }

    private List<String> getTypeParameters(final String signature) {

        if (null == signature) {
            throw new IllegalArgumentException();
        }

        if (!signature.startsWith("<")) {
            throw new IllegalArgumentException();
        }

        if (-1 == signature.indexOf('>')) {
            throw new IllegalArgumentException();
        }

        // ジェネリクス情報がおわる位置を取得し，その部分を切り出す        
        int toIndex = 0;
        for (int nestLevel = 0; true; toIndex++) {

            if ('<' == signature.charAt(toIndex)) {
                nestLevel++;
            }

            else if ('>' == signature.charAt(toIndex)) {
                nestLevel--;
                if (0 == nestLevel) {
                    break;
                }
            }
        }
        final String typeParameterString = signature.substring(1, toIndex);

        // ジェネリクス情報を1つ１つ分解し，Setに入れる
        final List<String> typeParameters = new LinkedList<String>();
        int startIndex = 0;
        int endIndex = 0;
        for (int nestLevel = 0; endIndex < typeParameterString.length(); endIndex++) {

            if ('<' == typeParameterString.charAt(endIndex)) {
                nestLevel++;
            }

            else if ('>' == typeParameterString.charAt(endIndex)) {
                nestLevel--;
            }

            else if (';' == typeParameterString.charAt(endIndex) && (0 == nestLevel)) {
                if (endIndex < typeParameterString.length()) {
                    final String typeParameter = typeParameterString.substring(startIndex,
                            endIndex + 1);
                    typeParameters.add(typeParameter);
                } else {
                    final String typeParameter = typeParameterString.substring(startIndex);
                    typeParameters.add(typeParameter);
                }
            }
        }

        return Collections.unmodifiableList(typeParameters);

    }

    private final JavaUnresolvedExternalClassInfo classInfo;
}
