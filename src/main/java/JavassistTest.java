import javassist.*;
import javassist.bytecode.AccessFlag;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Javassist中最为重要的是ClassPool，CtClass ，CtMethod 以及 CtField这几个类。
 *
 * ClassPool：一个基于HashMap实现的CtClass对象容器，其中键是类名称，值是表示该类的CtClass对象。默认的ClassPool使用与底层JVM相同的类路径，因此在某些情况下，可能需要向ClassPool添加类路径或类字节。
 *
 * CtClass：表示一个类，这些CtClass对象可以从ClassPool获得。
 *
 * CtMethods：表示类中的方法。
 *
 * CtFields ：表示类中的字段。
 * ————————————————
 * 版权声明：本文为CSDN博主「ShuSheng007」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
 * 原文链接：https://blog.csdn.net/ShuSheng0007/article/details/81269295
 */
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
            // 方法前后插入代码
            modifyMethod();
            // 将生成的class文件保存到磁盘
            ctClass.writeFile();

            // 测试
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

    /**
     *
     */
    private void modifyMethod() {
        ClassPool pool = ClassPool.getDefault();
        try {
            CtClass ctClass = pool.getCtClass("top.coki.GenerateClass");
            CtMethod hello = ctClass.getDeclaredMethod("hello");
            // 方法前插入代码，$1代表方法入参的第一个参数。
            hello.insertBefore("System.out.println(\"before test, the name is \" + $1);");
            hello.insertAfter("System.out.println(\"after test.\");");
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        JavassistTest javassistTest = new JavassistTest();
        javassistTest.DynGenerateClass();
    }
}
