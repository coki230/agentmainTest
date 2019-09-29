import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;

public class Agentmain {
    public static void agentmain(String args, Instrumentation inst) throws UnmodifiableClassException {
        System.out.println("agentmain start");
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                System.out.println("className is " + className);
                return classfileBuffer;
            }
        }, true);
        inst.retransformClasses(Account.class);
    }
}
