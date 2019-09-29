import javassist.*;
import javassist.bytecode.AccessFlag;

import java.io.IOException;
import java.lang.reflect.Field;

public class JavassistTest {
    public void DynGenerateClass() {
        ClassPool pool = ClassPool.getDefault();
        // 创建类
        CtClass ctClass = pool.makeClass("top.coki.GenerateClass");
        // 让类实现一个接口
        ctClass.setInterfaces(new CtClass[]{pool.makeInterface("java.lang.Cloneable")});

        try {
            // 获得一个类型为int，名称为id的字段
            CtField ctField = new CtField(CtClass.intType, "id", ctClass);
            // 设置类型public
            ctField.setModifiers(AccessFlag.PUBLIC);
            // 将这个字段添加到类上
            ctClass.addField(ctField);
            // 添加构造函数
            CtConstructor ctConstructor = CtNewConstructor.make("public GenerateClass(int pid){this.id = pid;}", ctClass);
            // 把该构造函数添加到此类上
            ctClass.addConstructor(ctConstructor);
            // 添加方法
            CtMethod ctMethod = CtNewMethod.make("public void hello(String name){System.out.println(\"hello \" + name);}", ctClass);
            ctClass.addMethod(ctMethod);
            // 将生成的class文件保存到磁盘
            ctClass.writeFile();

            Field[] fields = ctClass.toClass().getFields();
            System.out.println(fields[0].getName() + "==>" + fields[0].getType());

        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        JavassistTest javassistTest = new JavassistTest();
        javassistTest.DynGenerateClass();
    }
}
