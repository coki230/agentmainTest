import com.sun.tools.attach.*;

import java.io.IOException;
import java.util.List;

public class JVMTIThread {
    public static void main(String[] args) {
        List<VirtualMachineDescriptor> list = VirtualMachine.list();
        list.forEach(vmd -> {
            if (vmd.displayName().endsWith("AccountMain")) {
                try {
                    VirtualMachine virtualMachine = VirtualMachine.attach(vmd.id());
                    virtualMachine.loadAgent("D:\\work\\agentmainTest\\out\\artifacts\\agentmainTest_jar\\agentmainTest.jar", "cxs");
                    System.out.println("ok");
                    virtualMachine.detach();
                } catch (AttachNotSupportedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (AgentLoadException e) {
                    e.printStackTrace();
                } catch (AgentInitializationException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
