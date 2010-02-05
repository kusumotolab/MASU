package jp.ac.osaka_u.ist.sel.metricstool.main.parse.asm;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.JavaUnresolvedExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.JavaUnresolvedExternalFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.JavaUnresolvedExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

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

    private final JavaUnresolvedExternalClassInfo classInfo;
}
